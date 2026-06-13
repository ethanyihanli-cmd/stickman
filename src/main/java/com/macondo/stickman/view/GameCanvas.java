package com.macondo.stickman.view;

import com.macondo.stickman.model.Enemy;
import com.macondo.stickman.model.Particle;
import com.macondo.stickman.model.Player;
import com.macondo.stickman.model.Projectile;
import com.macondo.stickman.utils.ScoreManager;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.List;

public class GameCanvas extends Canvas {
    private GraphicsContext gc;
    private static final Color PLAYER_COLOR = Color.rgb(42, 184, 255);
    private static final Color ENEMY_COLOR = Color.rgb(218, 58, 58);

    public GameCanvas(double width, double height) {
        super(width, height);
        gc = this.getGraphicsContext2D();
    }

    public void render(Player player, List<Enemy> enemies, List<Projectile> projectiles, List<Particle> particles,
                       int currentLevel, boolean levelComplete, boolean gameComplete, boolean gameOver,
                       ScoreManager scoreManager, java.util.List<com.macondo.stickman.model.Platform> platforms) {
        drawBackground();
        drawHud(player, currentLevel, scoreManager);

        for (com.macondo.stickman.model.Platform plat : platforms) {
            gc.setFill(Color.rgb(63, 92, 70));
            gc.fillRoundRect(plat.getX(), plat.getY(), plat.getWidth(), plat.getHeight(), 8, 8);
            gc.setFill(Color.rgb(94, 138, 80));
            gc.fillRoundRect(plat.getX(), plat.getY(), plat.getWidth(), 8, 8, 8);
            gc.setFill(Color.rgb(73, 52, 39));
            gc.fillRect(plat.getX(), plat.getY() + 8, plat.getWidth(), plat.getHeight() - 8);
        }

        for (Enemy e : enemies) {
            String state = e.isAttacking() ? "ATTACK" : (e.getVx() != 0 ? "RUN" : "IDLE");
            boolean facingRight = e.getVx() >= 0;
            StickmanDrawer.drawStickman(gc, e.getX(), e.getY(), e.getWidth(), e.getHeight(),
                    state, facingRight, e.isHitFlash(), e.isAttacking(), ENEMY_COLOR);

            gc.setFill(Color.rgb(30, 35, 40, 0.65));
            gc.fillRoundRect(e.getX() - 2, e.getY() - 13, e.getWidth() + 4, 7, 4, 4);
            gc.setFill(Color.LIMEGREEN);
            double enemyHealthPercent = e.getHealth() / 3.0;
            gc.fillRoundRect(e.getX(), e.getY() - 12, e.getWidth() * enemyHealthPercent, 5, 4, 4);
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

        gc.setFill(Color.rgb(42, 184, 255, 0.22));
        gc.fillOval(player.getVisualX() - 11, player.getVisualY() - 8,
                player.getWidth() + 22, player.getHeight() + 18);
        StickmanDrawer.drawStickman(gc, player.getVisualX(), player.getVisualY(),
                player.getWidth(), player.getHeight(),
                player.getAnimationState(), player.isFacingRight(),
                player.isInvincible(), player.isAttacking(), PLAYER_COLOR);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        gc.setFill(Color.WHITE);
        gc.fillText("YOU", player.getVisualX() + 8, player.getVisualY() - 18);
        gc.setFill(Color.rgb(30, 35, 40, 0.65));
        gc.fillRoundRect(player.getVisualX() - 2, player.getVisualY() - 13, player.getWidth() + 4, 7, 4, 4);
        gc.setFill(Color.rgb(42, 220, 120));
        gc.fillRoundRect(player.getVisualX(), player.getVisualY() - 12,
                player.getWidth() * Math.max(0, player.getHealth() / 5.0), 5, 4, 4);

        if (levelComplete && !gameComplete && !gameOver) {
            gc.setFill(Color.rgb(0, 0, 0, 0.7));
            gc.fillRect(0, 0, getWidth(), getHeight());
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 48));
            gc.setFill(Color.YELLOW);
            String text = "Level " + currentLevel + " Complete!";
            double textWidth = gc.getFont().getSize() * text.length() * 0.6;
            gc.fillText(text, getWidth()/2 - textWidth/2, getHeight()/2);
            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
            gc.fillText("+200 bonus", getWidth()/2 - 50, getHeight()/2 + 50);
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

            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
            String restartText = "Press R to restart";
            double restartWidth = gc.getFont().getSize() * restartText.length() * 0.6;
            gc.fillText(restartText, getWidth()/2 - restartWidth/2, getHeight()/2 + 100);
        }

        if (gameOver) {
            gc.setFill(Color.rgb(0, 0, 0, 0.8));
            gc.fillRect(0, 0, getWidth(), getHeight());
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 52));
            gc.setFill(Color.RED);
            String text = "GAME OVER";
            double textWidth = gc.getFont().getSize() * text.length() * 0.6;
            gc.fillText(text, getWidth()/2 - textWidth/2, getHeight()/2);

            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 24));
            gc.setFill(Color.WHITE);
            String subtext = "Final Score: " + scoreManager.getCurrentScore();
            double subWidth = gc.getFont().getSize() * subtext.length() * 0.6;
            gc.fillText(subtext, getWidth()/2 - subWidth/2, getHeight()/2 + 50);

            gc.setFont(Font.font("Arial", FontWeight.NORMAL, 18));
            String restartText = "Press R to restart";
            double restartWidth = gc.getFont().getSize() * restartText.length() * 0.6;
            gc.fillText(restartText, getWidth()/2 - restartWidth/2, getHeight()/2 + 100);
        }
    }

    private void drawBackground() {
        double w = getWidth();
        double h = getHeight();

        gc.setFill(Color.rgb(127, 197, 238));
        gc.fillRect(0, 0, w, h);
        gc.setFill(Color.rgb(184, 226, 247));
        gc.fillRect(0, h * 0.36, w, h * 0.28);

        gc.setFill(Color.rgb(255, 220, 94));
        gc.fillOval(w - 150, 58, 78, 78);

        drawCloud(110, 92, 1.0);
        drawCloud(350, 142, 0.75);
        drawCloud(710, 102, 0.9);

        gc.setFill(Color.rgb(90, 122, 146));
        fillTriangle(-80, 420, 190, 170, 460, 420);
        fillTriangle(240, 420, 535, 135, 830, 420);
        fillTriangle(610, 420, 880, 190, 1130, 420);

        gc.setFill(Color.rgb(152, 168, 172, 0.85));
        fillTriangle(130, 226, 190, 170, 246, 226);
        fillTriangle(472, 198, 535, 135, 604, 198);
        fillTriangle(825, 240, 880, 190, 935, 240);

        gc.setFill(Color.rgb(61, 139, 87));
        gc.fillOval(-120, 365, 470, 210);
        gc.fillOval(230, 340, 520, 240);
        gc.fillOval(650, 370, 520, 220);

        for (int i = 0; i < 9; i++) {
            drawTree(54 + i * 118, 545 + (i % 3) * 16, 0.85 + (i % 2) * 0.18);
        }
    }

    private void drawCloud(double x, double y, double scale) {
        gc.setFill(Color.rgb(255, 255, 255, 0.82));
        gc.fillOval(x, y + 15 * scale, 66 * scale, 30 * scale);
        gc.fillOval(x + 24 * scale, y, 58 * scale, 42 * scale);
        gc.fillOval(x + 64 * scale, y + 12 * scale, 70 * scale, 34 * scale);
    }

    private void drawTree(double x, double y, double scale) {
        gc.setFill(Color.rgb(84, 57, 38));
        gc.fillRect(x + 18 * scale, y + 30 * scale, 12 * scale, 70 * scale);
        gc.setFill(Color.rgb(35, 106, 62));
        fillTriangle(x, y + 52 * scale, x + 24 * scale, y, x + 48 * scale, y + 52 * scale);
        gc.setFill(Color.rgb(42, 126, 70));
        fillTriangle(x - 5 * scale, y + 82 * scale, x + 24 * scale, y + 24 * scale, x + 53 * scale, y + 82 * scale);
    }

    private void drawHud(Player player, int currentLevel, ScoreManager scoreManager) {
        gc.setFill(Color.rgb(16, 28, 38, 0.72));
        gc.fillRoundRect(16, 16, getWidth() - 32, 56, 10, 10);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        gc.setFill(Color.WHITE);
        gc.fillText("Level " + currentLevel, 32, 51);
        gc.fillText("Score " + scoreManager.getCurrentScore(), 172, 51);
        gc.fillText("Best " + scoreManager.getHighScore(), 330, 51);

        gc.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        gc.fillText("HP", getWidth() - 264, 51);
        gc.setFill(Color.rgb(235, 241, 244, 0.85));
        gc.fillRoundRect(getWidth() - 230, 34, 190, 16, 8, 8);
        gc.setFill(Color.rgb(224, 62, 71));
        gc.fillRoundRect(getWidth() - 230, 34, 190 * Math.max(0, player.getHealth() / 5.0), 16, 8, 8);
    }

    private void fillTriangle(double x1, double y1, double x2, double y2, double x3, double y3) {
        gc.fillPolygon(new double[] { x1, x2, x3 }, new double[] { y1, y2, y3 }, 3);
    }
}
