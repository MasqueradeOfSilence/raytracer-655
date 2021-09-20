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

    private boolean triangleIntersection(Ray ray, Triangle triangle)
    {
        return false;
    }

    private boolean collideWithSphere(Vector3D rayToSphere, Sphere sphere)
    {
        System.out.println("The norm: " + rayToSphere.length());
        return rayToSphere.length() <= sphere.getRadius();
    }

    // incorrect -- redo
    private boolean sphereIntersection(Ray ray, Sphere sphere)
    {
        // Take vector between camera origin and sphere. Make sure it's in the right direction. If not, it's behind the camera.
        Vector3D cameraToSphere = VectorOperations.subtractVectors(sphere.getCenter().getVector(),
                ray.getOrigin().getVector());
        double lengthOfProjectionOntoRay = VectorOperations.dotProduct(cameraToSphere, ray.getDirection().getVector());
        if (isNegative(lengthOfProjectionOntoRay))
        {
            // Wrong direction
            return false;
        }
        // Take vector between ray and sphere
        Vector3D rayToSphere = VectorOperations.subtractVectors(VectorOperations.multiplyByScalar(
                ray.getDirection().getVector(), lengthOfProjectionOntoRay), cameraToSphere);
        if (collideWithSphere(rayToSphere, sphere))
        {
            System.out.println("Sphere collision");
        }
        return false;
    }

    private boolean intersects(Ray ray, Shape shape)
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
            if (intersects(ray, currentShape))
            {
                intersectedShapes.add(currentShape);
            }
        }
        if (intersectedShapes.size() == 0)
        {
            return new Pixel(backgroundColor);
        }
        Shape closestShape = intersectedShapes.get(0);
        for (int i = 0; i < intersectedShapes.size(); i++)
        {

        }
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
