package com.evenstar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PPMRenderer
{
    public void generateDefaultGradientImage()
    {
        int width = 256;
        int height = 256;
        PPMImage image = new PPMImage(width, height);
        ArrayList<ArrayList<Integer>> pixels = new ArrayList<ArrayList<Integer>>();
        // This is just the default algorithm for the computer graphics "hello world" image.
        for (int j = height - 1; j >= 0; j--)
        {
            for (int i = 0; i < width; i++)
            {
                double r = (double)i / (width - 1);
                double g = (double)j / (height - 1);
                double b = 0.25;
                int int_r = (int)(255.999 * r);
                int int_g = (int)(255.999 * g);
                int int_b = (int)(255.999 * b);
                pixels.add(new ArrayList<Integer>(Arrays.asList(int_r, int_g, int_b)));
            }
        }
        image.setPixels(pixels);
        try
        {
            FileWriter imageFileWriter = new FileWriter("hello.ppm");
            imageFileWriter.write(image.getBeginningPartOfPPM());
            for (int i = 0; i < image.getPixels().size(); i++)
            {
                for (int j = 0; j < image.getPixels().get(i).size(); j++)
                {
                    imageFileWriter.write(String.valueOf(image.getPixels().get(i).get(j)) + " ");
                }
                imageFileWriter.write("\n");
            }
            imageFileWriter.close();
            System.out.println("Wrote image");
        }
        catch (IOException e)
        {
            System.out.println("Oops! An error occurred because life is pain");
            e.printStackTrace();
        }
    }
}
