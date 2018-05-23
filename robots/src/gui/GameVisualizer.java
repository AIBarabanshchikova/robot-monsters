package gui;

import log.Logger;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.*;
import java.util.Timer;
import java.util.stream.Collectors;

import javax.swing.*;

public class GameVisualizer extends JPanel
{
    private GameField gameField;

    public GameVisualizer(GameField gameField)
    {
        this.gameField = gameField;

        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu menu = new JPopupMenu("Препятствия");
                    JMenuItem item_1 = new JMenuItem("Добавить препятствие");
                    JMenuItem item_2 = new JMenuItem("Удалить препятствие");
                    item_1.addActionListener(l -> gameField.addObstacle(e.getPoint()));
                    item_2.addActionListener(k -> gameField.removeObstacle(e.getPoint()));
                    menu.add(item_1);
                    menu.add(item_2);
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
                else if (SwingUtilities.isMiddleMouseButton(e))
                    gameField.setTarget(e.getPoint());
                else if (SwingUtilities.isLeftMouseButton(e))
                    gameField.removeRobot(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
        Timer timer = new Timer("redraw events generator", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }
    
    private static int round(double value)
    {
        return (int)(value + 0.5);
    }
    
    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        for (RobotModel model: gameField.getModels())
            drawRobot(g2d, model);
        drawTarget(g2d, gameField.getTarget());
        drawObstacles(g2d);
    }
    
    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
    
    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, RobotModel model)
    {
        int robotCenterX = model.getPosition().x;
        int robotCenterY = model.getPosition().y;
        AffineTransform t = AffineTransform.getRotateInstance(0, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.BLUE);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX, robotCenterY - 5, 10, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX , robotCenterY - 5, 10, 10);
        g.drawLine(robotCenterX, robotCenterY + 5, robotCenterX, robotCenterY + 10);
        g.drawLine(robotCenterX - 5, robotCenterY + 5, robotCenterX - 10, robotCenterY + 10);
        g.drawLine(robotCenterX + 5, robotCenterY + 5, robotCenterX + 10, robotCenterY + 10);
    }
    
    private void drawTarget(Graphics2D g, Point p)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0); 
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, p.x, p.y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, p.x, p.y, 5, 5);
    }

    private void drawObstacles(Graphics2D g){
        for (Obstacle obstacle : gameField.getObstacles()) {
            Rectangle rect = obstacle;
            g.setColor(Color.ORANGE);
            g.fillRect(rect.x, rect.y, rect.width, rect.height);
            g.setColor(Color.BLACK);
            g.drawRect(rect.x, rect.y, rect.width, rect.height);
        }
    }
}
