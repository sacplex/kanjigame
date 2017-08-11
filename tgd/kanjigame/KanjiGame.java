package com.tgd.kanjigame;

import com.tgd.kanjigame.board.PlayerBoard;
import com.tgd.kanjigame.database.LoadDatabase;
import com.tgd.kanjigame.io.ImageIO;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

public class KanjiGame extends Application
{

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Kanji Game");

        Group root = new Group();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        Canvas canvas = new Canvas(Game.WIDTH, Game.HEIGHT);
        root.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        ImageIO imageIO = new ImageIO();
        imageIO.addAllFileNames("black_v_card.png",
                "blue_v_card.png", "green_v_card.png", "orange_v_card.png",
                "purple_v_card.png", "red_v_card.png", "white_v_card.png",
                "blank_card.png", "large_blank_card.png", "play_button.png");

        imageIO.loadImages();

        LoadDatabase loadDatabase = LoadDatabase.getInstance();

        PlayerBoard playerBoard = new PlayerBoard(loadDatabase, imageIO);

        Game game = new Game(gc);
        game.addPlayerBoard(playerBoard);
        game.addScene(scene);

        game.initMouse();

        game.run();

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
