package com.macondo.stickman.view;

import com.macondo.stickman.model.Enemy;
import com.macondo.stickman.model.Particle;
import com.macondo.stickman.model.Player;
import com.macondo.stickman.model.Projectile;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

public class GameCanvas extends Canvas {
    private GraphicsContext gc;

    public GameCanvas(double width, double height) {
        super(width, height);
        gc = this.getGraphicsContext2D();
    }

    public void render(Player player, List<Enemy> enemies, List<Projectile> projectiles, List<Particle> particles,
                       int currentLevel, boolean levelComplete, boolean gameComplete, boolean gameOver) {
        gc.setFill(Color.rgb(30, 30, 45));
        gc.fillRect(0, 0, getWidth(), getHeight());

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.WHITE);
        gc.fillText("Level: " + currentLevel, 20, 50);

        gc.setFill(Color.WHITE);
        gc.fillRect(20, 70, 200, 20);
        gc.setFill(Color.RED);
        double healthPercent = player.getHealth() / 100.0;
        gc.fillRect(20, 70, 200 * healthPercent, 20);
        gc.setFill(Color.WHITE);
        gc.fillText("HP: " + player.getHealth(), 20, 65);

        if (player.isInvincible()) {
            gc.setGlobalAlpha(0.5);
        }

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
            double enemyHealthPercent = e.getHealth() / 30.0;
            gc.fillRect(e.getX(), e.getY() - 10, e.getWidth() * enemyHealthPercent, 5);

            gc.setStroke(Color.BLACK);
            gc.setLineWidth(2);
            double eyeX = e.getX() + e.getWidth() * 0.7;
            double eyeY = e.getY() + e.getHeight() * 0.3;
            gc.fillOval(eyeX, eyeY, 5, 5);
            gc.fillOval(eyeX - 15, eyeY, 5, 5);
        }

        for (Projectile p : projectiles) {
            gc.setFill(Color.ORANGERED);
            gc.fillOval(p.getX(), p.getY(), p.getWidth(), p.getHeight());
            gc.setFill(Color.YELLOW);
            gc.fillOval(p.getX() + 5, p.getY() + 5, p.getWidth() - 10, p.getHeight() - 10);
        }

        for (Particle part : particles) {
            double alpha = part.getLife() / 0.6;
            if (alpha > 1) alpha = 1;
            gc.setFill(Color.rgb(255, 100, 0, alpha));
            gc.fillOval(part.getX(), part.getY(), part.getSize(), part.getSize());
        }

        if (player.isAttacking()) {
            gc.setFill(Color.ORANGE);
            gc.fillOval(player.getAttackX(), player.getAttackY() - 15, 25, 30);
        }

        gc.setFill(Color.rgb(200, 120, 80));
        gc.fillRect(player.getVisualX(), player.getVisualY(),
                player.getWidth(), player.getHeight());

        gc.setGlobalAlpha(1.0);

        if (levelComplete && !gameComplete && !gameOver) {
            gc.setFill(Color.rgb(0, 0, 0, 0.7));
            gc.fillRect(0, 0, getWidth(), getHeight());
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            gc.setFill(Color.YELLOW);
            String text = "Level " + currentLevel + " Complete!";
            double textWidth = gc.getFont().getSize() * text.length() * 0.6;
            gc.fillText(text, getWidth()/2 - textWidth/2, getHeight()/2);
        }

        if (gameComplete) {
            gc.setFill(Color.rgb(0, 0, 0, 0.8));
            gc.fillRect(0, 0, getWidth(), getHeight());
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 52));
            gc.setFill(Color.GOLD);
            String text = "VICTORY!";
            double textWidth = gc.getFont().getSize() * text.length() * 0.6;
            gc.fillText(text, getWidth()/2 - textWidth/2, getHeight()/2);

            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
            gc.setFill(Color.WHITE);
            String subtext = "You cleared all levels";
            double subWidth = gc.getFont().getSize() * subtext.length() * 0.6;
            gc.fillText(subtext, getWidth()/2 - subWidth/2, getHeight()/2 + 50);
        }

        if (gameOver) {
            gc.setFill(Color.rgb(0, 0, 0, 0.8));
            gc.fillRect(0, 0, getWidth(), getHeight());
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 52));
            gc.setFill(Color.RED);
            String text = "GAME OVER";
            double textWidth = gc.getFont().getSize() * text.length() * 0.6;
            gc.fillText(text, getWidth()/2 - textWidth/2, getHeight()/2);
        }
    }
}
