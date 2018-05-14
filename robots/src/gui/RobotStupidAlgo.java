package gui;

import java.awt.*;
import java.util.*;

public class RobotStupidAlgo implements IRobotAlgorithm {
    @Override
    public Stack<Point> calculateRoute(Point start, Point end, Collection<Obstacle> obstacles) {
        Stack<Point> stack = new Stack<>();
        int x = end.x;
        int y = end.y;
        stack.push(new Point(x, y));
        while (y != start.y){
            if(end.y > start.y) {
                y--;
                stack.push(new Point(x, y));
            } else {
                y++;
                stack.push(new Point(x, y));
            }
        }

        while (x != start.x){
            if(end.x > start.x) {
                x--;
                stack.push(new Point(x, y));
            } else {
                x++;
                stack.push(new Point(x, y));
            }
        }
        return stack;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }
}
