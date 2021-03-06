package com.evenstar.util;

import com.evenstar.model.lights.Light;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.textures.Material;

public final class ClassIdentifier
{
    public static boolean isDiffuse(Material material)
    {
        return material.getClass().toString().contains("Diffuse");
    }

    public static boolean isReflective(Material material)
    {
        return material.getClass().toString().contains("Reflective");
    }

    public static boolean isGlass(Material material)
    {
        return material.getClass().toString().contains("Glass");
    }

    public static boolean isAmber(Material material)
    {
        return material.getClass().toString().contains("Amber");
    }

    public static boolean isPhong(Material material)
    {
        return material.getClass().toString().contains("Phong");
    }

    public static boolean isEmissive(Material material)
    {
        return material.getClass().toString().contains("Emissive");
    }

    public static boolean isTexture(Material material)
    {
        return material.getClass().toString().contains("Texture");
    }

    public static boolean isTriangle(Shape shape)
    {
        return shape.getClass().toString().contains("Triangle");
    }

    public static boolean isSphere(Shape shape)
    {
        return shape.getClass().toString().contains("Sphere");
    }

    public static boolean isDirectionalLight(Light light)
    {
        return light.getClass().toString().contains("Directional");
    }

    public static boolean isAreaLight(Light light)
    {
        return light.getClass().toString().contains("Area");
    }

    public static boolean isPointLight(Light light)
    {
        return light.getClass().toString().contains("Point");
    }
}
