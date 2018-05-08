package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends AssociatedFrame implements Observer {
    private TextField m_coordinatesContent;

    public CoordinatesWindow(RobotModel robotModel){
        super(robotModel, "Координаты робота", true, true, true, true);
        m_coordinatesContent = new TextField("");
        m_coordinatesContent.setSize(200, 500);

        robotModel.addObserver(this);
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
