package com.evenstar.model.vectors;

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
}
