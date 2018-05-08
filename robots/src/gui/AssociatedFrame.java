package gui;

import javax.swing.*;

public class AssociatedFrame extends JInternalFrame{

    private RobotModel robotModel;

    public AssociatedFrame (RobotModel robotModel, String title, boolean resizable, boolean closable, boolean maximizable, boolean iconifiable){
        super(title, resizable, closable, maximizable, iconifiable);
        this.robotModel = robotModel;
    }

    public RobotModel getRobotModel() {
        return robotModel;
    }
}
