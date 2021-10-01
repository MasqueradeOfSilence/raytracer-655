package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.Hit;
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

    private Color colorShape(Hit hit, Ray ray)
    {
        Shape shape = hit.getCorrespondingShape();
        if (ClassIdentifier.isSphere(shape))
        {
            Sphere sphere = (Sphere) shape;
            if (ClassIdentifier.isDiffuse(sphere.getMaterial()))
            {
                if (this.shadower.isInShadow(this.scene, sphere, hit))
                {
                    // Tint the ambient light
                    Vector3D combined = VectorOperations.multiplyVectors(sphere.getMaterial().getVector(),
                            scene.getAmbientLight().getLightColor().getVector());
                    return new Color(combined);
                }
                SphereNormal sphereNormal = new SphereNormal(hit.getHitPoint(), sphere.getCenter());
                return this.lighter.getFinalColor(new Color(sphere.getMaterial().getVector()),
                        sphereNormal.getVector(), this.scene.getDirectionalLight(), (Diffuse)sphere.getMaterial(),
                        hit.getHitPoint().getVector(), ray, this.scene);
            }
            else if (ClassIdentifier.isReflective(sphere.getMaterial()))
            {
                return new Color(VectorOperations.multiplyVectors(sphere.getMaterial().getVector(),
                        this.scene.getBackgroundColor().getVector()));
            }
            return new Color(sphere.getMaterial().getVector());
        }
        else if (ClassIdentifier.isTriangle(shape))
        {
            Triangle triangle = (Triangle) shape;
            if (ClassIdentifier.isDiffuse(triangle.getMaterial()))
            {
                if (this.shadower.isInShadow(this.scene, triangle, hit))
                {
                    Vector3D combined = VectorOperations.multiplyVectors(triangle.getMaterial().getVector(),
                            scene.getAmbientLight().getLightColor().getVector());
                    return new Color(combined);
                }
                Vector3D ab = VectorOperations.subtractVectors(triangle.getVertex2().getVector(),
                        triangle.getVertex1().getVector());
                Vector3D ac = VectorOperations.subtractVectors(triangle.getVertex3().getVector(),
                        triangle.getVertex1().getVector());
                TriangleNormal triangleNormal = new TriangleNormal(ab, ac);
                return this.lighter.getFinalColor(new Color(triangle.getMaterial().getVector()),
                        triangleNormal.getVector(), this.scene.getDirectionalLight(), (Diffuse)triangle.getMaterial(),
                        hit.getHitPoint().getVector(), ray, this.scene);
            }
            return new Color(triangle.getMaterial().getVector());
        }
        return new Color(0, 0, 0);
    }

    private Hit getClosestHit(ArrayList<Hit> hits)
    {
        assert (hits.size() > 0);
        Hit closestHit = hits.get(0);
        for (int i = 1; i < hits.size(); i++)
        {
            Hit currentHit = hits.get(i);
            if (currentHit.getDistanceToRay() < closestHit.getDistanceToRay())
            {
                closestHit = currentHit;
            }
        }
        return closestHit;
    }

    private boolean nothingHit(ArrayList<Hit> distancesFromRayToShapes)
    {
        return distancesFromRayToShapes.size() == 0;
    }

    public Pixel computeColorOfPixel(Ray ray, Color backgroundColor)
    {
        ArrayList<Hit> rayShapeHits = this.intersector.computeRayShapeHits(ray, this.scene.getShapes());
        if (this.nothingHit(rayShapeHits))
        {
            return new Pixel(backgroundColor);
        }
        Hit closest = getClosestHit(rayShapeHits);
        return new Pixel(this.colorShape(closest, ray));
    }

    private double computeDistanceToImagePlane(double fov)
    {
        double zoomOut = -.12;
        // Negative due to graphics standard.
        return -(1 / Math.tan(Math.toRadians(fov))) + zoomOut;
    }

    public Ray buildRay(int i, int j, int dimension, Camera camera)
    {
        double x = ((2 * (i + .5)) / dimension) - 1;
        double y = 1 - ((2 * (j + .5)) / dimension);
        double z = computeDistanceToImagePlane(camera.getFieldOfView());
        Direction rayDirection = new Direction(x, y, z);
        rayDirection.getVector().normalize();
        Point rayOrigin = camera.getLookFrom();
        return new Ray(rayOrigin, rayDirection);
    }

    private PPMImage shootRayAtEachPixelAndLightIt(int dimension, PPMImage image)
    {
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                // j and i must be switched due to how a PPM is structured
                Ray ray = buildRay(j, i, dimension, scene.getCamera());
                Pixel coloredPixel = this.computeColorOfPixel(ray, scene.getBackgroundColor());
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
