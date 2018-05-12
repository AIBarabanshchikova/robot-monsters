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
import java.util.HashSet;
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
                    /*try (BufferedWriter outputWriter = Files.newBufferedWriter(Paths.get("out.txt"))) {
                        HashSet<RobotModel> models = new HashSet<>();
                        Component[] components = getContentPane().getComponents();
                        for (Component comp: components) {
                            AssociatedFrame frame = (AssociatedFrame) comp;
                            outputWriter.write(String.format("WIN %s %d %d %d %d %d\r\n",
                                    frame.getFrameType(),
                                    frame.getX(), frame.getY(), frame.getWidth(), frame.getHeight(),
                                    frame.getRobotModel().hashCode()));
                            models.add(frame.getRobotModel());
                        }
                        for (RobotModel model: models) {
                            outputWriter.write(String.format("MODEL %d %d %d\r\n",
                                    model.hashCode(),
                                    model.getPosition().x, model.getPosition().y));
                        }
                        for (RobotModel model: models)
                            for (Obstacle obs: model.getObstacles()){
                                outputWriter.write(String.format("OBS %d %d %d\r\n",
                                        model.hashCode(),
                                        obs.x, obs.y));
                            }
                        outputWriter.flush();
                        outputWriter.close();
                        System.exit(0);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }*/
                    System.exit(0);
                }

            }

            //метод восстановления из файла положения окошек
            @Override
            public void windowOpened(WindowEvent e) {
                /*try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\fyfcn\\Desktop\\Robots-master\\out.txt"))) {
                    String[] values;
                    String line;
                    Map<Integer, RobotModel> models = new HashMap<>();
                    while ((line = br.readLine()) != null) {
                        int id;
                        RobotModel robotModel;
                        values = line.split(" ");
                        if (values[0].equals("WIN")) {
                            id = Integer.parseInt(values[6]);
                            models.putIfAbsent(id, new RobotModel());
                            robotModel = models.get(id);
                            int x = Integer.parseInt(values[2]);
                            int y = Integer.parseInt(values[3]);
                            int width = Integer.parseInt(values[4]);
                            int height = Integer.parseInt(values[5]);
                            if (values[1].equals("LOG")) {
                                resizeWindow(createLogWindow(robotModel), x, y, width, height);
                            } else if (values[1].equals("GAME")) {
                                resizeWindow(createGameWindow(robotModel), x, y, width, height);
                            } else {
                                CoordinatesWindow coordinatesWindow = createCoordinatesWindow(robotModel);
                                resizeWindow(coordinatesWindow, x, y, width, height);
                            }
                        }
                        else if (values[0].equals("MODEL")) {
                            id = Integer.parseInt(values[1]);
                            int x = Integer.parseInt(values[2]);
                            int y = Integer.parseInt(values[3]);
                            robotModel = models.get(id);
                            robotModel.setX(x);
                            robotModel.setY(y);
                            robotModel.setTargetX(x);
                            robotModel.setTargetY(y);
                        }
                        else {
                            id = Integer.parseInt(values[1]);
                            robotModel = models.get(id);
                            Point p = new Point(Integer.parseInt(values[2]), Integer.parseInt(values[3]));
                            robotModel.addObstacle(p);
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
                }*/
            }
        });
    }

    //метод, который переопределяет размеры окон
    protected  JInternalFrame resizeWindow(JInternalFrame frame, int x, int y, int width, int height) {
        frame.setLocation(x, y);
        frame.setSize(width, height);
        addWindow(frame);
        return frame;
    }

    //создаём окно с логом
    protected LogWindow createLogWindow(GameField gameField)
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource(), gameField);
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    //создаём окно с игрой
    protected  GameWindow createGameWindow(GameField gameField){
        GameWindow gameWindow = new GameWindow(gameField);
        gameWindow.setSize(400,  400);
        return  gameWindow;
    }

    //создаём окно с координатами
    protected  CoordinatesWindow createCoordinatesWindow(GameField gameField) {
        CoordinatesWindow coordinatesWindow = new CoordinatesWindow(gameField);
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
            GameField gameField = new GameField();
            CoordinatesWindow coordinatesWindow = createCoordinatesWindow(gameField);
            gameField.addObserver(coordinatesWindow);
            addWindow(coordinatesWindow);

            LogWindow logWindow = createLogWindow(gameField);
            addWindow(logWindow);

            GameWindow gameWindow = createGameWindow(gameField);
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
