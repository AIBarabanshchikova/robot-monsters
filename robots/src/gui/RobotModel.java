package gui;

import java.awt.*;
import java.util.*;

public class RobotModel {

    private IRobotAlgorithm algo;
    private Stack<Point> currentRoute;
    private volatile Point position;

    public RobotModel(IRobotAlgorithm algo){
        this.algo = algo;
        position = new Point(100, 100);
    }

    public Point getPosition() {
        return position;
    }

    public void nextPos() {
        if (currentRoute != null && !currentRoute.isEmpty()) {
            position = currentRoute.pop();
        }
    }

    public void recalculateRoute(Point target, Collection<Obstacle> obstacles){
        currentRoute = algo.calculateRoute(position, target, obstacles);
    }
}
