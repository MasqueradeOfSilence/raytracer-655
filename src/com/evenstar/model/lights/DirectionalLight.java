package com.evenstar.model.lights;

import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Direction;

public class DirectionalLight implements Light
{
    private final Direction direction;
    private final Color lightColor;

    public DirectionalLight(Direction directionToLight, Color lightColor)
    {
        this.direction = directionToLight;
        this.lightColor = lightColor;
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
