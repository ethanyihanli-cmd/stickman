package com.macondo.stickman.input;

import java.util.HashSet;
import java.util.Set;
import javafx.scene.input.KeyCode;

public class InputHandler {
    private Set<KeyCode> activeKeys = new HashSet<>();

    public void keyPressed(KeyCode code) {
        activeKeys.add(code);
    }

    public void keyReleased(KeyCode code) {
        activeKeys.remove(code);
    }

    public boolean isKeyDown(KeyCode code) {
        return activeKeys.contains(code);
    }

    public boolean isMovingLeft() {
        return isKeyDown(KeyCode.A) || isKeyDown(KeyCode.LEFT);
    }

    public boolean isMovingRight() {
        return isKeyDown(KeyCode.D) || isKeyDown(KeyCode.RIGHT);
    }

    public boolean isJumping() {
        return isKeyDown(KeyCode.K) || isKeyDown(KeyCode.W) || isKeyDown(KeyCode.UP) || isKeyDown(KeyCode.SPACE);
    }

    public boolean isAttacking() {
        return isKeyDown(KeyCode.J) || isKeyDown(KeyCode.S);
    }
}
