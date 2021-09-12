package com.evenstar.model.vectors;

import java.util.Objects;

public class Vector3D
{
    private double x;
    private double y;
    private double z;

    public Vector3D(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // Copy constructor
    public Vector3D(Vector3D original)
    {
        this.x = original.x;
        this.y = original.y;
        this.z = original.z;
    }

    public Vector3D getUnitVector()
    {
        return new Vector3D(this.x / this.length(), this.y / this.length(), this.z / this.length());
    }

    public double length()
    {
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getZ()
    {
        return z;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vector3D vector3D = (Vector3D) o;
        return Double.compare(vector3D.x, x) == 0 &&
                Double.compare(vector3D.y, y) == 0 &&
                Double.compare(vector3D.z, z) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y, z);
    }
}
