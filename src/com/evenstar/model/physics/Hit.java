package com.evenstar.model.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.vectors.Point;

public class Hit
{
    // Hits: Point, Distance, and Shape
    private final Point hitPoint;
    private final double distanceToRay;
    private final Shape correspondingShape;
    private final Ray correspondingRay;

    public Hit(Point hitPoint, double distanceToRay, Shape correspondingShape, Ray correspondingRay)
    {
        this.hitPoint = hitPoint;
        this.distanceToRay = distanceToRay;
        this.correspondingShape = correspondingShape;
        this.correspondingRay = correspondingRay;
    }

    public Point getHitPoint()
    {
        return hitPoint;
    }

    public double getDistanceToRay()
    {
        return distanceToRay;
    }

    public Shape getCorrespondingShape()
    {
        return correspondingShape;
    }

    public Ray getCorrespondingRay()
    {
        return correspondingRay;
    }
}
