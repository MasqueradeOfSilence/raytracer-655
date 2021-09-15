package com.evenstar.model.shapes;

import com.evenstar.model.Ray;
import com.evenstar.model.textures.Material;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;

public class Sphere implements Shape
{
    private final Point center;
    private final double radius;
    private final Material material;

    public Sphere(Point center, double radius, Material material)
    {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    public Point getCenter()
    {
        return center;
    }

    public double getRadius()
    {
        return radius;
    }

    public Material getMaterial()
    {
        return material;
    }

    // TODO examine this function
    @Override
    public Hit hitByRay(Ray ray, double tMin, double tMax, Hit hit)
    {
        Vector3D originToCenter = VectorOperations.subtractVectors(ray.getOrigin().getVector(), this.center.getVector());
        double a = ray.getDirection().getVector().lengthSquared();
        double halfB = VectorOperations.dotProduct(originToCenter, ray.getDirection().getVector());
        double c = originToCenter.lengthSquared() - (this.radius * this.radius);
        double discriminant = (halfB * halfB) - (a * c);
        if (discriminant < 0)
        {
            return null;
        }
        double squareRootOfDiscriminant = Math.sqrt(discriminant);
        // Make sure it's in the right range
        double root = (-halfB - squareRootOfDiscriminant) / a;
        if (root < tMin || tMax < root)
        {
            root = (-halfB + squareRootOfDiscriminant) / a;
            if (root < tMin || tMax < root)
            {
                return null;
            }
        }
        // Return hit if successful; null otherwise.
        hit.setT(root);
        hit.setWhereHit(ray.at(hit.getT()));
        Vector3D outwardNormal = VectorOperations.divideByScalar((VectorOperations.subtractVectors(hit.getWhereHit().getVector(), this.center.getVector())), this.radius);
        hit.setFaceNormal(ray, outwardNormal);
        return hit;
    }
}
