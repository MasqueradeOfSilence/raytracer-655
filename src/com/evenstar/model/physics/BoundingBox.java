package com.evenstar.model.physics;

import com.evenstar.model.vectors.Point;

public class BoundingBox
{
    private final Point vertex1;
    private final Point vertex2;
    private final Point vertex3;
    private final Point vertex4;
    private final Point vertex5;
    private final Point vertex6;
    private final Point vertex7;
    private final Point vertex8;

    public BoundingBox(Point vertex1, Point vertex2, Point vertex3, Point vertex4,
                       Point vertex5, Point vertex6, Point vertex7, Point vertex8)
    {
        this.vertex1 = vertex1;
        this.vertex2 = vertex2;
        this.vertex3 = vertex3;
        this.vertex4 = vertex4;
        this.vertex5 = vertex5;
        this.vertex6 = vertex6;
        this.vertex7 = vertex7;
        this.vertex8 = vertex8;
    }

    public Point getVertex1()
    {
        return vertex1;
    }

    public Point getVertex2()
    {
        return vertex2;
    }

    public Point getVertex3()
    {
        return vertex3;
    }

    public Point getVertex4()
    {
        return vertex4;
    }

    public Point getVertex5()
    {
        return vertex5;
    }

    public Point getVertex6()
    {
        return vertex6;
    }

    public Point getVertex7()
    {
        return vertex7;
    }

    public Point getVertex8()
    {
        return vertex8;
    }

    public Point getVertexWithStrippedZ(Point point)
    {
        return new Point(point.getX(), point.getY(), 0);
    }

    @Override
    public String toString()
    {
        return "BoundingBox{" +
                "vertex1=" + vertex1 +
                ", vertex2=" + vertex2 +
                ", vertex3=" + vertex3 +
                ", vertex4=" + vertex4 +
                ", vertex5=" + vertex5 +
                ", vertex6=" + vertex6 +
                ", vertex7=" + vertex7 +
                ", vertex8=" + vertex8 +
                '}';
    }
}