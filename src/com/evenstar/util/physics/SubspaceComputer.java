package com.evenstar.util.physics;

import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.physics.Subspace;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.vectors.Midpoint;
import com.evenstar.model.vectors.Point;
import com.evenstar.util.AcceleratedRaytracer;

import java.util.ArrayList;

public class SubspaceComputer
{
    public double distance2D(Point a, Point b)
    {
        return Math.sqrt(Math.pow(b.getX() - a.getX(), 2) + Math.pow(b.getY() - a.getY(), 2));
    }

    public Midpoint midpoint2D(Point a, Point b)
    {
        return new Midpoint(((a.getX() + b.getX()) / 2), ((a.getY() + b.getY()) / 2));
    }

    public char didAOrBWin(BoundingBox boundingBox)
    {
        Point p1 = boundingBox.getVertex1();
        Point p2 = boundingBox.getVertex2();
        Point p3 = boundingBox.getVertex3();
        double a = this.distance2D(p1, p2);
        double b = this.distance2D(p1, p3);
        double winner = Math.max(a, b);
        if (winner == a)
        {
            return 'a';
        }
        else
        {
            return 'b';
        }
    }

    public boolean isVerticalSplit(char aOrB)
    {
        return aOrB == 'a';
    }

    public Subspace computeBottomSubspace(Subspace parent, Midpoint midpoint)
    {
        double maxZ = parent.getUpperLeft().getZ();
        Point upperLeft = new Point(midpoint.getX(), midpoint.getY(), maxZ);
        Point bottomRight = parent.getBottomRight();
        return new Subspace(upperLeft, bottomRight);
    }

    public Subspace computeTopSubspace(Subspace parent, Midpoint midpoint)
    {
        double minZ = parent.getBottomRight().getZ();
        Point upperLeft = parent.getUpperLeft();
        Point bottomRight = new Point(parent.getBottomRight().getX(), midpoint.getY(), minZ);
        return new Subspace(upperLeft, bottomRight);
    }

    public Subspace computeRightSubspace(Subspace parent, Midpoint midpoint)
    {
        double maxZ = parent.getUpperLeft().getZ();
        Point upperLeft = new Point(midpoint.getX(), midpoint.getY(), maxZ);
        Point bottomRight = parent.getBottomRight();
        System.out.println("Bottom right: " + bottomRight.toString());
        return new Subspace(upperLeft, bottomRight);
    }

    public Subspace computeLeftSubspace(Subspace parent, Midpoint midpoint)
    {
        double minZ = parent.getBottomRight().getZ();
        Point upperLeft = parent.getUpperLeft();
        Point bottomRight = new Point(midpoint.getX(), parent.getBottomRight().getY(), minZ);
        return new Subspace(upperLeft, bottomRight);
    }

    public double computeLargestMagnitudeExtentOfBoundingBox(BoundingBox boundingBox)
    {
        Point p1 = boundingBox.getVertex1();
        Point p2 = boundingBox.getVertex2();
        Point p3 = boundingBox.getVertex3();
        double a = this.distance2D(p1, p2);
        double b = this.distance2D(p1, p3);
        return Math.max(a, b);
    }

    public BoundingBox computeBoxAroundScene(Scene scene)
    {
        ArrayList<Shape> shapes = scene.getShapes();
        assert(shapes.size() > 0);
        double highestX = Double.MIN_VALUE;
        double lowestX = Double.MAX_VALUE;
        double highestY = Double.MIN_VALUE;
        double lowestY = Double.MAX_VALUE;
        double highestZ = Double.MIN_VALUE;
        double lowestZ = Double.MAX_VALUE;
        AcceleratedRaytracer ar = new AcceleratedRaytracer();
        ArrayList<BoundingBox> boundingBoxes = ar.computeBoundingBoxes(shapes);
        for (int i = 0; i < boundingBoxes.size(); i++)
        {
            BoundingBox current = boundingBoxes.get(i);
            ArrayList<Point> vertices = current.getVertices();
            for (int j = 0; j < vertices.size(); j++)
            {
                Point vertex = vertices.get(j);
                if (vertex.getX() >= highestX)
                {
                    highestX = vertex.getX();
                }
                else if (vertex.getX() <= lowestX)
                {
                    lowestX = vertex.getX();
                }
                if (vertex.getY() >= highestY)
                {
                    highestY = vertex.getY();
                }
                else if (vertex.getY() <= lowestY)
                {
                    lowestY = vertex.getY();
                }
                if (vertex.getZ() >= highestZ)
                {
                    highestZ = vertex.getZ();
                }
                else if (vertex.getZ() <= lowestZ)
                {
                    lowestZ = vertex.getZ();
                }
            }
        }
        return new BoundingBox(
                new Point(lowestX, highestY, highestZ),
                new Point(highestX, highestY, highestZ),
                new Point(lowestX, lowestY, highestZ),
                new Point(highestX, lowestY, highestZ),
                new Point(lowestX, highestY, lowestZ),
                new Point(highestX, highestY, lowestZ),
                new Point(lowestX, lowestY, lowestZ),
                new Point(highestX, lowestY, lowestZ)
        );
    }

    public ArrayList<Subspace> computeSubspacesForScene(Scene scene)
    {
        // First iteration
        ArrayList<Subspace> subspaces = new ArrayList<>();
        BoundingBox boxAroundScene = this.computeBoxAroundScene(scene);
        double largestMagnitudeExtent = this.computeLargestMagnitudeExtentOfBoundingBox(boxAroundScene);
        char winningMagnitude = this.didAOrBWin(boxAroundScene);

        return subspaces;
    }
}
