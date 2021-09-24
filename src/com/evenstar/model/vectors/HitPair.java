package com.evenstar.model.vectors;

public class HitPair
{
    private final Vector3D hitPoint;
    private final Vector3D normal;

    public HitPair(Vector3D hitPoint, Vector3D normal)
    {
        this.hitPoint = hitPoint;
        this.normal = normal;
    }

    public Vector3D getHitPoint()
    {
        return hitPoint;
    }

    public Vector3D getNormal()
    {
        return normal;
    }
}
