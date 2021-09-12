package com.evenstar.util;

import com.evenstar.model.Ray;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;

public final class MathCalculations
{
    public static boolean sphereHitByRay(Sphere sphere, Ray ray)
    {
        Vector3D originToCenter = VectorOperations.subtractVectors(ray.getOrigin().getVector(), sphere.getCenter().getVector());
        double a = VectorOperations.dotProduct(ray.getDirection().getVector(), ray.getDirection().getVector());
        double b = 2.0 * VectorOperations.dotProduct(originToCenter, ray.getDirection().getVector());
        double c = VectorOperations.dotProduct(originToCenter, originToCenter) - (sphere.getRadius() * sphere.getRadius());
        double discriminant = (b * b) - (4 * a * c);
        return discriminant > 0;
    }
}
