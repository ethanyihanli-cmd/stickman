package com.macondo.stickman.controller;

import com.macondo.stickman.input.InputHandler;
import com.macondo.stickman.model.Player;
import com.macondo.stickman.view.GameCanvas;
import javafx.animation.AnimationTimer;

public class GameLoop {
    private GameCanvas canvas;
    private Player player;
    private InputHandler input;
    private AnimationTimer timer;

    public GameLoop(GameCanvas canvas, InputHandler input) {
        this.canvas = canvas;
        this.input = input;
        this.player = new Player(100, 600, input);
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
                canvas.render(player);
            }
        };
    }

    private void update(double dt) {
        player.update(dt);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}
