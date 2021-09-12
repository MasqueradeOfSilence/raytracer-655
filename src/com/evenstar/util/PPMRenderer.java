package com.evenstar.util;

import com.evenstar.model.*;
import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.VectorOperations;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

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
            System.out.println("Wrote image");
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

    public Color translateColorFrom1To255Scale(Color color)
    {
        int int_r = (int)(255.999 * color.r());
        int int_g = (int)(255.999 * color.g());
        int int_b = (int)(255.999 * color.b());
        return new Color(int_r, int_g, int_b);
    }

    public Color skyColor(Ray ray)
    {
        Direction unitDirection = ray.getDirection();
        // Create a gradient from blue to white
        double t = 0.5 * (unitDirection.getY() * 1.0);
        Color firstColor = new Color(VectorOperations.multiplyByScalar(new Color(1.0, 1.0, 1.0).getVector(), 1.0 - t));
        Color secondColor = new Color(VectorOperations.multiplyByScalar(new Color(0.5, 0.7, 1.0).getVector(), t));
        return new Color(VectorOperations.addVectors(firstColor.getVector(), secondColor.getVector()));
    }

    public Direction computeRayDirectionBasedOnCamera(Camera camera, double u, double v)
    {
        Point point1 = new Point(VectorOperations.multiplyByScalar(camera.getHorizontal().getVector(), u));
        Point point2 = new Point(VectorOperations.multiplyByScalar(camera.getVertical().getVector(), v));
        Point point3 = new Point(VectorOperations.addVectors(camera.getLowerLeftCorner().getVector(), point1.getVector()));
        Point point4 = new Point(VectorOperations.addVectors(point3.getVector(), point2.getVector()));
        return new Direction(VectorOperations.subtractVectors(point4.getVector(), camera.getOrigin().getVector()));
    }
}
