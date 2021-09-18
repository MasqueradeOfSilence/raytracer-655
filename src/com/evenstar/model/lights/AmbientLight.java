package com.evenstar.model.lights;

import com.evenstar.model.vectors.Color;

public class AmbientLight implements Light
{
    private final Color lightColor;

    public AmbientLight(Color lightColor)
    {
        this.lightColor = lightColor;
    }

    @Override
    public Color getLightColor()
    {
        return lightColor;
    }

    @Override
    public String toString()
    {
        return "AmbientLight{" +
                "lightColor=" + lightColor +
                '}';
    }
}
