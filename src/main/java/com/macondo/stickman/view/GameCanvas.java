package com.macondo.stickman.view;

import com.macondo.stickman.model.Enemy;
import com.macondo.stickman.model.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.List;

public class GameCanvas extends Canvas {
    private GraphicsContext gc;

    public GameCanvas(double width, double height) {
        super(width, height);
        gc = this.getGraphicsContext2D();
    }

    public void render(Player player, List<Enemy> enemies) {
        gc.setFill(Color.rgb(30, 30, 45));
        gc.fillRect(0, 0, getWidth(), getHeight());

        for (Enemy e : enemies) {
            if (e.isHitFlash()) {
                gc.setFill(Color.rgb(255, 100, 100));
            } else {
                gc.setFill(Color.RED);
            }
            gc.fillRect(e.getX(), e.getY(), e.getWidth(), e.getHeight());

            gc.setFill(Color.WHITE);
            gc.fillRect(e.getX(), e.getY() - 10, e.getWidth(), 5);
            gc.setFill(Color.GREEN);
            double healthPercent = e.getHealth() / 30.0;
            gc.fillRect(e.getX(), e.getY() - 10, e.getWidth() * healthPercent, 5);
        }

        if (player.isAttacking()) {
            gc.setFill(Color.ORANGE);
            gc.fillOval(player.getAttackX(), player.getAttackY() - 15, 25, 30);
        }

        gc.setFill(Color.rgb(200, 120, 80));
        gc.fillRect(player.getVisualX(), player.getVisualY(),
                player.getWidth(), player.getHeight());
    }
}

