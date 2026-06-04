package com.macondo.stickman.view;

import com.macondo.stickman.model.Player;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class GameCanvas extends Canvas {
    private GraphicsContext gc;

    public GameCanvas(double width, double height) {
        super(width, height);
        gc = this.getGraphicsContext2D();
    }

    public void render(Player player) {
        gc.setFill(Color.rgb(30, 30, 45));
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setFill(Color.rgb(200, 120, 80));
        gc.fillRect(player.getVisualX(), player.getVisualY(),
                player.getWidth(), player.getHeight());
    }
}
