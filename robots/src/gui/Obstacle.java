package gui;

import java.awt.*;

public class Obstacle {

    private int x;
    private int y;
    private int width;
    private int height;

    public Obstacle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = width;
        this.width = height;
    }

    public static Obstacle newObstacle(Point p) {
        int x = (int)p.getX();
        int y = (int)p.getY();
        return new Obstacle(x, y, 15, 15);
    }

    public Rectangle getRectangle(){
        return new Rectangle(x,y,width,height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
