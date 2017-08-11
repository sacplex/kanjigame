package com.tgd.kanjigame.control;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;

public class Button
{
    private Rectangle box;
    private Image image;
    private int x, y;

    public Button(Image image, int x, int y)
    {
        this.image = image;
        this.x = x;
        this.y = y;

        box = new Rectangle(x, y, (int)image.getWidth(), (int)image.getHeight());
    }

    public void draw(GraphicsContext gc)
    {
        gc.drawImage(image, x, y);
    }

    public boolean intersected(int x, int y)
    {
        return box.contains(x, y);
    }
}
