package com.evenstar.model;

import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ray ray = (Ray) o;
        return Objects.equals(origin, ray.origin) &&
                Objects.equals(direction, ray.direction);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(origin, direction);
    }
}
