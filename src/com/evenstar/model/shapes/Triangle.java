package com.evenstar.model.shapes;

import com.evenstar.model.textures.Material;
import com.evenstar.model.vectors.HitPair;
import com.evenstar.model.vectors.Point;

import java.util.Objects;

public class Triangle implements Shape
{
    private final Point vertex1;
    private final Point vertex2;
    private final Point vertex3;
    private final Material material;
    private HitPair hitPair = null;

    public Triangle(Point vertex1, Point vertex2, Point vertex3, Material material)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.material = material;
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

    @Override
    public HitPair getHitPair()
    {
        return hitPair;
    }

    public void setHitPair(HitPair hitPair)
    {
        this.hitPair = hitPair;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Objects.equals(vertex1, triangle.vertex1) &&
                Objects.equals(vertex2, triangle.vertex2) &&
                Objects.equals(vertex3, triangle.vertex3) &&
                Objects.equals(material, triangle.material);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(vertex1, vertex2, vertex3, material);
    }

    @Override
    public Material getMaterial()
    {
        return material;
    }

    @Override
    public String toString()
    {
        return "Triangle{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                ", vertex3=" + vertex3 +
                ", material=" + material +
                '}';
    }
}
