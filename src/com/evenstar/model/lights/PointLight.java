package com.evenstar.model.lights;

import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;

public class PointLight implements Light
{
    private final Color lightColor;
    private final Point location;
    private boolean on;

    public PointLight(Color lightColor, Point location)
    {
        this.lightColor = lightColor;
        this.location = location;
        this.on = true;
    }

    public PointLight(Vector3D lightColor, Vector3D location)
    {
        this.lightColor = new Color(lightColor);
        this.location = new Point(location);
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

    public Point getLocation()
    {
        return location;
    }

    @Override
    public Color getLightColor()
    {
        return lightColor;
    }
}
