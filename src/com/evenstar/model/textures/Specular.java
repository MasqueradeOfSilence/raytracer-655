package com.evenstar.model.textures;

import com.evenstar.model.vectors.Vector3D;

public class Specular implements Material
{
    private final Vector3D xyz;
    private final Vector3D specularHighlight;
    private final int phongConstant;

    public Specular(Vector3D xyz, Vector3D specularHighlight, int phongConstant)
    {
        this.xyz = xyz;
        this.specularHighlight = specularHighlight;
        this.phongConstant = phongConstant;
    }

    @Override
    public Vector3D getVector()
    {
        return xyz;
    }

    public Vector3D getSpecularHighlight()
    {
        return specularHighlight;
    }

    public int getPhongConstant()
    {
        return phongConstant;
    }
}
