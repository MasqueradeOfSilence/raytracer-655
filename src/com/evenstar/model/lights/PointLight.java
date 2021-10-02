package com.evenstar.model.lights;

import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;

public class PointLight implements Light
{
    private final Color lightColor;
    private final Point location;

    public PointLight(Color lightColor, Point location)
    {
        this.lightColor = lightColor;
        this.location = location;
    }

    public PointLight(Vector3D lightColor, Vector3D location)
    {
        this.lightColor = new Color(lightColor);
        this.location = new Point(location);
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
