package gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            //метод выхода из приложения
            @Override
            public void windowClosing(WindowEvent e) {
                UIManager.put("OptionPane.yesButtonText"   , "Да"    );
                UIManager.put("OptionPane.noButtonText"    , "Нет"   );
                int result = JOptionPane.showConfirmDialog(desktopPane, "Вы уверены, что хотите выйти?", "Подтверждение", JOptionPane.YES_NO_OPTION);
                if (result == JOptionPane.YES_OPTION) {
                    try (BufferedWriter outputWriter = Files.newBufferedWriter(Paths.get("out.txt"))) {
                        Component[] components = getContentPane().getComponents();
                        for (Component comp: components) {
                            outputWriter.write(String.format("%s:%d %d %d %d:%d\r\n",
                                    comp.getAccessibleContext().getAccessibleName(),
                                    comp.getX(), comp.getY(), comp.getWidth(), comp.getHeight(),
                                    ((AssociatedFrame) comp).getRobotModel().hashCode()));
                        }
                        outputWriter.flush();
                        outputWriter.close();
                        System.exit(0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }

            }

            //метод восстановления из файла положения окошек
            @Override
            public void windowOpened(WindowEvent e) {
                try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\fyfcn\\Desktop\\Robots-master\\out.txt"))) {
                    String[] values;
                    String line;
                    Map<Integer, RobotModel> models = new HashMap<>();
                    while ((line = br.readLine()) != null) {
                        values = line.split(":");
                        int id = Integer.parseInt(values[2]);
                        models.putIfAbsent(id, new RobotModel());
                        RobotModel robotModel = models.get(id);
                        if (values[0].compareTo("Протокол работы") == 0) {
                            resizeWindow(createLogWindow(robotModel), values[1]);
                        } else if (values[0].compareTo("Игровое поле") == 0) {
                            resizeWindow(createGameWindow(robotModel), values[1]);
                        } else {
                            CoordinatesWindow coordinatesWindow = createCoordinatesWindow(robotModel);
                            resizeWindow(coordinatesWindow, values[1]);
                        }
                    }
                }
                //если файл не найден, то создаём три стандартных окна
                catch (FileNotFoundException ex) {
                    RobotModel robotModel = new RobotModel();
                    CoordinatesWindow coordinatesWindow = createCoordinatesWindow(robotModel);
                    robotModel.addObserver(coordinatesWindow);
                    addWindow(coordinatesWindow);

                    LogWindow logWindow = createLogWindow(robotModel);
                    addWindow(logWindow);

                    GameWindow gameWindow = createGameWindow(robotModel);
                    addWindow(gameWindow);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    //метод, который переопределяет размеры окон
    protected  JInternalFrame resizeWindow(JInternalFrame frame, String line) {
        String[] values;
        values = line.split(" ");
        frame.setLocation(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
        frame.setSize(Integer.parseInt(values[2]), Integer.parseInt(values[3]));
        addWindow(frame);
        return frame;
    }

    //создаём окно с логом
    protected LogWindow createLogWindow(RobotModel robotModel)
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), robotModel);
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    //создаём окно с игрой
    protected  GameWindow createGameWindow(RobotModel robotModel){
        GameWindow gameWindow = new GameWindow(robotModel);
        gameWindow.setSize(400,  400);
        return  gameWindow;
    }

    //создаём окно с координатами
    protected  CoordinatesWindow createCoordinatesWindow(RobotModel robotModel) {
        CoordinatesWindow coordinatesWindow = new CoordinatesWindow(robotModel);
        coordinatesWindow.setLocation(10, 10);
        coordinatesWindow.setSize(400, 700);
        setMinimumSize(coordinatesWindow.getSize());
        coordinatesWindow.pack();
        return  coordinatesWindow;
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
            RobotModel robotModel = new RobotModel();
            CoordinatesWindow coordinatesWindow = createCoordinatesWindow(robotModel);
            robotModel.addObserver(coordinatesWindow);
            addWindow(coordinatesWindow);

            LogWindow logWindow = createLogWindow(robotModel);
            addWindow(logWindow);

            GameWindow gameWindow = createGameWindow(robotModel);
            addWindow(gameWindow);
        });

        menuBar.add(fileMenu);
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
