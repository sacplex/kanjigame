package com.tgd.kanjigame.io;

import com.tgd.kanjigame.io.path.Path;

import java.util.ArrayList;

public abstract class IO implements Runnable
{
    protected Path path;
    protected ArrayList<String> fileNames;

    public IO()
    {
        fileNames = new ArrayList<>();
    }

    public void addFileNames(String fileName)
    {
        fileNames.add(fileName);
    }

    public void addAllFileNames(String... _fileNames)
    {
        for(int i=0; i<_fileNames.length; i++)
            fileNames.add(_fileNames[i]);
    }
}
