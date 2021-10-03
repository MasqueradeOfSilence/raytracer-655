package com.evenstar.model.lights;

import com.evenstar.model.vectors.Color;

public class AmbientLight implements Light
{
    private final Color lightColor;
    private boolean on;

    public AmbientLight(Color lightColor)
    {
        this.lightColor = lightColor;
        this.on = true;
    }

    @Override
    public void turnOff()
    {
        this.on = false;
    }

    @Override
    public void turnOn()
    {
        this.on = true;
    }

    @Override
    public boolean isOn()
    {
        return on;
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
