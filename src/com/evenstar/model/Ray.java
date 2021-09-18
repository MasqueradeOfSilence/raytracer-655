package com.evenstar.model;

import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;

public class Ray
{
    private final Point origin;
    private final Direction direction;

    public Ray(Point origin, Direction direction)
    {
        this.origin = origin;
        this.direction = direction;
    }

    public Point getOrigin()
    {
        return origin;
    }

    public Direction getDirection()
    {
        return direction;
    }

    @Override
    public String toString()
    {
        return "Ray{" +
                "origin=" + origin +
                ", direction=" + direction +
                '}';
    }
}
