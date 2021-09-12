package com.evenstar.model.vectors;

public class Point
{
    private final Vector3D coordinates;

    public Point(double x, double y, double z)
    {
        coordinates = new Vector3D(x, y, z);
    }

    public Point(Point point)
    {
        this.coordinates = point.coordinates;
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

    public Vector3D getVector()
    {
        return coordinates;
    }
}
