package com.evenstar;

import java.util.ArrayList;

public class PPMImage
{
    private int width = 256;
    private int height = 256;
    private ArrayList<ArrayList<Integer>> pixels = new ArrayList<ArrayList<Integer>>();
    private final String beginningPartOfPPM;

    public String getBeginningPartOfPPM() {
        return beginningPartOfPPM;
    }

    public PPMImage(int width, int height) {
        this.width = width;
        this.height = height;
        beginningPartOfPPM = "P3" + "\n" + width + "\n" + height + "\n" + "255" + "\n";
    }

    public ArrayList<ArrayList<Integer>> getPixels()
    {
        return pixels;
    }

    public void setPixels(ArrayList<ArrayList<Integer>> pixels)
    {
        this.pixels = pixels;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
