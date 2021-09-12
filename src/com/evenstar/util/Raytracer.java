package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;

import java.util.ArrayList;
import java.util.Arrays;

public class Raytracer
{
    private final PPMRenderer ppmRenderer;

    public Raytracer()
    {
        ppmRenderer = new PPMRenderer();
    }

    public void raytraceHelloWorldImage()
    {
        this.generateDefaultGradientImage();
    }

    public void raytraceBlueSkyImage()
    {
        PPMImage blueSkyImage = new PPMImage(400, 225);
        this.generateBlueSkyImage(blueSkyImage);
    }

    private void generateDefaultGradientImage()
    {
        int width = 256;
        int height = 256;
        PPMImage image = new PPMImage(width, height);
        ArrayList<ArrayList<Integer>> pixels = new ArrayList<>();
        // This is just the default algorithm for the computer graphics "hello world" image.
        for (int j = image.getHeight() - 1; j >= 0; j--)
        {
            System.out.println("Progress: " + ppmRenderer.computeProgress(image.getHeight(), j));
            for (int i = 0; i < image.getWidth(); i++)
            {
                double r = (double)i / (image.getWidth() - 1);
                double g = (double)j / (image.getHeight() - 1);
                double b = 0.25;
                Color pixelColor = new Color(r, g, b);
                Color newColor = ppmRenderer.translateColorFrom1To255Scale(pixelColor);
                pixels.add(new ArrayList<>(Arrays.asList((int)newColor.r(), (int)newColor.g(), (int)newColor.b())));
            }
        }
        image.setPixels(pixels);
        ppmRenderer.writeImageToFile(image, "hello.ppm");
    }

    private void generateBlueSkyImage(PPMImage blueSkyImage)
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
            System.out.println("Progress: " + ppmRenderer.computeProgress(blueSkyImage.getHeight(), j));
            for (int i = 0; i < blueSkyImage.getWidth(); i++) {
                double u = (double) i / (blueSkyImage.getWidth() - 1);
                double v = (double) j / (blueSkyImage.getHeight() - 1);
                Ray ray = new Ray(camera.getOrigin(), ppmRenderer.computeRayDirectionBasedOnCamera(camera, u, v));
                Color pixelColor = ppmRenderer.skyColorWithSphere(ray);
                Color newColor = ppmRenderer.translateColorFrom1To255Scale(pixelColor);
                pixels.add(new ArrayList<>(Arrays.asList((int) newColor.r(), (int) newColor.g(), (int) newColor.b())));
            }
        }
        blueSkyImage.setPixels(pixels);
        ppmRenderer.writeImageToFile(blueSkyImage, "sky.ppm");
    }
}
