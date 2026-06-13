package com.macondo.stickman.model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int levelNumber;
    private List<EnemyData> enemies;
    private List<Platform> platforms;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.enemies = new ArrayList<>();
        this.platforms = new ArrayList<>();
        setupEnemiesAndPlatforms();
    }

    private void setupEnemiesAndPlatforms() {
        platforms.add(new Platform(0, 650, 1024, 40));

        switch (levelNumber) {
            case 1:
                enemies.add(new EnemyData(720, 590, 610, 930));
                platforms.add(new Platform(400, 480, 250, 20));
                break;
            case 2:
                enemies.add(new EnemyData(760, 590, 620, 930));
                enemies.add(new EnemyData(380, 590, 260, 520));
                platforms.add(new Platform(150, 480, 200, 20));
                platforms.add(new Platform(600, 460, 220, 20));
                break;
            case 3:
                enemies.add(new EnemyData(800, 590, 660, 950));
                enemies.add(new EnemyData(470, 590, 350, 620));
                enemies.add(new EnemyData(220, 590, 150, 340));
                platforms.add(new Platform(300, 480, 200, 20));
                platforms.add(new Platform(700, 450, 200, 20));
                break;
            default:
                enemies.add(new EnemyData(300, 590, 200, 700));
                break;
        }
    }

    public List<EnemyData> getEnemyData() {
        return enemies;
    }

    public List<Platform> getPlatforms() { return platforms; }

    public int getLevelNumber() {
        return levelNumber;
    }

    public static class EnemyData {
        public double x, y, patrolLeft, patrolRight;

        public EnemyData(double x, double y, double patrolLeft, double patrolRight) {
            this.x = x;
            this.y = y;
            this.patrolLeft = patrolLeft;
            this.patrolRight = patrolRight;
        }
    }
}
