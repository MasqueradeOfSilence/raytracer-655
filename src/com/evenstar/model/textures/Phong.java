package com.evenstar.model.textures;

import com.evenstar.model.vectors.Vector3D;

public class Phong implements Material
{
    // Phong is GLOSSY
    private final int n;
    private final Vector3D xyz;
    private final double specularCoefficient;
    private final Vector3D specularHighlight;
    private final int phongConstant;

    public Phong(int n, Vector3D xyz, double specularCoefficient, Vector3D specularHighlight, int phongConstant)
    {
        this.n = n;
        this.xyz = xyz;
        this.specularCoefficient = specularCoefficient;
        this.specularHighlight = specularHighlight;
        this.phongConstant = phongConstant;
    }

    public int getN()
    {
        return n;
    }

    public double getSpecularCoefficient()
    {
        return specularCoefficient;
    }

    public Vector3D getSpecularHighlight()
    {
        return specularHighlight;
    }

    public int getPhongConstant()
    {
        return phongConstant;
    }

    @Override
    public Vector3D getVector()
    {
        return xyz;
    }
}
