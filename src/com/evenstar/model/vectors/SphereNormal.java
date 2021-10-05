package com.evenstar.model.vectors;

public class SphereNormal implements Normal
{
    private Vector3D normal;

    public SphereNormal(Point hitPoint, Point sphereCenter)
    {
        this.normal = VectorOperations.subtractVectors(hitPoint.getVector(), sphereCenter.getVector());
        this.normal.normalize();
    }

    public void makeNegative()
    {
        this.normal = new Vector3D(-this.normal.getX(), -this.normal.getY(), -this.normal.getZ());
    }

    @Override
    public Vector3D getVector()
    {
        return normal;
    }
}
