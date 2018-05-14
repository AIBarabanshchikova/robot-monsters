package gui;

import java.awt.BorderLayout;
import java.util.Observer;

import javax.swing.*;

public class GameWindow extends AssociatedFrame
{
    private final GameVisualizer m_visualizer;

    public GameWindow(GameField gameField)
    {
        super(gameField, "GAME", true, true, true, true);
        m_visualizer = new GameVisualizer(gameField);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Роботы");
        JMenuItem item = new JMenuItem("Добавить робота");
        item.addActionListener(e -> {
            UIManager.put("OptionPane.yesButtonText"   , "RobotBFSAlgorithm"    );
            UIManager.put("OptionPane.noButtonText"    , "RobotStupidAlgorithm"   );
            int result = JOptionPane.showConfirmDialog(this, "Выберите алгоритм для передвижения робота:", "Выбор алгоритма", JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.YES_OPTION)
                gameField.addRobot(new RobotModel(new RobotBFSAlgo()));
            else
                gameField.addRobot(new RobotModel(new RobotStupidAlgo())); }
        );
        //item.addActionListener((e) -> {gameField.addRobot(new RobotModel(new RobotStupidAlgo()));});
        menu.add(item);
        menuBar.add(menu);
        setJMenuBar(menuBar);
    }
}
