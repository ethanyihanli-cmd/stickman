package com.macondo.stickman.model;

public abstract class GameObject {
    protected double x, y;
    protected double vx, vy;
    protected double width, height;

    public GameObject(double x, double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.width = w;
        this.height = h;
        this.vx = 0;
        this.vy = 0;
    }

    public void update(double dt) {
        x += vx * dt;
        y += vy * dt;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getWidth() { return width; }
    public double getHeight() { return height; }
    public void setX(double x) { this.x = x; }
    public void setY(double y) { this.y = y; }
    public void setVx(double vx) { this.vx = vx; }
    public void setVy(double vy) { this.vy = vy; }
    public double getVx() { return vx; }
    public double getVy() { return vy; }
}
