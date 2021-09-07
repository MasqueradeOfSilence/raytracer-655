package com.evenstar.model;

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

    // Operations DO NOT affect the vector itself
    public Vector3D addSecondVector(Vector3D secondVector)
    {
        Vector3D copy = new Vector3D(this);
        copy.x += secondVector.x;
        copy.y += secondVector.y;
        copy.z += secondVector.z;
        return copy;
    }

    public Vector3D subtractSecondVector(Vector3D secondVector)
    {
        Vector3D copy = new Vector3D(this);
        copy.x -= secondVector.x;
        copy.y -= secondVector.y;
        copy.z -= secondVector.z;
        return copy;
    }

    public double dotWithSecondVector(Vector3D secondVector)
    {
        return this.x * secondVector.x
                + this.y * secondVector.y
                + this.z * secondVector.z;
    }

    public Vector3D crossWithSecondVector(Vector3D secondVector)
    {
        Vector3D copy = new Vector3D(this);
        double new_x = this.y * secondVector.z - this.z * secondVector.y;
        double new_y = this.z * secondVector.x - this.x * secondVector.z;
        double new_z = this.x * secondVector.y - this.y * secondVector.x;
        copy.x = new_x;
        copy.y = new_y;
        copy.z = new_z;
        return copy;
    }

    public Vector3D multiplyByScalar(double scalar)
    {
        Vector3D copy = new Vector3D(this);
        copy.x *= scalar;
        copy.y *= scalar;
        copy.z *= scalar;
        return copy;
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
}
