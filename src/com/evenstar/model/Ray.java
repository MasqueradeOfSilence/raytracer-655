package com.evenstar.model;

public class Ray
{
    private Point origin;
    private Direction direction;

    public Point at(double t)
    {
        Vector3D secondTerm = direction.getDirectionVector().multiplyByScalar(t);
        Vector3D firstTerm = origin.getCoordinates();
        Vector3D toReturn = firstTerm.addSecondVector(secondTerm);
        return new Point(toReturn);
    }

    public Point getOrigin()
    {
        return origin;
    }

    public Direction getDirection()
    {
        return direction;
    }
}
