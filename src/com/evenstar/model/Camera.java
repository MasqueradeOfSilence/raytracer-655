package com.evenstar.model;

import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.VectorOperations;

public class Camera
{
    private Direction lookAt;
    private Direction lookFrom;
    private Direction lookUp;
    private int fieldOfView;

    private double aspectRatio;
    private double viewportHeight;
    private double viewportWidth;
    private double focalLength;
    private Point origin;
    private Direction horizontal;
    private Direction vertical;

    public Camera(Direction lookAt, Direction lookFrom, Direction lookUp, int fieldOfView)
    {
        this.lookAt = lookAt;
        this.lookFrom = lookFrom;
        this.lookUp = lookUp;
        this.fieldOfView = fieldOfView;
    }

    // May not need this anymore
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
        Point point1 = new Point(VectorOperations.divideByScalar(horizontal.getVector(), 2));
        Point point2 = new Point(VectorOperations.divideByScalar(vertical.getVector(), 2));
        Point point3 = new Point(VectorOperations.subtractVectors(origin.getVector(), point1.getVector()));
        Point point4 = new Point(VectorOperations.subtractVectors(point3.getVector(), point2.getVector()));
        Point point5 = new Point(0, 0, focalLength);
        return new Point(VectorOperations.subtractVectors(point4.getVector(), point5.getVector()));
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
