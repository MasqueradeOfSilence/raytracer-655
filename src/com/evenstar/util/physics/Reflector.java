package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.Hit;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.vectors.*;
import com.evenstar.util.Raytracer;

import java.util.ArrayList;

public class Reflector
{
    private Ray computeReflectionRay(Ray incidentRay, Normal normalAtIntersectionPoint, Point intersectionPoint)
    {
        double dotProduct = VectorOperations.dotProduct(incidentRay.getDirection().getVector(),
                normalAtIntersectionPoint.getVector()) * 2;
        Vector3D modified = VectorOperations.multiplyByScalar(normalAtIntersectionPoint.getVector(), dotProduct);
        Vector3D minuend = VectorOperations.subtractVectors(incidentRay.getDirection().getVector(), modified);
        return new Ray(intersectionPoint, new Direction(minuend));
    }

    public Color getReflectionColor(Ray incidentRay, Normal normalAtIntersectionPoint, Point intersectionPoint, Sphere sphere,
                         Scene scene, Intersector intersector, Raytracer raytracer, int i, int j)
    {
        // If I decide to make reflection recursive later, I'll start by looking here.
        Ray reflectionRay = computeReflectionRay(incidentRay, normalAtIntersectionPoint, intersectionPoint);
        Color reflectiveBackground = new Color(VectorOperations.multiplyVectors(sphere.getMaterial().getVector(),
                scene.getBackgroundColor().getVector()));
        ArrayList<Hit> rayShapeHits = intersector.computeRayShapeHits(reflectionRay, scene.getShapes(), scene);
        if (raytracer.nothingHit(rayShapeHits))
        {
            return reflectiveBackground;
        }
        Hit closest = raytracer.getClosestHit(rayShapeHits);
        return raytracer.colorShape(closest, reflectionRay, i, j);
    }
}
