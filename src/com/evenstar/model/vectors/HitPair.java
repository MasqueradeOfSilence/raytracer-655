package com.evenstar.model.vectors;

public class HitPair
{
    private final Point hitPoint;
    private final Vector3D normal;

    public HitPair(Point hitPoint, Vector3D normal)
    {
        this.hitPoint = hitPoint;
        this.normal = normal;
    }

    public Point getHitPoint()
    {
        return hitPoint;
    }

    public Vector3D getNormal()
    {
        return normal;
    }
}
