package com.evenstar.model;

import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;

public class Ray
{
    private final Point origin;
    private final Direction direction;

    public Ray(Point origin, Direction direction)
    {
        this.origin = origin;
        this.direction = direction;
    }

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
