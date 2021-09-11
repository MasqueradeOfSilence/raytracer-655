package com.evenstar.model;

import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;

public class Camera
{
    private final double aspectRatio;
    private final double viewportHeight;
    private final double viewportWidth;
    private final double focalLength;
    private final Point origin;
    private final Direction horizontal;
    private final Direction vertical;

    public Camera(double aspectRatio, double viewportHeight, double viewportWidth, double focalLength, Point origin,
                  Direction horizontal, Direction vertical)
    {
        this.aspectRatio = aspectRatio;
        this.viewportHeight = viewportHeight;
        this.viewportWidth = viewportWidth;
        this.focalLength = focalLength;
        this.origin = origin;
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    public Point getLowerLeftCorner()
    {
        Point point1 = new Point(horizontal.getDirectionVector().divideByScalar(2));
        Point point2 = new Point(vertical.getDirectionVector().divideByScalar(2));
        Point point3 = new Point(origin.getCoordinates().subtractSecondVector(point1.getCoordinates()));
        Point point4 = new Point(point3.getCoordinates().subtractSecondVector(point2.getCoordinates()));
        Point point5 = new Point(0, 0, focalLength);
        return new Point(point4.getCoordinates().subtractSecondVector(point5.getCoordinates()));
    }

    public double getAspectRatio()
    {
        return aspectRatio;
    }

    public double getViewportHeight()
    {
        return viewportHeight;
    }

    public double getViewportWidth()
    {
        return viewportWidth;
    }

    public double getFocalLength()
    {
        return focalLength;
    }

    public Point getOrigin()
    {
        return origin;
    }

    public Direction getHorizontal()
    {
        return horizontal;
    }

    public Direction getVertical()
    {
        return vertical;
    }
}
