package com.evenstar.model.shapes;

import com.evenstar.model.vectors.Point;

public class Sphere
{
    private final Point center;
    private final double radius;

    public Sphere(Point center, double radius)
    {
        this.center = center;
        this.radius = radius;
    }

    public Point getCenter()
    {
        return center;
    }

    public double getRadius()
    {
        return radius;
    }
}
