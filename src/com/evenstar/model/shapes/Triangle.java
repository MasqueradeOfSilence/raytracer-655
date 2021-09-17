package com.evenstar.model.shapes;

import com.evenstar.model.Ray;
import com.evenstar.model.vectors.Point;

public class Triangle implements Shape
{
    private final Point vertex1;
    private final Point vertex2;
    private final Point vertex3;

    public Triangle(Point vertex1, Point vertex2, Point vertex3)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
    }

    @Override
    public Hit hitByRay(Ray ray, double tMin, double tMax, Hit hit)
    {
        return null;
    }

    public Point getVertex1()
    {
        return vertex1;
    }

    public Point getVertex2()
    {
        return vertex2;
    }

    public Point getVertex3()
    {
        return vertex3;
    }
}
