package com.macondo.stickman.model;

public class Projectile extends GameObject {
    private double life;
    private int damage;

    public Projectile(double x, double y, double vx, double vy) {
        super(x, y, 20, 20);
        this.vx = vx;
        this.vy = vy;
        this.life = 2.0;
        this.damage = 15;
    }

    @Override
    public void update(double dt) {
        super.update(dt);
        life -= dt;
    }

    public boolean isExpired() {
        return life <= 0;
    }

    public int getDamage() {
        return damage;
    }
}
