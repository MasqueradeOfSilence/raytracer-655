package com.evenstar.model.shapes;

import com.evenstar.model.textures.Material;
import com.evenstar.model.vectors.HitPair;
import com.evenstar.model.vectors.Point;

import java.util.Objects;

public class Sphere implements Shape
{
    private final Point center;
    private final double radius;
    private final Material material;
    private HitPair hitPair = null;

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

    @Override
    public HitPair getHitPair()
    {
        return hitPair;
    }

    public void setHitPair(HitPair hitPair)
    {
        // Will be set and re-set a lot
        this.hitPair = hitPair;
    }

    @Override
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sphere sphere = (Sphere) o;
        return Double.compare(sphere.radius, radius) == 0 &&
                Objects.equals(center, sphere.center) &&
                Objects.equals(material, sphere.material);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(center, radius, material);
    }
}
