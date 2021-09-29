package com.evenstar.model.vectors;

import java.util.Objects;

public class Color
{
    private final Vector3D rgb;

    public Color(double r, double g, double b)
    {
        rgb = new Vector3D(r, g, b);
    }

    public Color(Color color)
    {
        this.rgb = color.rgb;
    }

    public Color(Vector3D colorVector)
    {
        this.rgb = colorVector;
    }

    public double r()
    {
        return rgb.getX();
    }

    public double g()
    {
        return rgb.getY();
    }

    public double b()
    {
        return rgb.getZ();
    }

    public Vector3D getVector()
    {
        return rgb;
    }

    @Override
    public String toString()
    {
        return "Color{" +
                "rgb=" + rgb +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Color color = (Color) o;
        return Objects.equals(rgb, color.rgb);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(rgb);
    }
}
