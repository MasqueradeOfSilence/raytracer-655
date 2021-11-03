package com.evenstar.model.textures;

import com.evenstar.model.vectors.Vector3D;

public class Emissive implements Material
{
    // For area lights
    private final Vector3D color;

    public Emissive(Vector3D color)
    {
        this.color = color;
    }

    @Override
    public Vector3D getVector()
    {
        return color;
    }
}
