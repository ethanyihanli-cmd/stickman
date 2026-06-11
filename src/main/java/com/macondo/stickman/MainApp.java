package com.macondo.stickman;

import com.macondo.stickman.controller.GameLoop;
import com.macondo.stickman.input.InputHandler;
import com.macondo.stickman.view.GameCanvas;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage primaryStage) {
        InputHandler input = new InputHandler();
        GameCanvas canvas = new GameCanvas(1024, 768);
        GameLoop gameLoop = new GameLoop(canvas, input);

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root, 1024, 768);

        scene.setOnKeyPressed(e -> input.keyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> input.keyReleased(e.getCode()));

        primaryStage.setTitle("Stickman Arena: Shadow Brawl");
        primaryStage.setScene(scene);
        primaryStage.show();

        gameLoop.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

