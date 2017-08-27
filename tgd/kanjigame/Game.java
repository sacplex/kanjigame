package com.tgd.kanjigame;

import com.tgd.kanjigame.board.PlayerBoard;
import com.tgd.kanjigame.card.Card;
import com.tgd.kanjigame.io.ImageIO;
import com.tgd.kanjigame.network.client.Client;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Collections;

public class Game
{
    public final static int WIDTH = 1280;
    public final static int HEIGHT = 720;

    private Scene scene;
    private GraphicsContext gc;
    private Client client;
    private PlayerBoard playerBoard;

    private Card playingCard;

    public Game(GraphicsContext gc)
    {
        this.gc = gc;
    }

    public void initMouse()
    {
        if(scene != null)
        {
            scene.setOnMousePressed(event -> {
                playerBoard.hasMouseIntersectedWithButton((int)event.getX(), (int)event.getY());

                if(playingCard != null) {
                    if(!playerBoard.getPlayArea().hasCardIntersectedWithPlayArea(playingCard))
                        playingCard.resetOriginal();
                }

                playingCard = playerBoard.hasMouseIntersectedWithCard((int)event.getX(), (int)event.getY());
            });

            scene.setOnMouseMoved(mouseMoveEvent -> {
                if(playingCard != null)
                    playingCard.dragCard((int) mouseMoveEvent.getX(), (int) mouseMoveEvent.getY());
            });

            /*scene.setOnMouseReleased(mouseReleasedEvent -> {
                if(playingCard != null) {
                    System.out.println("released");

                }
            });*/
        }
    }

    public void run()
    {
        Timeline gameLoop = new Timeline();
        gameLoop.setCycleCount(Timeline.INDEFINITE);

        final long timeStart = System.currentTimeMillis();

        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(0.017), event -> {
                    update();
                    render();
        });

        gameLoop.getKeyFrames().add(keyFrame);
        gameLoop.play();
    }

    private void update()
    {

    }

    private void render()
    {
        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,1280,720);
        gc.setFill(Color.BLACK);

        if(playerBoard != null)
            playerBoard.draw(gc);
    }

    public void addScene(Scene scene)
    {
        this.scene = scene;
    }

    public void addPlayerBoard(PlayerBoard playerBoard)
    {
        this.playerBoard = playerBoard;
    }

    public void addClient(Client client) { this.client = client; }
}
