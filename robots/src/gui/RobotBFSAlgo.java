package gui;

import java.awt.*;
import java.util.*;

public class RobotBFSAlgo implements IRobotAlgorithm {
    @Override
    public Stack<Point> calculateRoute(Point start, Point end, Collection<Obstacle> obstacles) {
        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> father = new HashMap<>();

        queue.add(start);
        visited.add(start);
        while (!queue.isEmpty()){
            Point point = queue.peek();
            if(point.equals(end)) break;
            queue.poll();
            Set<Point> incidentPoints = incidentPoints(point);
            for (Point w: incidentPoints){
                if (!visited.contains(w) && !GameField.collidedWithAnObstacle(obstacles, w)) {
                    visited.add(w);
                    queue.add(w);
                    father.put(w, point);
                }
            }
        }

        return  restoreRoute(father, start, end);
    }

    public Set<Point> incidentPoints(Point p){
        Integer X[] = {0,1,1,1,0,-1,-1,-1};
        Integer Y[] = {1,1,0,-1,-1,-1,0,1};
        Set<Point> incidentPoints = new HashSet<>();

        for(int i = 0; i < 8; i++)
            incidentPoints.add(new Point((int)(p.getX() + X[i]), (int)(p.getY() + Y[i])));

        return incidentPoints;
    }

    //восстанавливаем путь
    public Stack<Point> restoreRoute(Map<Point, Point> father, Point robot, Point target){
        Stack<Point> result = new Stack<>();

        Point currentPoint = new Point(target.x, target.y);
        while(!currentPoint.equals(robot)){
            result.push(currentPoint);
            currentPoint = father.get(currentPoint);
        }
        return result;
    }
}
