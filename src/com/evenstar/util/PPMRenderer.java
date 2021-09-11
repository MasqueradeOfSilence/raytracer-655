package com.evenstar.util;

import com.evenstar.model.*;
import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;

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
        Color firstColor = new Color(new Color(1.0, 1.0, 1.0).getRgb().multiplyByScalar(1.0 - t));
        Color secondColor = new Color(new Color(0.5, 0.7, 1.0).getRgb().multiplyByScalar(t));
        return new Color(firstColor.getRgb().addSecondVector(secondColor.getRgb()));
    }

    public Direction computeRayDirectionBasedOnCamera(Camera camera, double u, double v)
    {
        Point point1 = new Point(camera.getHorizontal().getDirectionVector().multiplyByScalar(u));
        Point point2 = new Point(camera.getVertical().getDirectionVector().multiplyByScalar(v));
        Point point3 = new Point(camera.getLowerLeftCorner().getCoordinates().addSecondVector(point1.getCoordinates()));
        Point point4 = new Point(point3.getCoordinates().addSecondVector(point2.getCoordinates()));
        Direction point5 = new Direction(point4.getCoordinates().subtractSecondVector(camera.getOrigin().getCoordinates()));
        return new Direction(point5);
    }


    public void generateBlueSkyImage(PPMImage blueSkyImage)
    {
        double viewportHeight = 2.0;
        double viewportWidth = blueSkyImage.getAspectRatio() * viewportHeight;
        double focalLength = 1.0;
        Point origin = new Point(0, 0, 0);
        Direction horizontal = new Direction(viewportWidth, 0, 0);
        Direction vertical = new Direction(0, viewportHeight, 0);
        Camera camera = new Camera(blueSkyImage.getAspectRatio(), viewportHeight, viewportWidth, focalLength,
                origin, horizontal, vertical);
        ArrayList<ArrayList<Integer>> pixels = new ArrayList<>();
        for (int j = blueSkyImage.getHeight() - 1; j >= 0; j--) {
            System.out.println("Progress: " + computeProgress(blueSkyImage.getHeight(), j));
            for (int i = 0; i < blueSkyImage.getWidth(); i++) {
                double u = (double) i / (blueSkyImage.getWidth() - 1);
                double v = (double) j / (blueSkyImage.getHeight() - 1);
                Ray ray = new Ray(camera.getOrigin(), computeRayDirectionBasedOnCamera(camera, u, v));
                Color pixelColor = skyColor(ray);
                Color newColor = translateColorFrom1To255Scale(pixelColor);
                pixels.add(new ArrayList<>(Arrays.asList((int) newColor.r(), (int) newColor.g(), (int) newColor.b())));
            }
        }
        blueSkyImage.setPixels(pixels);
        this.writeImageToFile(blueSkyImage, "sky.ppm");
    }
}
