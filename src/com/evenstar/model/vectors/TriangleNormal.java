package com.evenstar.model.vectors;

public class TriangleNormal implements Normal
{
    private final Vector3D normal;

    public TriangleNormal(Vector3D ab, Vector3D ac)
    {
        this.normal = VectorOperations.crossProduct(ab, ac);
        this.normal.normalize();
    }

    @Override
    public Vector3D getVector()
    {
        return normal;
    }
}
