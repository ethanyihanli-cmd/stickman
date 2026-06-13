package com.macondo.stickman.view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class StickmanDrawer {

    public static void drawStickman(GraphicsContext gc, double x, double y,
                                    double width, double height,
                                    String state, boolean facingRight,
                                    boolean isInvincible, boolean isAttacking, Color bodyColor) {

        double headRadius = height * 0.2;
        double bodyStartY = y + headRadius * 1.5;
        double bodyEndY = y + height * 0.6;

        if (isInvincible) {
            gc.setStroke(Color.rgb(255, 255, 255, 0.6));
            gc.setFill(Color.rgb(200, 200, 200, 0.6));
        } else {
            gc.setStroke(Color.BLACK);
            gc.setFill(bodyColor);
        }

        gc.setLineWidth(3);

        double centerX = x + width / 2;

        gc.fillOval(centerX - headRadius, y, headRadius * 2, headRadius * 2);

        gc.strokeLine(centerX, bodyStartY, centerX, bodyEndY);

        double armAngle = 0;
        double legAngle = 0;

        switch (state) {
            case "RUN":
                armAngle = Math.sin(System.currentTimeMillis() * 0.015) * 0.8;
                legAngle = Math.sin(System.currentTimeMillis() * 0.015) * 0.6;
                break;
            case "JUMP":
                armAngle = 0.5;
                legAngle = 0.7;
                break;
            case "ATTACK":
                armAngle = 1.2;
                legAngle = 0.2;
                break;
            default:
                armAngle = 0;
                legAngle = 0;
                break;
        }

        double leftArmX, leftArmY, rightArmX, rightArmY;
        double leftLegX, leftLegY, rightLegX, rightLegY;

        if (facingRight) {
            leftArmX = centerX - 20 - armAngle * 15;
            leftArmY = bodyStartY + 15 + armAngle * 10;
            rightArmX = centerX + 20 + armAngle * 15;
            rightArmY = bodyStartY + 15 - armAngle * 10;

            leftLegX = centerX - 15 - legAngle * 10;
            leftLegY = bodyEndY + 25 + legAngle * 15;
            rightLegX = centerX + 15 + legAngle * 10;
            rightLegY = bodyEndY + 25 - legAngle * 15;
        } else {
            leftArmX = centerX + 20 + armAngle * 15;
            leftArmY = bodyStartY + 15 + armAngle * 10;
            rightArmX = centerX - 20 - armAngle * 15;
            rightArmY = bodyStartY + 15 - armAngle * 10;

            leftLegX = centerX + 15 + legAngle * 10;
            leftLegY = bodyEndY + 25 + legAngle * 15;
            rightLegX = centerX - 15 - legAngle * 10;
            rightLegY = bodyEndY + 25 - legAngle * 15;
        }

        gc.strokeLine(centerX, bodyStartY + 15, leftArmX, leftArmY);
        gc.strokeLine(centerX, bodyStartY + 15, rightArmX, rightArmY);

        gc.strokeLine(centerX, bodyEndY, leftLegX, leftLegY);
        gc.strokeLine(centerX, bodyEndY, rightLegX, rightLegY);

        gc.fillOval(centerX - 4, y + headRadius * 1.2, 3, 3);
        gc.fillOval(centerX + 1, y + headRadius * 1.2, 3, 3);

        if (isAttacking) {
            gc.setStroke(Color.ORANGE);
            gc.setLineWidth(5);
            if (facingRight) {
                gc.strokeLine(centerX + 15, bodyStartY + 15, centerX + 45, bodyStartY + 10);
            } else {
                gc.strokeLine(centerX - 15, bodyStartY + 15, centerX - 45, bodyStartY + 10);
            }
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(3);
        }
    }
}
