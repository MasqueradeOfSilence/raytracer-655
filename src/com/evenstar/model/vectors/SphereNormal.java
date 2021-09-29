package com.evenstar.model.vectors;

public class SphereNormal implements Normal
{
    private final Vector3D normal;

    public SphereNormal(Point hitPoint, Point sphereCenter)
    {
        this.normal = VectorOperations.subtractVectors(hitPoint.getVector(), sphereCenter.getVector());
        this.normal.normalize();
    }

    @Override
    public Vector3D getVector()
    {
        return normal;
    }
}
