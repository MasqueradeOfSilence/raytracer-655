package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.Hit;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.textures.Air;
import com.evenstar.model.textures.Glass;
import com.evenstar.model.vectors.*;
import com.evenstar.util.Raytracer;

import java.util.ArrayList;

public class Refractor
{
    private final Air air;

    public Refractor()
    {
        air = new Air();
    }

    private Direction computeRefractionDirection(Direction incomingRayDirection, SphereNormal normalAtIntersectionPoint,
                                                 Glass glass)
    {
        double clampedDotProduct = Math.min(1, Math.max(-1, VectorOperations.dotProduct(incomingRayDirection.getVector(),
                normalAtIntersectionPoint.getVector())));
        double ior1 = air.getIndexOfRefraction();
        double ior2 = glass.getIndexOfRefraction();
        if (clampedDotProduct < 0)
        {
            clampedDotProduct = -clampedDotProduct;
        }
        else
        {
            ior1 = glass.getIndexOfRefraction();
            ior2 = air.getIndexOfRefraction();
            normalAtIntersectionPoint.makeNegative();
        }
        double ratio = ior1 / ior2;
        double k = 1 - ratio * ratio * (1 - clampedDotProduct * clampedDotProduct);
        if (k < 0)
        {
            // No refraction
            return new Direction(0, 0, 0);
        }
        else
        {
            double innerFactor = ratio * clampedDotProduct - Math.sqrt(k);
            Vector3D secondHalf = VectorOperations.multiplyByScalar(normalAtIntersectionPoint.getVector(), innerFactor);
            Vector3D firstHalf = VectorOperations.multiplyByScalar(incomingRayDirection.getVector(), ratio);
            return new Direction(VectorOperations.addVectors(firstHalf, secondHalf));
        }
    }

    private Point getRefractionOrigin(Direction incomingRayDirection, SphereNormal normalAtIntersectionPoint,
                                      Point hitPoint)
    {
        boolean outside = VectorOperations.dotProduct(incomingRayDirection.getVector(),
                normalAtIntersectionPoint.getVector()) < 0;
        Vector3D bias = VectorOperations.multiplyByScalar(normalAtIntersectionPoint.getVector(), 0.001);
        if (outside)
        {
            return new Point(VectorOperations.subtractVectors(hitPoint.getVector(), bias));
        }
        else
        {
            return new Point(VectorOperations.addVectors(hitPoint.getVector(), bias));
        }
    }

    private Color computeRefractionColorOnly(Intersector intersector, Scene scene, Ray ray, Raytracer raytracer,
                                             Glass glass)
    {
        ArrayList<Hit> rayShapeHits = intersector.computeRayShapeHits(ray, scene.getShapes());
        Color reflectiveBackground = new Color(VectorOperations.multiplyVectors(glass.getVector(),
                scene.getBackgroundColor().getVector()));
        if (raytracer.nothingHit(rayShapeHits))
        {
            return reflectiveBackground;
        }
        Hit closest = raytracer.getClosestHit(rayShapeHits);
        return raytracer.colorShape(closest, ray);
    }

    private double fresnel(Direction incomingRayDirection, SphereNormal normalAtIntersectionPoint, Glass glass)
    {
        double clampedDotProduct = Math.min(1, Math.max(-1, VectorOperations.dotProduct(incomingRayDirection.getVector(),
                normalAtIntersectionPoint.getVector())));
        double ior1 = air.getIndexOfRefraction();
        double ior2 = glass.getIndexOfRefraction();
        if (clampedDotProduct > 0)
        {
            ior1 = glass.getIndexOfRefraction();
            ior2 = air.getIndexOfRefraction();
        }
        double sint = ior1 / ior2 * Math.sqrt(Math.max(0, 1 - clampedDotProduct * clampedDotProduct));
        if (sint >= 1)
        {
            return 1;
        }
        else
        {
            double cost = Math.sqrt(Math.max(0, 1 - sint * sint));
            clampedDotProduct = Math.abs(clampedDotProduct);
            double Rs = ((ior2 * clampedDotProduct) - (ior1 * cost)) / ((ior2 * clampedDotProduct) + (ior1 * cost));
            double Rp = ((ior1 * clampedDotProduct) - (ior2 * cost)) / ((ior1 * clampedDotProduct) + (ior2 * cost));
            return (Rs * Rs + Rp * Rp) / 2;
        }
    }

    public Color getReflectedAndRefractedColor(Ray ray, SphereNormal normal, Glass glass, Point hitPoint,
                                               Intersector intersector, Scene scene, Raytracer raytracer, Sphere sphere,
                                               Reflector reflector)
    {
        Direction refractionDirection = computeRefractionDirection(ray.getDirection(), normal, glass);
        Point refractionOrigin = getRefractionOrigin(ray.getDirection(), normal, hitPoint);
        Ray refractionRay = new Ray(refractionOrigin, refractionDirection);
        // if kr < 1 this is it, otherwise it's zero
        Color refractionColor = new Color(0, 0, 0);
        double kr = fresnel(ray.getDirection(), normal, glass);
        if (kr < 1)
        {
            refractionColor = computeRefractionColorOnly(intersector, scene, refractionRay, raytracer, glass);
        }
        Color reflectionColor = reflector.getReflectionColor(ray, normal, hitPoint, sphere, scene,
                        intersector, raytracer);
        Vector3D firstHalf = VectorOperations.multiplyByScalar(reflectionColor.getVector(), kr);
        Vector3D secondHalf = VectorOperations.multiplyByScalar(refractionColor.getVector(), (1 - kr));
        Vector3D sum = VectorOperations.addVectors(firstHalf, secondHalf);
        sum = new Vector3D(Math.max(Math.min(sum.getX(), 1), 0), Math.max(Math.min(sum.getY(), 1), 0),
                Math.max(Math.min(sum.getZ(), 1), 0));
        return new Color(sum);
    }
}
