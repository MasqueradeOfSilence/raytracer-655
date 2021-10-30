package com.evenstar.model.physics;

import com.evenstar.model.vectors.Point;

import java.util.ArrayList;
import java.util.Objects;

public class Subspace
{
    private final Point upperLeft;
    private final Point bottomRight;
    private ArrayList<BoundingBox> boxes;

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

    public ArrayList<BoundingBox> getBoxes()
    {
        return boxes;
    }

    public void setBoxes(ArrayList<BoundingBox> boxes)
    {
        this.boxes = boxes;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subspace subspace = (Subspace) o;
        return Objects.equals(upperLeft, subspace.upperLeft) &&
                Objects.equals(bottomRight, subspace.bottomRight) &&
                Objects.equals(boxes, subspace.boxes);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(upperLeft, bottomRight, boxes);
    }

    @Override
    public String toString()
    {
        return "Subspace{" +
                "upperLeft=" + upperLeft +
                ", bottomRight=" + bottomRight +
                ", boxes=" + boxes +
                '}';
    }
}
