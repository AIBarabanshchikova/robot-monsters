package gui;

import javax.swing.*;
import java.awt.*;

public class CoordinatesWindow extends JInternalFrame {
    private TextArea m_coordinatesContent;

    public CoordinatesWindow(){
        super("Координаты робота", true, true, true, true);
        m_coordinatesContent = new TextArea("");
        m_coordinatesContent.setSize(200, 500);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add( m_coordinatesContent,BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
