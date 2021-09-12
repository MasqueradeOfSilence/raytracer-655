package com.evenstar.model.shapes;

import com.evenstar.model.Ray;
import com.evenstar.model.vectors.Point;

public class Sphere implements Shape
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

    @Override
    public boolean hit(Ray ray, double tMin, double tMax, Hit hit)
    {
        return false;
    }
}
