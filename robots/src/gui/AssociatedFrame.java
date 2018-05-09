package gui;

import javax.swing.*;

public class AssociatedFrame extends JInternalFrame{

    private RobotModel robotModel;
    private String frameType;

    public AssociatedFrame (RobotModel robotModel, String id, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable){
        super(getTitleById(id), resizable, closable, maximizable, iconifiable);
        this.frameType = id;
        this.robotModel = robotModel;
    }

    public RobotModel getRobotModel() {
        return robotModel;
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
