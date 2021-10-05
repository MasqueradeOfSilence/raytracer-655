package com.evenstar.model.textures;

import com.evenstar.model.vectors.Vector3D;

public class Glass implements Material
{
    private final Vector3D xyz;
    // IoR for glass is usually around 1.5 - 1.7
    private final double indexOfRefraction = 1.5;

    public Glass(Vector3D xyz)
    {
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
}
