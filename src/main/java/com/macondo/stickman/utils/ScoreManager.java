package com.macondo.stickman.utils;

import java.util.prefs.Preferences;

public class ScoreManager {
    private static ScoreManager instance;
    private int currentScore;
    private int highScore;
    private Preferences prefs;

    private ScoreManager() {
        prefs = Preferences.userNodeForPackage(ScoreManager.class);
        highScore = prefs.getInt("highScore", 0);
        currentScore = 0;
    }

    public static ScoreManager getInstance() {
        if (instance == null) {
            instance = new ScoreManager();
        }
        return instance;
    }

    public void addScore(int points) {
        currentScore += points;
        if (currentScore > highScore) {
            highScore = currentScore;
            prefs.putInt("highScore", highScore);
        }
    }

    public void resetScore() {
        currentScore = 0;
    }

    public int getCurrentScore() {
        return currentScore;
    }

    public int getHighScore() {
        return highScore;
    }
}
