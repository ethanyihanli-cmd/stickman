package com.macondo.stickman.model;

import com.macondo.stickman.input.InputHandler;

public class Player extends GameObject {
    private static final double SPEED = 300;
    private static final double JUMP_POWER = -550;
    private static final double GRAVITY = 1800;

    private boolean onGround;
    private InputHandler input;

    private boolean isAttacking;
    private double attackTimer;
    private static final double ATTACK_DURATION = 0.2;
    private static final double ATTACK_COOLDOWN = 0.5;

    private double specialCooldown;
    private static final double SPECIAL_COOLDOWN_MAX = 1.5;

    private int health;
    private boolean invincible;
    private double invincibleTimer;
    private static final double INVINCIBLE_DURATION = 1.0;

    private boolean facingRight;

    public Player(double x, double y, InputHandler input) {
        super(x, y, 40, 60);
        this.input = input;
        this.health = 100;
        this.invincible = false;
        this.isAttacking = false;
        this.attackTimer = 0;
        this.specialCooldown = 0;
        this.onGround = true;
        this.facingRight = true;
    }

    @Override
    public void update(double dt) {
        if (input.isMovingLeft()) {
            vx = -SPEED;
            facingRight = false;
        } else if (input.isMovingRight()) {
            vx = SPEED;
            facingRight = true;
        } else {
            vx = 0;
        }

        if (input.isJumping() && onGround) {
            vy = JUMP_POWER;
            onGround = false;
        }

        if (input.isAttacking() && attackTimer <= 0) {
            isAttacking = true;
            attackTimer = ATTACK_DURATION + ATTACK_COOLDOWN;
        }

        if (attackTimer > 0) {
            attackTimer -= dt;
            if (attackTimer <= ATTACK_COOLDOWN) {
                isAttacking = false;
            }
        }

        if (specialCooldown > 0) {
            specialCooldown -= dt;
        }

        if (invincible) {
            invincibleTimer -= dt;
            if (invincibleTimer <= 0) {
                invincible = false;
            }
        }

        vy += GRAVITY * dt;
        super.update(dt);

        if (y + height >= 650) {
            y = 650 - height;
            vy = 0;
            onGround = true;
        } else {
            onGround = false;
        }

        if (x < 0) x = 0;
        if (x + width > 1024) x = 1024 - width;
    }

    public void takeDamage(int amount) {
        if (invincible) return;
        health -= amount;
        invincible = true;
        invincibleTimer = INVINCIBLE_DURATION;
        if (health < 0) health = 0;
    }

    public void knockback(double fromX) {
        double direction = (x < fromX) ? -1 : 1;
        vx = direction * 300;
        vy = -200;
    }

    public boolean isAttacking() { return isAttacking; }
    public boolean canUseSpecial() { return specialCooldown <= 0; }
    public void useSpecial() { specialCooldown = SPECIAL_COOLDOWN_MAX; }

    public double getAttackX() { return x + width + 20; }
    public double getAttackY() { return y + height / 2; }
    public double getVisualX() { return x; }
    public double getVisualY() { return y; }
    public double getCenterX() { return x + width / 2; }
    public double getCenterY() { return y + height / 2; }
    public int getHealth() { return health; }
    public boolean isInvincible() { return invincible; }
    public boolean isAlive() { return health > 0; }
    public boolean isFacingRight() { return facingRight; }
}
