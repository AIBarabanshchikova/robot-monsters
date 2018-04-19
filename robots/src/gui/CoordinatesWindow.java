package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends JInternalFrame implements Observer {
    private TextField m_coordinatesContent;

    public CoordinatesWindow(){
        super("Координаты робота", true, true, true, true);
        m_coordinatesContent = new TextField("");
        m_coordinatesContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add( m_coordinatesContent,BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void update(Observable o, Object arg) {
        RobotModel robotModel = (RobotModel) arg;
        m_coordinatesContent.setText(String.format("%f %f", robotModel.getX(), robotModel.getY()));
    }
}
