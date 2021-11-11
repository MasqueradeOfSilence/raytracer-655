package com.evenstar.model;

import com.evenstar.model.physics.Hit;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;

public class UV
{
    private final Hit hit;
    private final double u;
    private final double v;

    // Calculates the UV from the hit point as soon as it is constructed.
    public UV(Hit hit, Sphere sphere)
    {
        this.hit = hit;
        Vector3D d = VectorOperations.subtractVectors(sphere.getCenter().getVector(), hit.getHitPoint().getVector());
        d = VectorOperations.normalize(d);
        double u = 0.5 + (Math.atan2(d.getZ(), d.getX()) / (2 * Math.PI));
        u = 0.5 - u;
        if (u < 0)
        {
            u = -u;
        }
        this.u = u;
        this.v = 0.5 - (Math.asin(d.getY()) / Math.PI);
    }

    public Hit getHit()
    {
        return hit;
    }

    public double getU()
    {
        return u;
    }

    public double getV()
    {
        return v;
    }
}
