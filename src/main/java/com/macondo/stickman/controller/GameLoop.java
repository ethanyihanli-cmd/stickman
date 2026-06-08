package com.macondo.stickman.controller;

import com.macondo.stickman.input.InputHandler;
import com.macondo.stickman.model.CollisionDetector;
import com.macondo.stickman.model.Enemy;
import com.macondo.stickman.model.Player;
import com.macondo.stickman.view.GameCanvas;
import javafx.animation.AnimationTimer;
import java.util.ArrayList;
import java.util.List;

public class GameLoop {
    private GameCanvas canvas;
    private Player player;
    private InputHandler input;
    private List<Enemy> enemies;
    private AnimationTimer timer;

    public GameLoop(GameCanvas canvas, InputHandler input) {
        this.canvas = canvas;
        this.input = input;
        this.player = new Player(100, 600, input);
        this.enemies = new ArrayList<>();
        this.enemies.add(new Enemy(300, 590, 200, 700));
        initLoop();
    }

    private void initLoop() {
        timer = new AnimationTimer() {
            private long lastUpdate = 0;
            @Override
            public void handle(long now) {
                if (lastUpdate == 0) {
                    lastUpdate = now;
                    return;
                }
                double deltaTime = (now - lastUpdate) / 1e9;
                lastUpdate = now;

                update(deltaTime);
                canvas.render(player, enemies);
            }
        };
    }

    private void update(double dt) {
        player.update(dt);

        for (Enemy e : enemies) {
            e.update(dt);
            CollisionDetector.handlePlayerEnemyCollision(player, e);

            if (CollisionDetector.checkAttackHit(player, e)) {
                e.takeDamage(10);
            }
        }

        enemies.removeIf(e -> !e.isAlive());
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}
