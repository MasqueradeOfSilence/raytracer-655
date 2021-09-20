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
            System.out.println("nope!");
            return -1.0;
        }
        else
        {
            System.out.println("Intersected a sphere!");
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

    private Pixel colorPixel(Ray ray, Color backgroundColor)
    {
        // ray-shape intersection algorithm goes here
        ArrayList<Shape> shapes = this.scene.getShapes();
        ArrayList<Shape> intersectedShapes = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++)
        {
            Shape currentShape = shapes.get(i);
            if (intersects(ray, currentShape) != -1.0)
            {
                intersectedShapes.add(currentShape);
            }
        }
        if (intersectedShapes.size() == 0)
        {
            return new Pixel(backgroundColor);
        }
        else
        {
            // remove this. just a test
            return new Pixel(new Color(0, 0, 255));
        }
//        Shape closestShape = intersectedShapes.get(0);
//        for (int i = 0; i < intersectedShapes.size(); i++)
//        {
//
//        }
//        return new Pixel(backgroundColor);
    }

    private double computeDistanceToImagePlane(double fov)
    {
        return 1 / Math.tan(Math.toRadians(fov));
    }

    private Ray buildRay(int i, int j, int dimension, Camera camera)
    {
//        Vector3D pixelTemp = new Vector3D(i, j, 0);
//        Direction rayDirection = new Direction(VectorOperations.subtractVectors(pixelTemp, camera.getLookFrom().getVector()));
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
