package com.evenstar.model.vectors;

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
}
