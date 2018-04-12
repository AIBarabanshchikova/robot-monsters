package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Stack;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;        
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
            screenSize.width  - inset*2,
            screenSize.height - inset*2);

        setContentPane(desktopPane);
        
        
/*        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);*/

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                UIManager.put("OptionPane.yesButtonText"   , "Да"    );
                UIManager.put("OptionPane.noButtonText"    , "Нет"   );
                int result = JOptionPane.showConfirmDialog(desktopPane, "Вы уверены, что хотите выйти?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try (BufferedWriter outputWriter = Files.newBufferedWriter(Paths.get("out.txt"))) {
                        Component[] components = getContentPane().getComponents();
                        for (Component comp: components) {
                            outputWriter.write(String.format("%s:%d %d %d %d\r\n",
                                    comp.getAccessibleContext().getAccessibleName(),
                                    comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight()));
                        }
                        outputWriter.flush();
                        outputWriter.close();
                        System.exit(0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

            @Override
            public void windowOpened(WindowEvent e) {
                try (BufferedReader br = new BufferedReader(new FileReader("out.txt"))) {
                    //String[] values = br.readLine().split(" ");
                    String[] values;
                    String[] values2;
                    String line;
                    while ((line = br.readLine()) != null) {
                        values = line.split(":");
                        if ( values[0].compareTo("Протокол работы") == 0) {
                            values2 = values[1].split(" ");
                            LogWindow logWindow = createLogWindow();
                            logWindow.setLocation(Integer.parseInt(values2[0]), Integer.parseInt(values2[1]));
                            logWindow.setSize(Integer.parseInt(values2[2]), Integer.parseInt(values2[3]));
                            addWindow(logWindow);
                        }
                        else {
                            values2 = values[1].split(" ");
                            GameWindow gameWindow = new GameWindow();
                            gameWindow.setLocation(Integer.parseInt(values2[0]), Integer.parseInt(values2[1]));
                            gameWindow.setSize(Integer.parseInt(values2[2]), Integer.parseInt(values2[3]));
                            addWindow(gameWindow);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
    
    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");

        addMenuItem(lookAndFeelMenu, "Системная схема", (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });

        addMenuItem(lookAndFeelMenu, "Универсальная схема", (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        addMenuItem(testMenu, "Сообщение в лог", (event) -> {
            Logger.debug("Новая строка");
        });

        JMenu fileMenu = new JMenu("Файл");
        fileMenu.setMnemonic(KeyEvent.VK_V);
        fileMenu.getAccessibleContext().setAccessibleDescription(
                "Файловые команды");
        addMenuItem(fileMenu, "Закрыть приложение", (event) -> {
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        });
        addMenuItem(fileMenu, "Добавить окна", (event) -> {
            LogWindow logWindow = createLogWindow();
            addWindow(logWindow);

            GameWindow gameWindow = new GameWindow();
            gameWindow.setSize(400,  400);
            addWindow(gameWindow);
        });

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        
        return menuBar;
    }

    private void addMenuItem (JMenu menu, String text, ActionListener act){
        JMenuItem menuItem = new JMenuItem(text, KeyEvent.VK_S);
        menuItem.addActionListener(act);
        menu.add(menuItem);
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
