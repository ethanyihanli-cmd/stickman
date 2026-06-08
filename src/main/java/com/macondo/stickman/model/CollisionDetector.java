package com.macondo.stickman.model;

public class CollisionDetector {
    public static boolean checkCollision(GameObject a, GameObject b) {
        return a.getX() < b.getX() + b.getWidth() &&
                a.getX() + a.getWidth() > b.getX() &&
                a.getY() < b.getY() + b.getHeight() &&
                a.getY() + a.getHeight() > b.getY();
    }

    public static void handlePlayerEnemyCollision(Player player, Enemy enemy) {
        if (!checkCollision(player, enemy)) {
            return;
        }

        double overlapLeft = (player.getX() + player.getWidth()) - enemy.getX();
        double overlapRight = (enemy.getX() + enemy.getWidth()) - player.getX();

        if (overlapLeft < overlapRight) {
            player.setX(enemy.getX() - player.getWidth());
        } else {
            player.setX(enemy.getX() + enemy.getWidth());
        }
    }

    public static boolean checkAttackHit(Player player, Enemy enemy) {
        if (!player.isAttacking()) return false;

        double attackLeft = player.getAttackX();
        double attackRight = attackLeft + 30;
        double attackTop = player.getAttackY() - 20;
        double attackBottom = player.getAttackY() + 20;

        return attackLeft < enemy.getX() + enemy.getWidth() &&
                attackRight > enemy.getX() &&
                attackTop < enemy.getY() + enemy.getHeight() &&
                attackBottom > enemy.getY();
    }
}

