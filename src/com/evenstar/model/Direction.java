package com.evenstar.model;

public class Direction
{
    private Vector3D point;
    public Direction(double x, double y, double z)
    {
        point = new Vector3D(x, y, z);
    }

    public double getX()
    {
        return point.getX();
    }

    public double getY()
    {
        return point.getY();
    }

    public double getZ()
    {
        return point.getZ();
    }

    public Vector3D getDirectionVector()
    {
        return point;
    }
}
