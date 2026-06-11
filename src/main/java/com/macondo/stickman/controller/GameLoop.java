package com.macondo.stickman.controller;

import com.macondo.stickman.input.InputHandler;
import com.macondo.stickman.model.CollisionDetector;
import com.macondo.stickman.model.Enemy;
import com.macondo.stickman.model.Level;
import com.macondo.stickman.model.Particle;
import com.macondo.stickman.model.Player;
import com.macondo.stickman.model.Projectile;
import com.macondo.stickman.view.GameCanvas;
import javafx.animation.AnimationTimer;
import javafx.scene.input.KeyCode;
import java.util.ArrayList;
import java.util.List;

public class GameLoop {
    private GameCanvas canvas;
    private Player player;
    private InputHandler input;
    private List<Enemy> enemies;
    private List<Projectile> projectiles;
    private List<Particle> particles;
    private AnimationTimer timer;
    private int currentLevel;
    private boolean levelComplete;
    private boolean gameComplete;
    private boolean gameOver;
    private double levelCompleteTimer;

    public GameLoop(GameCanvas canvas, InputHandler input) {
        this.canvas = canvas;
        this.input = input;
        this.player = new Player(100, 600, input);
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.particles = new ArrayList<>();
        this.currentLevel = 1;
        this.levelComplete = false;
        this.gameComplete = false;
        this.gameOver = false;
        loadLevel(currentLevel);
        initLoop();
    }

    private void loadLevel(int levelNum) {
        enemies.clear();
        projectiles.clear();
        particles.clear();
        Level level = new Level(levelNum);
        for (Level.EnemyData data : level.getEnemyData()) {
            enemies.add(new Enemy(data.x, data.y, data.patrolLeft, data.patrolRight));
        }
        player.setX(100);
        player.setY(600);
        player.setVx(0);
        player.setVy(0);
        levelComplete = false;
        gameOver = false;
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
                canvas.render(player, enemies, projectiles, particles, currentLevel, levelComplete, gameComplete, gameOver);
            }
        };
    }

    private void update(double dt) {
        if (gameComplete || gameOver) {
            return;
        }

        if (levelComplete) {
            levelCompleteTimer -= dt;
            if (levelCompleteTimer <= 0) {
                currentLevel++;
                if (currentLevel > 3) {
                    gameComplete = true;
                } else {
                    loadLevel(currentLevel);
                }
            }
            return;
        }

        if (!player.isAlive()) {
            gameOver = true;
            return;
        }

        player.update(dt);

        if (input.isKeyDown(KeyCode.L) && player.canUseSpecial()) {
            player.useSpecial();
            double direction = player.isFacingRight() ? 1 : -1;
            Projectile fireball = new Projectile(
                    player.getCenterX(), player.getCenterY(),
                    direction * 500, 0
            );
            projectiles.add(fireball);
        }

        for (Enemy e : enemies) {
            e.updateAI(player, dt);
            CollisionDetector.handlePlayerEnemyCollision(player, e);
            CollisionDetector.handleEnemyAttack(player, e);

            if (CollisionDetector.checkAttackHit(player, e)) {
                e.takeDamage(10);
                for (int i = 0; i < 5; i++) {
                    particles.add(new Particle(
                            e.getX() + e.getWidth()/2, e.getY() + e.getHeight()/2,
                            (Math.random() - 0.5) * 200,
                            (Math.random() - 0.5) * 200 - 100,
                            5, 0.5
                    ));
                }
            }
        }

        for (Projectile p : projectiles) {
            p.update(dt);
            for (Enemy e : enemies) {
                if (CollisionDetector.checkCollision(p, e)) {
                    e.takeDamage(p.getDamage());
                    for (int i = 0; i < 10; i++) {
                        particles.add(new Particle(
                                e.getX() + e.getWidth()/2, e.getY() + e.getHeight()/2,
                                (Math.random() - 0.5) * 300,
                                (Math.random() - 0.5) * 300 - 100,
                                4, 0.6
                        ));
                    }
                    p.setX(-100);
                    p.setY(-100);
                }
            }
        }

        projectiles.removeIf(p -> p.isExpired());
        enemies.removeIf(e -> !e.isAlive());

        for (Particle part : particles) {
            part.update(dt);
        }
        particles.removeIf(p -> !p.isAlive());

        if (enemies.isEmpty() && !levelComplete) {
            levelComplete = true;
            levelCompleteTimer = 2.0;
        }
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
    }
}
