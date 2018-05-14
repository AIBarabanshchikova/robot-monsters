package gui;

import log.Logger;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class GameField extends Observable {

    private final ArrayList<RobotModel> robotModels;
    private ArrayList<Obstacle> obstacles;
    private volatile Point target;

    public GameField() {
        obstacles = new ArrayList<>();
        robotModels = new ArrayList<>();
        target = new Point(150, 100);
        recalculateAllRoutes();
        Timer timer = new Timer("events generator", true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (RobotModel model: robotModels)
                    model.nextPos();
                setChanged();
                notifyObservers();
            }
        }, 0, 10);
    }

    //смотрим столкновение с препятствиями
    public static boolean collidedWithAnObstacle(Collection<Obstacle> obstacles, Point p) {
        boolean collision = false;
        Rectangle robot = new Rectangle((int) p.getX() - 15, (int) p.getY() - 10, 30, 20);
        for (Obstacle obs : obstacles) {
            if (robot.intersects(obs))
                collision = true;
        }
        return collision;
    }

    public Point getTarget() { return target; }

    public void setTarget(Point target) {
        if (!collidedWithAnObstacle(obstacles, target)) {
            this.target = target;
            recalculateAllRoutes();
        } else Logger.debug("Цель недостижима!");
    }

    public ArrayList<RobotModel> getModels() { return robotModels; }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    private void recalculateAllRoutes() {
        //for (RobotModel model : robotModels)
          //  model.recalculateRoute(target, obstacles);
        robotModels.parallelStream().forEach(m -> m.recalculateRoute(target, obstacles));
    }

    //добавляем препятствие
    public void addObstacle(Point p) {
        Obstacle obs = new Obstacle(p.getLocation());
        boolean enoughSpace = obstacles.stream().allMatch(o -> o.distance(obs) > 45);
        //если препятствие не содержит в себе цели и достаточно места для прохода робота
        if (!obs.contains(target) && enoughSpace) {
            obstacles.add(new Obstacle(p.getLocation()));
            recalculateAllRoutes();
        } else if (!enoughSpace)
            Logger.debug("Препятствия слишком близки друг к другу!");
        else
            Logger.debug("Нельзя ставить препятствие на цель!");
    }

    //удаляем препятствие
    public void removeObstacle(Point p) {
        obstacles = (ArrayList<Obstacle>) obstacles.stream().filter(e -> !e.contains(p)).collect(Collectors.toList());
    }

    public void addRobot(RobotModel robotModel){
        robotModels.add(robotModel);
    }
}
