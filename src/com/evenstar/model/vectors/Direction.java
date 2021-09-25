package com.evenstar.model.vectors;

import java.util.Objects;

public class Direction
{
    private final Vector3D point;

    public Direction(double x, double y, double z)
    {
        point = new Vector3D(x, y, z);
    }

    public Direction(Direction direction)
    {
        this.point = direction.point;
    }

    public Direction(Vector3D vector)
    {
        point = vector;
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

    public Vector3D getVector()
    {
        return point;
    }

    @Override
    public String toString()
    {
        return "Direction{" +
                "point=" + point +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direction direction = (Direction) o;
        return Objects.equals(point, direction.point);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(point);
    }
}
