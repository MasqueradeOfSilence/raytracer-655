package com.evenstar;

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

    }

}
