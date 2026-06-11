package com.macondo.stickman.model;

public class Particle {
    private double x, y;
    private double vx, vy;
    private double life;
    private double size;

    public Particle(double x, double y, double vx, double vy, double size, double life) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.size = size;
        this.life = life;
    }

    public void update(double dt) {
        x += vx * dt;
        y += vy * dt;
        life -= dt;
    }

    public boolean isAlive() {
        return life > 0;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getSize() { return size; }
    public double getLife() { return life; }
}
