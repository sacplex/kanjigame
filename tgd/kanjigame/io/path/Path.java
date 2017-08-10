package com.tgd.kanjigame.io.path;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Path
{
    protected ArrayList<String> fileNames;
    protected volatile boolean running;
    protected Thread loader;

    protected String currentDirectory;

    protected static String slash = File.separator;

    public Path()
    {
        fileNames = new ArrayList<>();

        try
        {
            currentDirectory = new File(".").getCanonicalPath();
            this.currentDirectory = currentDirectory + slash;
        }
        catch(IOException e)
        {
            System.out.println("<Path> Error, Can not find parent directory");
        }
    }

    protected String getJavaFXFilePreDirectory()
    {
        return "file:///";
    }
}
