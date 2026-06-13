package com.macondo.stickman.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SoundPlayer {
    private static SoundPlayer instance;
    private Map<String, MediaPlayer> soundPlayers;
    private MediaPlayer bgMusicPlayer;

    private SoundPlayer() {
        soundPlayers = new HashMap<>();
    }

    public static SoundPlayer getInstance() {
        if (instance == null) {
            instance = new SoundPlayer();
        }
        return instance;
    }

    public void loadSound(String name, String filePath) {
        try {
            URL resource = getClass().getResource(filePath);
            if (resource == null) {
                System.err.println("Sound file not found: " + filePath);
                return;
            }
            Media media = new Media(resource.toString());
            MediaPlayer player = new MediaPlayer(media);
            soundPlayers.put(name, player);
        } catch (Exception e) {
            System.err.println("Failed to load sound " + name + ": " + e.getMessage());
        }
    }

    public void playSound(String name) {
        MediaPlayer player = soundPlayers.get(name);
        if (player != null) {
            player.stop();
            player.seek(javafx.util.Duration.ZERO);
            player.play();
        }
    }

    public void playBackgroundMusic(String filePath, boolean loop) {
        try {
            URL resource = getClass().getResource(filePath);
            if (resource == null) {
                System.err.println("Music file not found: " + filePath);
                return;
            }
            if (bgMusicPlayer != null) {
                bgMusicPlayer.stop();
            }
            Media media = new Media(resource.toString());
            bgMusicPlayer = new MediaPlayer(media);
            if (loop) {
                bgMusicPlayer.setCycleCount(javafx.animation.Animation.INDEFINITE);
            }
            bgMusicPlayer.play();
        } catch (Exception e) {
            System.err.println("Failed to load background music: " + e.getMessage());
        }
    }

    public void stopBackgroundMusic() {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.stop();
        }
    }

    public void setMusicVolume(double volume) {
        if (bgMusicPlayer != null) {
            bgMusicPlayer.setVolume(volume);
        }
    }

    public void setSoundVolume(String name, double volume) {
        MediaPlayer player = soundPlayers.get(name);
        if (player != null) {
            player.setVolume(volume);
        }
    }
}
