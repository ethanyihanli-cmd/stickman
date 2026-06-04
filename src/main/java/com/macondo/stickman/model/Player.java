package com.macondo.stickman.model;

import com.macondo.stickman.input.InputHandler;

public class Player extends GameObject {
    private static final double SPEED = 300;
    private static final double JUMP_POWER = -550;
    private static final double GRAVITY = 1800;

    private boolean onGround = true;
    private InputHandler input;

    public Player(double x, double y, InputHandler input) {
        super(x, y, 40, 60);
        this.input = input;
    }

    @Override
    public void update(double dt) {
        if (input.isMovingLeft()) {
            vx = -SPEED;
        } else if (input.isMovingRight()) {
            vx = SPEED;
        } else {
            vx = 0;
        }

        if (input.isJumping() && onGround) {
            vy = JUMP_POWER;
            onGround = false;
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

        if (x < 0) {
            x = 0;
        }
        if (x + width > 1024) {
            x = 1024 - width;
        }
    }

    public double getVisualX() {
        return x;
    }

    public double getVisualY() {
        return y;
    }
}
