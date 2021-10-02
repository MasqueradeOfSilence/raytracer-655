package com.evenstar.model;

import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Pixel;

import java.util.ArrayList;

public class PPMImage
{
    private final int width;
    private final int height;
    private ArrayList<ArrayList<Pixel>> pixels = new ArrayList<>();
    private final String beginningPartOfPPM;

    private Color defaultPink()
    {
        return new Color(255, 192, 203);
    }

    private void prefillPixels(int dimension)
    {
        for (int i = 0; i < dimension; i++)
        {
            this.pixels.add(i, new ArrayList<>());
            for (int j = 0; j < dimension; j++)
            {
                this.pixels.get(i).add(j, new Pixel(defaultPink()));
            }
        }
    }

    public String getBeginningPartOfPPM() {
        return beginningPartOfPPM;
    }

    private int antialias(int dimension)
    {
        return dimension / 2;
    }

    public PPMImage(int width, int height)
    {
        this.width = width;
        this.height = height;
        this.prefillPixels(width);
        beginningPartOfPPM = "P3" + "\n" + antialias(width) + "\n" + antialias(height) + "\n" + "255" + "\n";
    }

    public void addPixel(Pixel pixel, int i, int j)
    {
        this.pixels.get(i).set(j, pixel);
    }

    public ArrayList<ArrayList<Pixel>> getPixels()
    {
        return pixels;
    }

    public void setPixels(ArrayList<ArrayList<Pixel>> pixels)
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
