package com.evenstar.util;

import com.evenstar.model.Ray;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;

public final class MathCalculations
{
    public static double sphereHitByRay(Sphere sphere, Ray ray)
    {
        Vector3D originToCenter = VectorOperations.subtractVectors(ray.getOrigin().getVector(), sphere.getCenter().getVector());
        double a = ray.getDirection().getVector().lengthSquared();
        double halfB = VectorOperations.dotProduct(originToCenter, ray.getDirection().getVector());
        double c = originToCenter.lengthSquared() - (sphere.getRadius() * sphere.getRadius());
        double discriminant = (halfB * halfB) - (a * c);
        if (discriminant < 0)
        {
            return -1.0;
        }
        else
        {
            return (-halfB - Math.sqrt(discriminant)) / a;
        }
    }
}
