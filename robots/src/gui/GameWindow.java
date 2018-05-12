package gui;

import java.awt.BorderLayout;
import java.util.Observer;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

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
    }
}
