package com.macondo.stickman.model;

public class Enemy extends GameObject {
    private double patrolLeft;
    private double patrolRight;
    private double speed;
    private int direction;
    private int health;
    private boolean hitFlash = false;
    private double flashTimer = 0;

    public Enemy(double x, double y, double patrolLeft, double patrolRight) {
        super(x, y, 40, 60);
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        this.speed = 150;
        this.direction = 1;
        this.health = 30;
    }

    @Override
    public void update(double dt) {
        vx = direction * speed;
        super.update(dt);

        if (x <= patrolLeft) {
            x = patrolLeft;
            direction = 1;
        } else if (x + width >= patrolRight) {
            x = patrolRight - width;
            direction = -1;
        }

        if (y + height >= 650) {
            y = 650 - height;
            vy = 0;
        } else {
            vy += 1800 * dt;
        }

        if (flashTimer > 0) {
            flashTimer -= dt;
            if (flashTimer <= 0) {
                hitFlash = false;
            }
        }
    }

    public void takeDamage(int damage) {
        if (health <= 0) return;
        health -= damage;
        hitFlash = true;
        flashTimer = 0.2;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public boolean isHitFlash() {
        return hitFlash;
    }

    public int getHealth() {
        return health;
    }
}

