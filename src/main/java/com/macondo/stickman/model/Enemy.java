package com.macondo.stickman.model;

public class Enemy extends GameObject {
    private double patrolLeft;
    private double patrolRight;
    private double speed;
    private int direction;
    private int health;
    private boolean hitFlash;
    private double flashTimer;

    private enum State { PATROL, CHASE, ATTACK }
    private State state;
    private double attackCooldown;
    private static final double ATTACK_COOLDOWN_MAX = 1.0;
    private double chaseRange = 200;
    private double attackRange = 50;

    public Enemy(double x, double y, double patrolLeft, double patrolRight) {
        super(x, y, 40, 60);
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        this.speed = 150;
        this.direction = 1;
        this.health = 30;
        this.hitFlash = false;
        this.flashTimer = 0;
        this.state = State.PATROL;
        this.attackCooldown = 0;
    }

    @Override
    public void update(double dt) {
        if (attackCooldown > 0) {
            attackCooldown -= dt;
        }

        if (flashTimer > 0) {
            flashTimer -= dt;
            if (flashTimer <= 0) {
                hitFlash = false;
            }
        }

        vy += 1800 * dt;
        super.update(dt);

        if (y + height >= 650) {
            y = 650 - height;
            vy = 0;
        }
    }

    public void updateAI(Player player, double dt) {
        double dx = player.getX() - x;
        double distance = Math.abs(dx);

        if (distance < attackRange && attackCooldown <= 0) {
            state = State.ATTACK;
            attackCooldown = ATTACK_COOLDOWN_MAX;
        } else if (distance < chaseRange) {
            state = State.CHASE;
        } else {
            state = State.PATROL;
        }

        switch (state) {
            case PATROL:
                vx = direction * speed;
                if (x <= patrolLeft) {
                    x = patrolLeft;
                    direction = 1;
                } else if (x + width >= patrolRight) {
                    x = patrolRight - width;
                    direction = -1;
                }
                break;
            case CHASE:
                if (dx > 0) {
                    vx = speed;
                } else {
                    vx = -speed;
                }
                break;
            case ATTACK:
                vx = 0;
                break;
        }

        update(dt);
    }

    public boolean isAttacking() {
        return state == State.ATTACK && attackCooldown > ATTACK_COOLDOWN_MAX - 0.2;
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
