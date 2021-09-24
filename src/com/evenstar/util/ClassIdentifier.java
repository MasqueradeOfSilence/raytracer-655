package com.evenstar.util;

import com.evenstar.model.shapes.Shape;
import com.evenstar.model.textures.Material;

public final class ClassIdentifier
{
    public static boolean isDiffuse(Material material)
    {
        return material.getClass().toString().contains("Diffuse");
    }

    public static boolean isTriangle(Shape shape)
    {
        return shape.getClass().toString().contains("Triangle");
    }

    public static boolean isSphere(Shape shape)
    {
        return shape.getClass().toString().contains("Sphere");
    }
}
