package com.evenstar.model.textures;

import com.evenstar.model.vectors.Vector3D;

import java.util.Objects;

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

    @Override
    public String toString()
    {
        return "Reflective{" +
                "xyz=" + xyz +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reflective that = (Reflective) o;
        return Objects.equals(xyz, that.xyz);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(xyz);
    }
}
