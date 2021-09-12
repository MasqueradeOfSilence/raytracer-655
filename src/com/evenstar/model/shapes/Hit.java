package com.evenstar.model.shapes;

import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;

public class Hit
{
    private final Point whereHit;
    private final Vector3D normal;
    private final double t;

    public Hit(Point whereHit, Vector3D normal, double t)
    {
        this.whereHit = whereHit;
        this.normal = normal;
        this.t = t;
    }

    public Point getWhereHit()
    {
        return whereHit;
    }

    public Vector3D getNormal()
    {
        return normal;
    }

    public double getT()
    {
        return t;
    }
}
