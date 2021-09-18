package com.evenstar.model.textures;

import com.evenstar.model.vectors.Vector3D;

public class Reflective implements Material
{
    private final Vector3D xyz;

    public Reflective(Vector3D xyz)
    {
        this.xyz = xyz;
    }

    @Override
    public Vector3D getVector()
    {
        return xyz;
    }
}
