package com.evenstar.util;

import com.evenstar.model.*;
import com.evenstar.model.vectors.Pixel;

import java.io.FileWriter;
import java.io.IOException;

public class PPMRenderer
{
    private int convertTo255Scale(double number)
    {
        return (int)(number * 255);
    }

    private String antialiasedPixels(double averageX, double averageY, double averageZ)
    {
        int x = this.convertTo255Scale(averageX);
        int y = this.convertTo255Scale(averageY);
        int z = this.convertTo255Scale(averageZ);
        return x + " " + y + " " + z + "\n";
    }

    public void writeImageToFile(PPMImage image, String name)
    {
        assert image.getPixels().size() > 0;
        try
        {
            System.out.println("Name: " + name);
            FileWriter imageFileWriter = new FileWriter(name);
            imageFileWriter.write(image.getBeginningPartOfPPM());
            System.out.println("Writing image...");
            for (int i = 0; i < image.getPixels().size(); i += 2)
            {
                // System.out.println("Progress: " + computeProgress(image.getPixels().size(), i));
                for (int j = 0; j < image.getPixels().get(i).size(); j += 2)
                {
                    Pixel pixel1 = image.getPixels().get(i).get(j);
                    Pixel pixel2 = image.getPixels().get(i).get(j + 1);
                    Pixel pixel3 = image.getPixels().get(i + 1).get(j);
                    Pixel pixel4 = image.getPixels().get(i + 1).get(j + 1);
                    double averageX = (pixel1.getR() + pixel2.getR() + pixel3.getR() + pixel4.getR()) / 4;
                    double averageY = (pixel1.getG() + pixel2.getG() + pixel3.getG() + pixel4.getG()) / 4;
                    double averageZ = (pixel1.getB() + pixel2.getB() + pixel3.getB() + pixel4.getB()) / 4;
                    imageFileWriter.write(antialiasedPixels(averageX, averageY, averageZ));
                    //imageFileWriter.write(image.getPixels().get(i).get(j).convertAndPrintForPPM());
                }
                imageFileWriter.write("\n");
            }
            imageFileWriter.close();
            System.out.println("Wrote image " + name);
        }
        catch (IOException e)
        {
            System.out.println("Oops! An error occurred with file I/O. Lmao");
            e.printStackTrace();
        }
    }

    public String computeProgress(int height, int currentScanLine)
    {
        return (100 - Math.round(((double)(height - 1 - currentScanLine) / (height - 1)) * 100)) + "%";
    }
}
