package com.evenstar.model.lights;

import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;

public class AreaLight implements Light
{
    private final Color lightColor;
    private final Point location;
    private boolean on;
    private final double radius;

    public AreaLight(Color lightColor, Point location, double radius)
    {
        this.lightColor = lightColor;
        this.location = location;
        this.on = true;
        this.radius = radius;
    }

    public AreaLight(Vector3D lightColor, Vector3D location, double radius)
    {
        this.lightColor = new Color(lightColor);
        this.location = new Point(location);
        this.on = true;
        this.radius = radius;
    }

    @Override
    public Color getLightColor()
    {
        return this.lightColor;
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
        return this.on;
    }

    public Point getLocation()
    {
        return location;
    }

    public double getRadius()
    {
        return radius;
    }
}
