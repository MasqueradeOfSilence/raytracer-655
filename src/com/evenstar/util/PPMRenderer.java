package com.evenstar.util;

import com.evenstar.model.Color;
import com.evenstar.model.PPMImage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class PPMRenderer
{
    private void writeImageToFile(PPMImage image, String name)
    {
        assert image.getPixels().size() > 0;
        try
        {
            FileWriter imageFileWriter = new FileWriter(name);
            imageFileWriter.write(image.getBeginningPartOfPPM());
            for (int i = 0; i < image.getPixels().size(); i++)
            {
                for (int j = 0; j < image.getPixels().get(i).size(); j++)
                {
                    imageFileWriter.write(image.getPixels().get(i).get(j) + " ");
                }
                imageFileWriter.write("\n");
            }
            imageFileWriter.close();
            System.out.println("Wrote image");
        }
        catch (IOException e)
        {
            System.out.println("Oops! An error occurred with file I/O. Lmao");
            e.printStackTrace();
        }
    }

    private String computeProgress(int height, int currentScanLine)
    {
        return Math.round(((double)(height - 1 - currentScanLine) / (height - 1)) * 100) + "%";
    }

    private Color translateColorFrom1To255Scale(Color color)
    {
        int int_r = (int)(255.999 * color.r());
        int int_g = (int)(255.999 * color.g());
        int int_b = (int)(255.999 * color.b());
        return new Color(int_r, int_g, int_b);
    }

    public void generateDefaultGradientImage()
    {
        int width = 256;
        int height = 256;
        PPMImage image = new PPMImage(width, height);
        ArrayList<ArrayList<Integer>> pixels = new ArrayList<>();
        // This is just the default algorithm for the computer graphics "hello world" image.
        for (int j = image.getHeight() - 1; j >= 0; j--)
        {
            System.out.println("Progress: " + computeProgress(image.getHeight(), j));
            for (int i = 0; i < image.getWidth(); i++)
            {
                double r = (double)i / (image.getWidth() - 1);
                double g = (double)j / (image.getHeight() - 1);
                double b = 0.25;
                Color pixelColor = new Color(r, g, b);
                Color newColor = translateColorFrom1To255Scale(pixelColor);
                pixels.add(new ArrayList<>(Arrays.asList((int)newColor.r(), (int)newColor.g(), (int)newColor.b())));
            }
        }
        image.setPixels(pixels);
        this.writeImageToFile(image, "hello.ppm");
    }
}
