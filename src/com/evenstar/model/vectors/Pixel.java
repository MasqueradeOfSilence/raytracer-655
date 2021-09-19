package com.evenstar.model.vectors;

public class Pixel
{
    private final Color rgb;

    public Pixel(Color rgb)
    {
        this.rgb = rgb;
    }

    public Pixel(Vector3D rgb)
    {
        this.rgb = new Color(rgb);
    }

    public Color getColor()
    {
        return rgb;
    }

    public double getR()
    {
        return this.rgb.getVector().getX();
    }

    public double getG()
    {
        return this.rgb.getVector().getY();
    }

    public double getB()
    {
        return this.rgb.getVector().getZ();
    }
}
