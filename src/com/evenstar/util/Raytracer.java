package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.vectors.*;

import java.util.ArrayList;
import java.util.Collections;

public class Raytracer
{
    private final PPMRenderer ppmRenderer;
    private Scene scene;

    public Raytracer(Scene scene)
    {
        ppmRenderer = new PPMRenderer();
        this.scene = scene;
    }

    public void render(int dimensions, String fileName)
    {
        // antialiasing logic will double dimensions here; i.e. antialias(dimensions)
        PPMImage image = this.raytrace(dimensions);
        ppmRenderer.writeImageToFile(image, getFileName(fileName));
    }

    private String getFileName(String fileName)
    {
        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
        return "output/" + fileName.split("\\.")[0] + ".ppm";
    }

    private boolean isNegative(double number)
    {
        return number < 0.00001;
    }

    private double triangleIntersection(Ray ray, Triangle triangle)
    {
        return -1.0;
    }

    private double sphereIntersection(Ray ray, Sphere sphere)
    {
        Vector3D originToCenter = VectorOperations.subtractVectors(ray.getOrigin().getVector(),
                sphere.getCenter().getVector());
        double a = VectorOperations.dotProduct(ray.getDirection().getVector(),
                ray.getDirection().getVector());
        double b = 2.0 * VectorOperations.dotProduct(originToCenter, ray.getDirection().getVector());
        double c = VectorOperations.dotProduct(originToCenter, originToCenter) -
                (sphere.getRadius() * sphere.getRadius());
        double discriminant = Math.pow(b, 2) - (4 * a * c);
        if (discriminant < 0)
        {
            return -1.0;
        }
        else
        {
            return (-b - Math.sqrt(discriminant)) / (2.0 * a);
        }
    }

    private double intersects(Ray ray, Shape shape)
    {
        if (shape.getClass().toString().contains("Triangle"))
        {
            return this.triangleIntersection(ray, (Triangle) shape);
        }
        else
        {
            return this.sphereIntersection(ray, (Sphere) shape);
        }
    }

    private boolean allMisses(ArrayList<Double> distancesOfShapes)
    {
        boolean allMisses = true;
        for (int i = 0; i < distancesOfShapes.size(); i++)
        {
            if (distancesOfShapes.get(i) != -1.0)
            {
                allMisses = false;
            }
        }
        return allMisses;
    }

    private Pixel getColorOfClosestShape(ArrayList<Double> distancesOfShapes, ArrayList<Shape> shapes)
    {
        int minDistance = distancesOfShapes.indexOf(Collections.min(distancesOfShapes));
        Shape closestShape = shapes.get(minDistance);
        if (closestShape.getClass().toString().contains("Sphere"))
        {
            Sphere sphere = (Sphere) closestShape;
            return new Pixel(sphere.getMaterial().getVector());
        }
        else if (closestShape.getClass().toString().contains("Triangle"))
        {
            Triangle triangle = (Triangle) closestShape;
            return new Pixel(triangle.getMaterial().getVector());
        }
        else
        {
            // Not implemented shape
            return new Pixel(this.scene.getBackgroundColor());
        }
    }

    private Pixel colorPixel(Ray ray, Color backgroundColor)
    {
        ArrayList<Shape> shapes = this.scene.getShapes();
        ArrayList<Double> distancesOfShapes = new ArrayList<>();
        for (Shape currentShape : shapes)
        {
            double intersectionDistance = intersects(ray, currentShape);
            distancesOfShapes.add(intersectionDistance);
        }
        assert (shapes.size() == distancesOfShapes.size());
        if (allMisses(distancesOfShapes))
        {
            return new Pixel(backgroundColor);
        }
        else
        {
            return getColorOfClosestShape(distancesOfShapes, shapes);
        }
    }

    private double computeDistanceToImagePlane(double fov)
    {
        return 1 / Math.tan(Math.toRadians(fov));
    }

    private Ray buildRay(int i, int j, int dimension, Camera camera)
    {
        double x = ((2 * (i + .5)) / dimension) - 1;
        double y = ((2 * (j + .5)) / dimension) - 1;
        double z = (computeDistanceToImagePlane(camera.getFieldOfView()));
        Direction rayDirection = new Direction(x, y, z);
        rayDirection.getVector().normalize();
        return new Ray(camera.getLookFrom(), rayDirection);
    }

    private PPMImage shootRayAtEachPixelAndLight(int dimension, PPMImage image)
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
     * @param dimension: how big our image will be. Can double for antialiasing
     */
    private PPMImage raytrace(int dimension)
    {
        // with antialiasing, look here
        PPMImage renderedImage = new PPMImage(dimension, dimension);
        renderedImage = shootRayAtEachPixelAndLight(dimension, renderedImage);
        return renderedImage;
    }
}
