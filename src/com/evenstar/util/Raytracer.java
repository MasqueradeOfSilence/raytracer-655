package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.util.physics.Intersector;
import com.evenstar.util.physics.Lighter;
import com.evenstar.util.physics.Shadower;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.vectors.*;

import java.util.ArrayList;
import java.util.Collections;

public class Raytracer
{
    private final PPMRenderer ppmRenderer;
    private Scene scene;
    private final Lighter lighter;
    private final Shadower shadower;
    private final Intersector intersector;

    public Raytracer(Scene scene)
    {
        this.ppmRenderer = new PPMRenderer();
        this.lighter = new Lighter();
        this.shadower = new Shadower();
        this.intersector = new Intersector();
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

    private boolean allMisses(ArrayList<Double> distancesOfShapes)
    {
        boolean allMisses = true;
        for (Double distancesOfShape : distancesOfShapes)
        {
            if (distancesOfShape != Constants.NO_INTERSECTION) {
                allMisses = false;
                break;
            }
        }
        return allMisses;
    }

    private Pixel getColorOfClosestShape(ArrayList<Double> distancesOfShapes, ArrayList<Shape> shapes, Ray ray)
    {
        int minDistance = distancesOfShapes.indexOf(Collections.min(distancesOfShapes));
        Shape closestShape = shapes.get(minDistance);
        if (ClassIdentifier.isSphere(closestShape))
        {
            Sphere sphere = (Sphere) closestShape;
            if (ClassIdentifier.isDiffuse(sphere.getMaterial()))
            {
                if (this.shadower.isInShadow(this.scene, sphere))
                {
                    return new Pixel(this.scene.getAmbientLight().getLightColor());
                }
                // will eventually iterate through all lights here
                return this.lighter.getFinalColorDiffuse(new Color(sphere.getMaterial().getVector()), sphere.getHitPair().getNormal(),
                        scene.getDirectionalLight(), (Diffuse)sphere.getMaterial(), sphere.getHitPair().getHitPoint().getVector(), ray, this.scene);
            }
            // specular material -- will need reflections
            return new Pixel(sphere.getMaterial().getVector());
        }
        else if (ClassIdentifier.isTriangle(closestShape))
        {
            Triangle triangle = (Triangle) closestShape;
            triangle = this.intersector.setHitPointTriangle(ray, triangle);
            if (ClassIdentifier.isDiffuse(triangle.getMaterial()))
            {
                // Checking if individual pixel is in shadow
                if (this.shadower.isInShadow(this.scene, triangle))
                {
                    return new Pixel(this.scene.getAmbientLight().getLightColor());
                }
                return this.lighter.getFinalColorDiffuse(new Color(triangle.getMaterial().getVector()), triangle.getHitPair().getNormal(),
                        scene.getDirectionalLight(), (Diffuse)triangle.getMaterial(), triangle.getHitPair().getHitPoint().getVector(), ray, this.scene);
            }
            // specular material -- will need reflections
            return new Pixel(triangle.getMaterial().getVector());
        }
        else
        {
            System.out.println("Shape not implemented yet. Returning background color");
            return new Pixel(this.scene.getBackgroundColor());
        }
    }

    private Pixel colorPixel(Ray ray, Color backgroundColor)
    {
        ArrayList<Shape> shapes = this.scene.getShapes();
        ArrayList<Double> distancesOfShapes = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++)
        {
            Shape currentShape = shapes.get(i);
            double intersectionDistance = this.intersector.intersects(ray, currentShape);
            if (ClassIdentifier.isSphere(currentShape))
            {
                shapes.set(i, this.intersector.setHitPointSphere(intersectionDistance, ray, (Sphere) currentShape));
            }
//            intersectionDistance = Math.abs(intersectionDistance);
            distancesOfShapes.add(intersectionDistance);
        }
        assert (shapes.size() == distancesOfShapes.size());
        if (allMisses(distancesOfShapes))
        {
            return new Pixel(backgroundColor);
        }
        else
        {
            return getColorOfClosestShape(distancesOfShapes, shapes, ray);
        }
    }

    private double computeDistanceToImagePlane(double fov)
    {
        return 1 / Math.tan(Math.toRadians(fov));
    }

//    private Ray buildRay(int i, int j, int dimension, Camera camera)
//    {
//        // Modifications to make it the right orientation
//        double x = -(((2 * (j + .5)) / dimension) - 1);
//        double y = ((2 * (i + .5)) / dimension) - 1;
////        double x = ((2 * (i + .5)) / dimension) - 1;
////        double y = ((2 * (j + .5)) / dimension) - 1;
//        double z = (computeDistanceToImagePlane(camera.getFieldOfView()));
//        Direction rayDirection = new Direction(x, y, z);
//        rayDirection.getVector().normalize();
//        return new Ray(camera.getLookFrom(), rayDirection);
//    }

    public Ray buildRay(int i, int j, int dimension, Camera camera)
    {
        double x = ((2 * (i + .5)) / dimension) - 1;
        double y = 1 - ((2 * (j + .5)) / dimension);
        double z = computeDistanceToImagePlane(camera.getFieldOfView());
        Direction rayDirection = new Direction(x, y, z);
        Point rayOrigin = camera.getLookFrom();
        return new Ray(rayOrigin, rayDirection);
    }

    private PPMImage shootRayAtEachPixelAndLightIt(int dimension, PPMImage image)
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
        renderedImage = shootRayAtEachPixelAndLightIt(dimension, renderedImage);
        return renderedImage;
    }
}
