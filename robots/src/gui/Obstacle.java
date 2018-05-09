package gui;

import java.awt.*;

public class Obstacle extends Rectangle {

    public Obstacle(Point p) {
        super(p.x, p.y, 15, 15);
    }

    public double distance(Obstacle obs) {
        return getCenter().distance(obs.getCenter());
    }

    public Point getCenter(){
        return new Point(this.x - this.width / 2, this.y - this.height / 2);
    }
}
