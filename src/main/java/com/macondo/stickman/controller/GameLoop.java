package com.macondo.stickman.controller;

import com.macondo.stickman.input.InputHandler;
import com.macondo.stickman.model.CollisionDetector;
import com.macondo.stickman.model.Enemy;
import com.macondo.stickman.model.Level;
import com.macondo.stickman.model.Particle;
import com.macondo.stickman.model.Platform;
import com.macondo.stickman.model.Player;
import com.macondo.stickman.model.Projectile;
import com.macondo.stickman.utils.ScoreManager;
import com.macondo.stickman.utils.SoundPlayer;
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
    private boolean levelCompleteSoundPlayed;
    private boolean gameOverSoundPlayed;
    private boolean victorySoundPlayed;
    private ScoreManager scoreManager;
    private java.util.List<Platform> platforms;

    public GameLoop(GameCanvas canvas, InputHandler input) {
        this.canvas = canvas;
        this.input = input;
        this.player = new Player(100, 600, input);
        this.platforms = new java.util.ArrayList<>();
        this.enemies = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.particles = new ArrayList<>();
        this.currentLevel = 1;
        this.levelComplete = false;
        this.gameComplete = false;
        this.gameOver = false;
        this.levelCompleteSoundPlayed = false;
        this.gameOverSoundPlayed = false;
        this.victorySoundPlayed = false;
        this.scoreManager = ScoreManager.getInstance();
        scoreManager.resetScore();

        SoundPlayer sp = SoundPlayer.getInstance();
        sp.loadSound("attack", "/com/macondo/stickman/sound/Attack sound.wav");
        sp.loadSound("hit", "/com/macondo/stickman/sound/Hit sound.wav");
        sp.loadSound("fireball", "/com/macondo/stickman/sound/Fireball sound.mp3");
        sp.loadSound("hurt", "/com/macondo/stickman/sound/Hurt sound.wav");
        sp.loadSound("levelComplete", "/com/macondo/stickman/sound/Level complete sound.wav");
        sp.loadSound("gameOver", "/com/macondo/stickman/sound/Game over sound.wav");
        sp.loadSound("victory", "/com/macondo/stickman/sound/Victory sound.wav");
        sp.playBackgroundMusic("/com/macondo/stickman/sound/BGM.mp3", true);

        loadLevel(currentLevel);
        initLoop();
    }

    private void loadLevel(int levelNum) {
        enemies.clear();
        projectiles.clear();
        particles.clear();
        Level level = new Level(levelNum);
        platforms.clear();
        platforms.addAll(level.getPlatforms());

        Platform ground = findMainPlatform();

        if (ground != null) {
            player.setX(100);
            player.setY(ground.getY() - player.getHeight());
        } else {
            player.setX(100);
            player.setY(600);
        }
        player.setVx(0);
        player.setVy(0);

        enemies.clear();
        for (Level.EnemyData data : level.getEnemyData()) {
            double enemyStartY = ground == null ? data.y : ground.getY() - 60;
            enemies.add(new Enemy(data.x, enemyStartY, data.patrolLeft, data.patrolRight));
        }

        levelComplete = false;
        gameOver = false;
        levelCompleteSoundPlayed = false;
    }

    private Platform findMainPlatform() {
        Platform main = null;
        for (Platform platform : platforms) {
            if (main == null || platform.getWidth() > main.getWidth()) {
                main = platform;
            }
        }
        return main;
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
                canvas.render(player, enemies, projectiles, particles, currentLevel,
                        levelComplete, gameComplete, gameOver, scoreManager, platforms);
            }
        };
    }

    private void update(double dt) {
        if (gameComplete || gameOver) {
            if (gameComplete && !victorySoundPlayed) {
                SoundPlayer.getInstance().playSound("victory");
                victorySoundPlayed = true;
            }
            if (gameOver && !gameOverSoundPlayed) {
                SoundPlayer.getInstance().playSound("gameOver");
                gameOverSoundPlayed = true;
            }

            if (input.isKeyDown(KeyCode.R)) {
                restartGame();
            }
            return;
        }

        if (levelComplete) {
            if (!levelCompleteSoundPlayed) {
                SoundPlayer.getInstance().playSound("levelComplete");
                levelCompleteSoundPlayed = true;
                scoreManager.addScore(200);
            }
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

        player.update(dt, platforms);

        if (input.isKeyDown(KeyCode.L) && player.canUseSpecial()) {
            player.useSpecial();
            SoundPlayer.getInstance().playSound("fireball");
            double direction = player.isFacingRight() ? 1 : -1;
            Projectile fireball = new Projectile(
                    player.getCenterX(), player.getCenterY(),
                    direction * 500, 0
            );
            projectiles.add(fireball);
        }

        List<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy e : enemies) {
            boolean wasAttacking = e.isAttacking();
            e.updateAI(player, dt, platforms);
            boolean nowAttacking = e.isAttacking();
            if (nowAttacking && !wasAttacking) {
                SoundPlayer.getInstance().playSound("attack");
            }

            CollisionDetector.handlePlayerEnemyCollision(player, e);
            if (CollisionDetector.handleEnemyAttack(player, e)) {
                SoundPlayer.getInstance().playSound("hurt");
            }

            if (CollisionDetector.checkAttackHit(player, e)) {
                e.takeDamage(1);
                SoundPlayer.getInstance().playSound("hit");
                for (int i = 0; i < 5; i++) {
                    particles.add(new Particle(
                            e.getX() + e.getWidth()/2, e.getY() + e.getHeight()/2,
                            (Math.random() - 0.5) * 200,
                            (Math.random() - 0.5) * 200 - 100,
                            5, 0.5
                    ));
                }
            }
            if (!e.isAlive()) {
                enemiesToRemove.add(e);
                scoreManager.addScore(100);
            }
        }
        enemies.removeAll(enemiesToRemove);

        if (input.isAttacking() && player.isAttacking()) {
            SoundPlayer.getInstance().playSound("attack");
        }

        for (Projectile p : projectiles) {
            p.update(dt);
            for (Enemy e : enemies) {
                if (CollisionDetector.checkCollision(p, e)) {
                    e.takeDamage(p.getDamage());
                    SoundPlayer.getInstance().playSound("hit");
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

        for (Particle part : particles) {
            part.update(dt);
        }
        particles.removeIf(p -> !p.isAlive());

        if (enemies.isEmpty() && !levelComplete) {
            levelComplete = true;
            levelCompleteTimer = 2.0;
        }
    }

    private void restartGame() {
        currentLevel = 1;
        gameComplete = false;
        gameOver = false;
        levelComplete = false;
        levelCompleteSoundPlayed = false;
        gameOverSoundPlayed = false;
        victorySoundPlayed = false;
        scoreManager.resetScore();
        player = new Player(100, 600, input);
        loadLevel(currentLevel);
    }

    public void start() {
        timer.start();
    }

    public void stop() {
        timer.stop();
        SoundPlayer.getInstance().stopBackgroundMusic();
    }
}
