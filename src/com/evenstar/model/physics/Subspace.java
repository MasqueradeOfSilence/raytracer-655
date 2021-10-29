package com.evenstar.model.physics;

import com.evenstar.model.vectors.Point;

public class Subspace
{
    private final Point upperLeft;
    private final Point bottomRight;

    public Subspace(Point upperLeft, Point bottomRight)
    {
        this.upperLeft = upperLeft;
        this.bottomRight = bottomRight;
    }

    public Subspace(BoundingBox boundingBox)
    {
        this.upperLeft = boundingBox.getVertex1();
        this.bottomRight = boundingBox.getVertex8();
    }

    public Point getUpperLeft()
    {
        return upperLeft;
    }

    public Point getBottomRight()
    {
        return bottomRight;
    }
}
