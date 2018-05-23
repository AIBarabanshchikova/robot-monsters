package gui;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends AssociatedFrame implements Observer {
    private TextArea m_coordinatesContent;
    private GameField gameField;

    public CoordinatesWindow(GameField gameField){
        super(gameField, "COORDINATE", true, true, true, true);
        m_coordinatesContent = new TextArea("");
        m_coordinatesContent.setSize(200, 500);
        this.gameField = gameField;

        gameField.addObserver(this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add( m_coordinatesContent,BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void update(Observable o, Object arg) {
        StringBuilder str = new StringBuilder();
        //gameField.getModels().stream().map(m -> String.format("%d x %d\r\n", m.getPosition().x, m.getPosition().y)).forEach(m -> str.append(m));
        for(RobotModel model: gameField.getModels()){
            str.append(String.format("%d x %d\r\n", model.getPosition().x, model.getPosition().y));
        }
        m_coordinatesContent.setText(str.toString());
    }
}
