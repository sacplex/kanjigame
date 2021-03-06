package com.tgd.kanjigame;

import com.tgd.kanjigame.io.ImageIO;

import com.tgd.kanjigame.network.client.Client;
import com.tgd.kanjigame.network.object.PlayerNetworkObject;
import com.tgd.kanjigame.players.Player;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.Scanner;

public class KanjiGame extends Application
{
    Player player;


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
                "blank_card.png", "large_blank_card.png", "play_button.png",
                "pass_button.png");

        imageIO.loadImages();

        System.out.println("Enter your player's name...");

        Scanner scanner = new Scanner(System.in);
        String clientName = scanner.next();

        player = new Player();

        player.setClient(new Client(clientName));

        player.getClient().sendPlayerToServer(new PlayerNetworkObject(player.getName()));
        player.getClient().receiveInitiallyFromServer();
        player.getClient().getPlayerBoard().buildGraphics(imageIO);
        player.getClient().startReaderThread();
        player.getClient().getPlayerBoard().setPlayer(player);

        Game game = new Game(gc);
        game.addPlayerBoard(player.getClient().getPlayerBoard());
        game.addScene(scene);

        game.initMouse();

        game.run();

        primaryStage.show();
    }

    @Override
    public void stop(){
        player.getClient().terminate();
        System.out.println("Stage is closing");
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
