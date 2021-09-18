package com.evenstar.model.textures;

import com.evenstar.model.vectors.Vector3D;

import java.util.Objects;

public class Diffuse implements Material
{
    private final Vector3D xyz;
    private final Vector3D specularHighlight;
    private final int phongConstant;

    public Diffuse(Vector3D xyz, Vector3D specularHighlight, int phongConstant)
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

    @Override
    public String toString()
    {
        return "Diffuse{" +
                "xyz=" + xyz +
                ", specularHighlight=" + specularHighlight +
                ", phongConstant=" + phongConstant +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Diffuse diffuse = (Diffuse) o;
        return phongConstant == diffuse.phongConstant &&
                Objects.equals(xyz, diffuse.xyz) &&
                Objects.equals(specularHighlight, diffuse.specularHighlight);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(xyz, specularHighlight, phongConstant);
    }
}
