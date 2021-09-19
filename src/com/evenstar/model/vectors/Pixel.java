package com.evenstar.model.vectors;

public class Pixel
{
    private Color rgb;

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

    private void convertTo255Scale()
    {
        this.rgb = new Color(this.getR() * 255, this.getG() * 255, this.getB() * 255);
    }

    public String convertAndPrintForPPM()
    {
        this.convertTo255Scale();
        return (int)getR() + " " + (int)getG() + " " + (int)getB() + "\n";
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
