package gui;

import javax.swing.*;

public class AssociatedFrame extends JInternalFrame{

    private GameField gameField;
    private String frameType;

    public AssociatedFrame (GameField gameField, String id, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable){
        super(getTitleById(id), resizable, closable, maximizable, iconifiable);
        this.frameType = id;
        this.gameField = gameField;
    }

    public GameField getGameField() {
        return gameField;
    }

    public String getFrameType() {
        return frameType;
    }

    public static String getTitleById(String id) {
        switch (id) {
            case "GAME": return "Игровое поле";
            case "LOG": return "Протокол работы";
            case "COORDINATE": return "Координаты робота";
        }
        return id;
    }
}
