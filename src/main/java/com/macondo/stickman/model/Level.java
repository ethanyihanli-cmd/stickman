package com.macondo.stickman.model;

import java.util.ArrayList;
import java.util.List;

public class Level {
    private int levelNumber;
    private List<EnemyData> enemies;

    public Level(int levelNumber) {
        this.levelNumber = levelNumber;
        this.enemies = new ArrayList<>();
        setupEnemies();
    }

    private void setupEnemies() {
        switch (levelNumber) {
            case 1:
                enemies.add(new EnemyData(300, 590, 200, 700));
                break;
            case 2:
                enemies.add(new EnemyData(500, 590, 400, 800));
                enemies.add(new EnemyData(200, 590, 100, 350));
                break;
            case 3:
                enemies.add(new EnemyData(600, 590, 500, 900));
                enemies.add(new EnemyData(350, 590, 250, 600));
                enemies.add(new EnemyData(100, 590, 50, 250));
                break;
            default:
                enemies.add(new EnemyData(300, 590, 200, 700));
                break;
        }
    }

    public List<EnemyData> getEnemyData() {
        return enemies;
    }

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
