package com.evenstar.model.lights;

import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Direction;

public class DirectionalLight implements Light
{
    private final Direction direction;
    private final Color lightColor;
    private boolean on;

    public DirectionalLight(Direction directionToLight, Color lightColor)
    {
        this.direction = directionToLight;
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

    public Direction getDirectionToLight()
    {
        return direction;
    }

    @Override
    public Color getLightColor()
    {
        return lightColor;
    }

    @Override
    public String toString()
    {
        return "DirectionalLight{" +
                "direction=" + direction +
                ", lightColor=" + lightColor +
                '}';
    }
}
