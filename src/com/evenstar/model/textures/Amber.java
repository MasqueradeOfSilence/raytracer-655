package com.evenstar.model.textures;

import com.evenstar.model.vectors.Vector3D;

public class Amber extends Glass
{
    private final Vector3D xyz;
    private final double indexOfRefraction = 1.55;

    public Amber(Vector3D xyz)
    {
        super(xyz);
        this.xyz = xyz;
    }

    public double getIndexOfRefraction()
    {
        return indexOfRefraction;
    }

    @Override
    public Vector3D getVector()
    {
        return xyz;
    }
    // A translucent material
}
