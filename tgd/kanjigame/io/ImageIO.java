package com.tgd.kanjigame.io;

import com.tgd.kanjigame.io.path.ImagePath;
import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageIO extends IO
{
    private HashMap<String, Image> imageMap;

    public ImageIO()
    {
        super();

        path = new ImagePath();
        imageMap = new HashMap<>();
    }

    public void loadImages()
    {
        for(String s : fileNames)
        {
            if(imageMap.get(s) == null)
                imageMap.put(s, new Image(((ImagePath)path).getImagePath() + s));
        }
    }

    public Image getImage(String fileName)
    {
        return imageMap.get(fileName);
    }

    public void run()
    {

    }
}
