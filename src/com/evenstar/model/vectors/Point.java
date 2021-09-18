package com.evenstar.model.vectors;

import java.util.Objects;

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

    @Override
    public String toString()
    {
        return "Point{" +
                "coordinates=" + coordinates +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return Objects.equals(coordinates, point.coordinates);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(coordinates);
    }
}
