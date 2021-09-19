package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Pixel;

public class Raytracer
{
    private final PPMRenderer ppmRenderer;

    public Raytracer()
    {
        ppmRenderer = new PPMRenderer();
    }

    public void render(Scene scene, int dimensions, String fileName)
    {
        // antialiasing logic will double dimensions here; i.e. antialias(dimensions)
        PPMImage image = this.raytrace(scene, dimensions);
        ppmRenderer.writeImageToFile(image, getFileName(fileName));
    }

    private String getFileName(String fileName)
    {
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        return "output/" + fileName.split("\\.")[0] + ".ppm";
    }

    private Pixel colorPixel(Ray ray, Color backgroundColor)
    {
        // ray-shape intersection algorithm goes here
        return new Pixel(backgroundColor);
    }

    private double computeDistanceToImagePlane(double fov)
    {
        return 1 / Math.tan(Math.toRadians(fov));
    }

    private Ray buildRay(int i, int j, int dimension, Camera camera)
    {
        double x = ((2 * i + .5) / dimension) - 1;
        double y = -((2 * j + .5) / dimension) - 1;
        double z = -(computeDistanceToImagePlane(camera.getFieldOfView()));
        Direction rayDirection = new Direction(x, y, z);
        rayDirection.getVector().normalize();
        return new Ray(camera.getLookFrom(), rayDirection);
    }

    private PPMImage shootRayAtEachPixelAndLight(Scene scene, int dimension, PPMImage image)
    {
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                Ray ray = buildRay(i, j, dimension, scene.getCamera());
                Pixel coloredPixel = colorPixel(ray, scene.getBackgroundColor());
                image.addPixel(coloredPixel, i, j);
            }
        }
        return image;
    }

    /**
     * Raytrace only currently works for square pixels
     * @param scene: our 3D scene represented in a data structure
     * @param dimension: how big our image will be. Can double for antialiasing
     */
    private PPMImage raytrace(Scene scene, int dimension)
    {
        // with antialiasing, look here
        PPMImage renderedImage = new PPMImage(dimension, dimension);
        renderedImage = shootRayAtEachPixelAndLight(scene, dimension, renderedImage);
        return renderedImage;
    }
}
