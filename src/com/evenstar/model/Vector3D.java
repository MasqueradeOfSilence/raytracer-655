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

    // Operations affect the vector itself
    public void addSecondVector(Vector3D secondVector)
    {
        this.x += secondVector.x;
        this.y += secondVector.y;
        this.z += secondVector.z;
    }

    public void subtractSecondVector(Vector3D secondVector)
    {
        this.x -= secondVector.x;
        this.y -= secondVector.y;
        this.z -= secondVector.z;
    }

    public double dotWithSecondVector(Vector3D secondVector)
    {
        return this.x * secondVector.x
                + this.y * secondVector.y
                + this.z * secondVector.z;
    }

    public void crossWithSecondVector(Vector3D secondVector)
    {
        double new_x = this.y * secondVector.z - this.z * secondVector.y;
        double new_y = this.z * secondVector.x - this.x * secondVector.z;
        double new_z = this.x * secondVector.y - this.y * secondVector.x;
        this.x = new_x;
        this.y = new_y;
        this.z = new_z;
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
