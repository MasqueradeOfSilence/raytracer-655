package com.evenstar.model;

import com.evenstar.model.physics.Hit;

public class UV
{
    private final Hit hit;
    private final double u;
    private final double v;

    // Calculates the UV from the hit point as soon as it is constructed.
    public UV(Hit hit)
    {
        this.hit = hit;
        double phi = Math.atan2(hit.getHitPoint().getZ(), hit.getHitPoint().getX());
        double theta = Math.asin(hit.getHitPoint().getY());
        this.u = 1 - (phi + Math.PI) / (2 * Math.PI);
        this.v = (theta + Math.PI / 2) / Math.PI;
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
