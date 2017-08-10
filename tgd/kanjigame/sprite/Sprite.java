package com.tgd.kanjigame.sprite;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.awt.*;

public abstract class Sprite
{
    protected Image image;
    protected int x, y;
    protected int width, height;
    protected Rectangle box;

    public Sprite(Image image, int x, int y)
    {
        this.image = image;
        this.x = x;
        this.y = y;

        width = (int)image.getWidth();
        height = (int)image.getHeight();

        box = new Rectangle(x, y, width, height);
    }

    public void draw(GraphicsContext gc)
    {
        gc.drawImage(image, x, y);
    }

    public void setLocation(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public void updateLocation(int x, int y)
    {
        this.x = this.x + x;
        this.y = this.y + y;
    }

    public Image getImage() { return image; }

    public Rectangle getBox()
    {
        return box;
    }

    public int getX() { return x; }

    public int getY() { return y; }
}
