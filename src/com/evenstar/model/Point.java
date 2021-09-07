package com.evenstar.model;

public class Point
{
    private Vector3D coordinates;
    public Point(double x, double y, double z)
    {
        coordinates = new Vector3D(x, y, z);
    }

    public Point(Vector3D coordinates)
    {
        this.coordinates = coordinates;
    }

    public double getX()
    {
        return coordinates.getX();
    }

    public double getY()
    {
        return coordinates.getY();
    }

    public double getZ()
    {
        return coordinates.getZ();
    }

    public Vector3D getCoordinates()
    {
        return coordinates;
    }
}
