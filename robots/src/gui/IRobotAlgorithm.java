package gui;

import java.awt.*;
import java.util.Collection;
import java.util.Stack;

public interface IRobotAlgorithm {
    public Stack<Point> calculateRoute(Point start, Point end, Collection<Obstacle> obstacles);

    String getName();
}
