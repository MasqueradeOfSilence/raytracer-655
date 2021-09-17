package com.evenstar.util;

import com.evenstar.model.*;

import java.io.FileWriter;
import java.io.IOException;

public class PPMRenderer
{
    public void writeImageToFile(PPMImage image, String name)
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
        return Math.round(((double)(height - 1 - currentScanLine) / (height - 1)) * 100) + "%";
    }
}
