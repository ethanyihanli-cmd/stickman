package com.macondo.stickman.model;

public class Enemy extends GameObject {
    private double patrolLeft;
    private double patrolRight;
    private double patrolSpeed;
    private double chaseSpeed;
    private int direction;
    private int health;
    private boolean hitFlash;
    private double flashTimer;
    private boolean onGround;
    private double jumpCooldown;

    private enum State { PATROL, CHASE, ATTACK }
    private State state;
    private double attackCooldown;
    private static final double ATTACK_COOLDOWN_MAX = 0.75;
    private double chaseRange = 420;
    private double attackRange = 58;
    private static final double JUMP_POWER = -820;
    private static final double GRAVITY = 1800;

    public Enemy(double x, double y, double patrolLeft, double patrolRight) {
        super(x, y, 40, 60);
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
        this.patrolSpeed = 120;
        this.chaseSpeed = 205;
        this.direction = 1;
        this.health = 3;
        this.hitFlash = false;
        this.flashTimer = 0;
        this.onGround = false;
        this.jumpCooldown = 0;
        this.state = State.PATROL;
        this.attackCooldown = 0;
    }

    public void update(double dt, java.util.List<Platform> platforms) {
        if (attackCooldown > 0) {
            attackCooldown -= dt;
        }
        if (jumpCooldown > 0) {
            jumpCooldown -= dt;
        }

        if (flashTimer > 0) {
            flashTimer -= dt;
            if (flashTimer <= 0) {
                hitFlash = false;
            }
        }

        double previousBottom = y + height;

        vy += GRAVITY * dt;
        super.update(dt);

        boolean landed = false;
        for (Platform plat : platforms) {
            if (previousBottom <= plat.getY() && y + height >= plat.getY() &&
                    x + width > plat.getX() && x < plat.getX() + plat.getWidth()) {
                y = plat.getY() - height;
                vy = 0;
                landed = true;
            }
        }
        onGround = landed;

        if (y + height >= 770) {
            y = 770 - height;
            vy = 0;
            onGround = true;
        }
    }

    public void update(double dt) { update(dt, new java.util.ArrayList<>()); }

    public void updateAI(Player player, double dt, java.util.List<Platform> platforms) {
        double dx = player.getCenterX() - (x + width / 2);
        double dy = Math.abs(player.getCenterY() - (y + height / 2));
        double distance = Math.abs(dx);

        if (distance < attackRange && dy < 65 && attackCooldown <= 0) {
            state = State.ATTACK;
            attackCooldown = ATTACK_COOLDOWN_MAX;
        } else if (distance < chaseRange && dy < 240) {
            state = State.CHASE;
        } else {
            state = State.PATROL;
        }

        switch (state) {
            case PATROL:
                vx = direction * patrolSpeed;
                if (x <= patrolLeft) {
                    x = patrolLeft;
                    direction = 1;
                } else if (x + width >= patrolRight) {
                    x = patrolRight - width;
                    direction = -1;
                }
                break;
            case CHASE:
                direction = dx > 0 ? 1 : -1;
                vx = direction * chaseSpeed;
                tryJumpTowardPlayer(player, dx, platforms);
                break;
            case ATTACK:
                vx = 0;
                break;
        }

        update(dt, platforms);
    }

    private void tryJumpTowardPlayer(Player player, double dx, java.util.List<Platform> platforms) {
        if (!onGround || jumpCooldown > 0) {
            return;
        }

        boolean playerIsHigher = player.getY() + player.getHeight() < y + height - 40;
        boolean playerIsNearby = Math.abs(dx) < 260;
        boolean upperPlatformBetweenUs = false;

        for (Platform platform : platforms) {
            boolean platformAboveEnemy = platform.getY() < y + height - 30;
            boolean canLandOnPlatform = x + width > platform.getX() - 80 &&
                    x < platform.getX() + platform.getWidth() + 80;
            boolean playerNearPlatform = player.getCenterX() > platform.getX() - 80 &&
                    player.getCenterX() < platform.getX() + platform.getWidth() + 80;
            if (platformAboveEnemy && canLandOnPlatform && playerNearPlatform) {
                upperPlatformBetweenUs = true;
                break;
            }
        }

        if ((playerIsHigher && playerIsNearby) || upperPlatformBetweenUs) {
            vy = JUMP_POWER;
            jumpCooldown = 0.8;
        }
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
