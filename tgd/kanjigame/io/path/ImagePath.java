package com.tgd.kanjigame.io.path;

public class ImagePath extends Path
{
    private final String imageDirectory = "Images";

    public String getImagePath()
    {
        return getJavaFXFilePreDirectory() + currentDirectory + slash + imageDirectory + slash;
    }
}
