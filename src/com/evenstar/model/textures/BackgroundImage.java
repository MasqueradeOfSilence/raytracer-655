package com.evenstar.model.textures;

public class BackgroundImage
{
    // Can only be 512x512 currently
    private final String fileName;

    public BackgroundImage(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}
