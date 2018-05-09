package gui;

import log.Logger;

import java.awt.*;
import java.util.*;
import java.util.stream.Collectors;

public class RobotModel extends Observable {

    private ArrayList<Obstacle> obstacles;
    private Stack<Point> currentRoute;

    private final Timer m_timer = initTimer();

    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private int widthBound = 500;
    private int heightBound = 500;

    public double getX() {
        return m_robotPositionX;
    }

    public double getY() {
        return m_robotPositionY;
    }

    public double getDirection() {
        return m_robotDirection;
    }

    public int getTargetX() {
        return m_targetPositionX;
    }

    public int getTargetY() {
        return m_targetPositionY;
    }

    public ArrayList<Obstacle> getObstacles() {
        return obstacles;
    }

    public void setX(int m_robotPositionX) {
        this.m_robotPositionX = m_robotPositionX;
    }

    public void setY(int m_robotPositionY) {
        this.m_robotPositionY = m_robotPositionY;
    }

    public void setTargetX(int m_targetPositionX) {
        this.m_targetPositionX = m_targetPositionX;
    }

    public void setTargetY(int m_targetPositionY) {
        this.m_targetPositionY = m_targetPositionY;
    }

    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    public RobotModel(){
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                notifyObservers(RobotModel.this);
            }
        }, 0, 50);
        m_timer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                tick();
            }
        }, 0, 10);
        obstacles = new ArrayList<>();
        currentRoute = new Stack<>();
    }

    protected void setTargetPosition(Point p)
    {
        if (!collidedWithAnObstacle(p)) {
            m_targetPositionX = p.x;
            m_targetPositionY = p.y;
            recalculateRoute();
        }
        else Logger.debug("Цель недостижима!");
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    protected void tick()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }

        if (!currentRoute.isEmpty()) {
            moveRobot(currentRoute.pop());
            setChanged();
        }
    }

    public void recalculateRoute(){
        Point robot = new Point((int) m_robotPositionX, (int) m_robotPositionY);
        Map<Point, Point> father = findRoute(
                robot,
                new Point(m_targetPositionX, m_targetPositionY)
        );
        currentRoute = restorationRoute(father, robot);
    }

    public Map<Point, Point> findRoute(Point robot, Point target){

        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> father = new HashMap<>();

        queue.add(robot);
        visited.add(robot);
        while (!queue.isEmpty()){
            Point point = queue.peek();
            if(point.equals(target)) break;
            queue.poll();
            Set<Point> incidentPoints = incidentPoints(point);
            for (Point w: incidentPoints){
                if (!visited.contains(w) && !collidedWithAnObstacle(w)) {
                    visited.add(w);
                    queue.add(w);
                    father.put(w, point);
                }
            }
        }
        return father;
    }

    public Set<Point> incidentPoints(Point p){
        Integer X[] = {0,1,1,1,0,-1,-1,-1};
        Integer Y[] = {1,1,0,-1,-1,-1,0,1};
        Set<Point> incidentPoints = new HashSet<>();

        for(int i = 0; i < 8; i++)
            if (getX() + X[i] >= 0 && getX() + X[i] <= widthBound && getY() + Y[i] >= 0 && getY() + Y[i] <= heightBound)
                incidentPoints.add(new Point((int)(p.getX() + X[i]), (int)(p.getY() + Y[i])));

        return incidentPoints;
    }

    public boolean collidedWithAnObstacle(Point p){
        boolean collision = false;
        Rectangle robot = new Rectangle((int)p.getX() - 15, (int)p.getY() - 10, 30, 20);
        for(Obstacle obs: obstacles) {
            if (robot.intersects(obs))
                collision = true;
        }
        return collision;
    }

    public Stack<Point> restorationRoute(Map<Point, Point> father, Point robot){
        Stack<Point> result = new Stack<>();

        Point currentPoint = new Point(m_targetPositionX, m_targetPositionY);
        while(!currentPoint.equals(robot)){
            result.push(currentPoint);
            currentPoint = father.get(currentPoint);
        }
        return result;
    }

    private void moveRobot(Point p)
    {
        m_robotPositionX = p.x;
        m_robotPositionY = p.y;
    }

    public void addObstacle(Point p) {
        Obstacle obs = new Obstacle(p.getLocation());
        boolean enoughSpace = obstacles.stream().allMatch(o -> o.distance(obs) > 45);
        if (!obs.contains(new Point(m_targetPositionX, m_targetPositionY)) && enoughSpace) {
            obstacles.add(new Obstacle(p.getLocation()));
            recalculateRoute();
        }
        else if (!enoughSpace)
            Logger.debug("Препятствия слишком близки друг к другу!");
        else
            Logger.debug("Нельзя ставить препятствия на цель!");
    }

    public void removeObstacle(Point p) {
        obstacles = (ArrayList<Obstacle>) obstacles.stream().filter(e -> !e.contains(p)).collect(Collectors.toList());
    }
}
