package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.Hit;
import com.evenstar.model.textures.Glass;
import com.evenstar.util.physics.*;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.vectors.*;

import java.util.ArrayList;

public class Raytracer
{
    private final PPMRenderer ppmRenderer;
    private Scene scene;
    private final Lighter lighter;
    private final Shadower shadower;
    private final Intersector intersector;
    private final Reflector reflector;
    private final Refractor refractor;

    public Raytracer(Scene scene)
    {
        this.ppmRenderer = new PPMRenderer();
        this.lighter = new Lighter();
        this.shadower = new Shadower(scene);
        this.intersector = new Intersector(scene);
        this.reflector = new Reflector();
        this.refractor = new Refractor();
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

    public Color colorShape(Hit hit, Ray ray)
    {
        Shape shape = hit.getCorrespondingShape();
        if (ClassIdentifier.isSphere(shape))
        {
            Sphere sphere = (Sphere) shape;
            SphereNormal sphereNormal = new SphereNormal(hit.getHitPoint(), sphere.getCenter());
            if (ClassIdentifier.isDiffuse(sphere.getMaterial()))
            {
                if (this.shadower.isInShadow(this.scene, hit))
                {
                    // Tint the ambient light
                    Vector3D combined = VectorOperations.multiplyVectors(sphere.getMaterial().getVector(),
                            scene.getAmbientLight().getLightColor().getVector());
                    return new Color(combined);
                }
                return this.lighter.getFinalColor(new Color(sphere.getMaterial().getVector()),
                        sphereNormal.getVector(), this.scene.getDirectionalLight(), (Diffuse)sphere.getMaterial(),
                        hit.getHitPoint().getVector(), ray, this.scene);
            }
            else if (ClassIdentifier.isReflective(sphere.getMaterial()))
            {
                return this.reflector.getReflectionColor(ray, sphereNormal, hit.getHitPoint(), sphere, this.scene,
                        this.intersector, this);
            }
            else if (ClassIdentifier.isGlass(sphere.getMaterial()))
            {
                Glass glass = (Glass) sphere.getMaterial();
                return this.refractor.getReflectedAndRefractedColor(ray, sphereNormal, glass, hit.getHitPoint(),
                        this.intersector, this.scene, this, sphere, this.reflector);
            }
            return new Color(sphere.getMaterial().getVector());
        }
        else if (ClassIdentifier.isTriangle(shape))
        {
            Triangle triangle = (Triangle) shape;
            if (ClassIdentifier.isDiffuse(triangle.getMaterial()))
            {
                if (this.shadower.isInShadow(this.scene, hit))
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

    public Hit getClosestHit(ArrayList<Hit> hits)
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

    public boolean nothingHit(ArrayList<Hit> distancesFromRayToShapes)
    {
        return distancesFromRayToShapes.size() == 0;
    }

    public Pixel computeColorOfPixel(Ray ray, Color backgroundColor)
    {
        ArrayList<Hit> rayShapeHits = this.intersector.computeRayShapeHits(ray, this.scene.getShapes(), this.scene);
        if (this.nothingHit(rayShapeHits))
        {
            return new Pixel(backgroundColor);
        }
        Hit closest = getClosestHit(rayShapeHits);
        return new Pixel(this.colorShape(closest, ray));
    }

    public double computeDistanceToImagePlane(double fov)
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
        System.out.println("Performed " + Globals.numIntersectionTests + " intersection tests.");
        return image;
    }

    private int antialiasDimension(int dimension)
    {
        return dimension * 2;
    }

    /**
     * Raytrace only currently works for square pixels
     * @param dimension: how big our image will be. Can double for antialiasing
     */
    private PPMImage raytrace(int dimension)
    {
//        this.scene.getDirectionalLight().turnOff();
//        this.scene.getMiscellaneousLights().get(0).turnOff();
        dimension = antialiasDimension(dimension);
        PPMImage renderedImage = new PPMImage(dimension, dimension);
        renderedImage = shootRayAtEachPixelAndLightIt(dimension, renderedImage);
        AcceleratedRaytracer acceleratedRaytracer = new AcceleratedRaytracer();
        //renderedImage = acceleratedRaytracer.drawBoundingBoxes(renderedImage, this.scene, dimension, this);
        return renderedImage;
    }
}
