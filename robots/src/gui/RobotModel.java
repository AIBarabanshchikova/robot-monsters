package gui;

import java.awt.*;
import java.util.*;

public class RobotModel extends Observable {

    private ArrayList<Obstacle> obstacles;

    private final Timer m_timer = initTimer();

    private volatile double m_robotPositionX = 100;
    private volatile double m_robotPositionY = 100;
    private volatile double m_robotDirection = 0;

    private volatile int m_targetPositionX = 150;
    private volatile int m_targetPositionY = 100;

    private static final double maxVelocity = 0.1;
    private static final double maxAngularVelocity = 0.001;

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
    }

    protected void setTargetPosition(Point p)
    {
        m_targetPositionX = p.x;
        m_targetPositionY = p.y;
    }

    private static double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private static double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;

        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    protected void tick()
    {
        double distance = distance(m_targetPositionX, m_targetPositionY,
                m_robotPositionX, m_robotPositionY);
        if (distance < 0.5)
        {
            return;
        }
        double velocity = maxVelocity;
        //double angleToTarget = angleTo(m_robotPositionX, m_robotPositionY, m_targetPositionX, m_targetPositionY);
        /*double angularVelocity = 0;
        if (angleToTarget > m_robotDirection)
        {
            angularVelocity = maxAngularVelocity;
        }
        if (angleToTarget < m_robotDirection)
        {
            angularVelocity = -maxAngularVelocity;
        }*/

        //moveRobot(velocity, angularVelocity, 10);
        setChanged();
    }

    public Map<Point, Point> findRoute(){

        Set<Point> visited = new HashSet<>();
        Queue<Point> queue = new LinkedList<>();
        Map<Point, Point> father = new HashMap<>();

        Point robot = new Point((int) m_robotPositionX, (int) m_robotPositionY);
        Point target = new Point(m_targetPositionX, m_targetPositionY);
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
            //if (getX() + X[i] >= 0 && getX() + X[i] <= 7 && getY() + Y[i] >= 0 && getY() + Y[i] <= 7)
                incidentPoints.add(new Point((int)(p.getX() + X[i]), (int)(p.getY() + Y[i])));

        return incidentPoints;
    }

    public boolean collidedWithAnObstacle(Point p){
        boolean underAttack = false;
        for(Obstacle obs: obstacles) {
            //левая вертикаль
            double discriminant1 = discriminant(p.getY(), p.getX(), obs.getX(), 30, 10);
            double lv_y1 = equationRoot(p.getY(), 10, Math.sqrt(discriminant1));
            double lv_y2 = equationRoot(p.getY(), 10, -Math.sqrt(discriminant1));
            //правая вертикаль
            double discriminant2 = discriminant(p.getY(), p.getX(), obs.getX() + obs.getWidth(), 30, 10);
            double rv_y1 = equationRoot(p.getY(), 10, Math.sqrt(discriminant2));
            double rv_y2 = equationRoot(p.getY(), 10, -Math.sqrt(discriminant2));
            //верхняя горизонталь
            double discriminant3 = discriminant(p.getX(), p.getY(), obs.getY(), 10, 30);
            double tg_y1 = equationRoot(p.getX(), 30, Math.sqrt(discriminant3));
            double tg_y2 = equationRoot(p.getX(), 30, -Math.sqrt(discriminant3));
            //нижняя горизонталь
            double discriminant4 = discriminant(p.getX(), p.getY(), obs.getY() + obs.getHeight(), 10, 30);
            double lg_y1 = equationRoot(p.getX(), 30, Math.sqrt(discriminant4));
            double lg_y2 = equationRoot(p.getX(), 30, -Math.sqrt(discriminant4));

            if ((discriminant1 >= 0 && (obs.getY() <= lv_y1) && (lv_y1 <= obs.getY() + obs.getHeight()) && (obs.getY() <= lv_y2) && (lv_y2 <= obs.getY() + obs.getHeight())) &&
                    (discriminant2 >= 0 && (obs.getY() <= rv_y1) && (rv_y1 <= obs.getY() + obs.getHeight()) && (obs.getY() <= rv_y2) && (rv_y2 <= obs.getY() + obs.getHeight())) &&
                    (discriminant3 >= 0 && (obs.getX() <= tg_y1) && (tg_y1 <= obs.getX() + obs.getWidth()) && (obs.getX() <= tg_y2) && (tg_y2 <= obs.getX() + obs.getWidth())) &&
                    (discriminant4 >= 0 && (obs.getX() <= lg_y1) && (lg_y1 <= obs.getX() + obs.getWidth()) && (obs.getX() <= lg_y2) && (lg_y2 <= obs.getX() + obs.getWidth())));
                underAttack = true;
        }
        return underAttack;
    }

    public  double discriminant(double y0, double x0, double x1, int a, int b){
        double discriminant =
                Math.pow(-(2*y0)/Math.pow(b,2),2) - 4*1/Math.pow(b, 2)*(Math.pow(y0,2)/Math.pow(b,2) - Math.pow(x1-x0,2)/Math.pow(a,2) + 1);
        return  discriminant;
    }
    public double equationRoot(double y0, int b, double discr){
        double y = (2*y0/Math.pow(b, 2) + discr) / (2*(1/Math.pow(b, 2)));
        return y;
    }

    private void moveRobot(double velocity, double angularVelocity, double duration)
    {
        velocity = applyLimits(velocity, 0, maxVelocity);
        angularVelocity = applyLimits(angularVelocity, -maxAngularVelocity, maxAngularVelocity);
        double newX = m_robotPositionX + velocity / angularVelocity *
                (Math.sin(m_robotDirection  + angularVelocity * duration) -
                        Math.sin(m_robotDirection));
        if (!Double.isFinite(newX))
        {
            newX = m_robotPositionX + velocity * duration * Math.cos(m_robotDirection);
        }
        double newY = m_robotPositionY - velocity / angularVelocity *
                (Math.cos(m_robotDirection  + angularVelocity * duration) -
                        Math.cos(m_robotDirection));
        if (!Double.isFinite(newY))
        {
            newY = m_robotPositionY + velocity * duration * Math.sin(m_robotDirection);
        }
        m_robotPositionX = newX;
        m_robotPositionY = newY;
        double newDirection = asNormalizedRadians(m_robotDirection + angularVelocity * duration);
        m_robotDirection = newDirection;
    }

    private static double applyLimits(double value, double min, double max)
    {
        if (value < min)
            return min;
        if (value > max)
            return max;
        return value;
    }

    private static double asNormalizedRadians(double angle)
    {
        while (angle < 0)
        {
            angle += 2*Math.PI;
        }
        while (angle >= 2*Math.PI)
        {
            angle -= 2*Math.PI;
        }
        return angle;
    }

    public void addObstacle(Point p) {
       obstacles.add(Obstacle.newObstacle(p.getLocation()));
    }
}
