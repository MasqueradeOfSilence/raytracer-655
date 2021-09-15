package com.evenstar.model;

import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;

public class Ray
{
    private final Point origin;
    private final Direction direction;

    public Ray(Point origin, Direction direction)
    {
        this.origin = origin;
        this.direction = direction;
    }

    // TODO Do you understand what this is doing? Is it needed?
    public Point at(double t)
    {
        Vector3D secondTerm = VectorOperations.multiplyByScalar(direction.getVector(), t);
        Vector3D firstTerm = origin.getVector();
        Vector3D toReturn = VectorOperations.addVectors(firstTerm, secondTerm);
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
