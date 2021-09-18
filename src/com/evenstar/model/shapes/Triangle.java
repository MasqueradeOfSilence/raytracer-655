package com.evenstar.model.shapes;

import com.evenstar.model.textures.Material;
import com.evenstar.model.vectors.Point;

public class Triangle implements Shape
{
    private final Point vertex1;
    private final Point vertex2;
    private final Point vertex3;
    private final Material material;

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
