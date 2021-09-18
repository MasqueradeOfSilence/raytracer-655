package com.evenstar.model.shapes;

import com.evenstar.model.textures.Material;
import com.evenstar.model.vectors.Point;

public class Sphere implements Shape
{
    private final Point center;
    private final double radius;
    private final Material material;

    public Sphere(Point center, double radius, Material material)
    {
        this.center = center;
        this.radius = radius;
        this.material = material;
    }

    public Point getCenter()
    {
        return center;
    }

    public double getRadius()
    {
        return radius;
    }

    public Material getMaterial()
    {
        return material;
    }

    @Override
    public String toString()
    {
        return "Sphere{" +
                "center=" + center +
                ", radius=" + radius +
                ", material=" + material +
                '}';
    }
}
