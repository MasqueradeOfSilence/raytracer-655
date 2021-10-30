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

    public char didAOrBWin(Subspace subspace)
    {
        Point p1 = this.computeVertex1(subspace);
        Point p2 = this.computeVertex2(subspace);
        Point p3 = this.computeVertex3(subspace);
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

    public boolean isVertexInsideSubspace(Point p, Subspace box)
    {
        return (box.getUpperLeft().getX() <= p.getX() && p.getX() <= box.getBottomRight().getX()) &&
        (box.getUpperLeft().getY() >= p.getY() && p.getY() >= box.getBottomRight().getY()) &&
        (box.getUpperLeft().getZ() >= p.getZ() && p.getZ() >= box.getBottomRight().getZ());
    }

    public boolean isSubspaceInsideVertex(Point p, Subspace box)
    {
        return (p.getX() <= box.getUpperLeft().getX() && box.getBottomRight().getX() <= p.getX())
                && (p.getY() >= box.getUpperLeft().getY() && box.getBottomRight().getY() >= p.getY());
    }

    public ArrayList<BoundingBox> computeShapesInSubspace(Subspace subspace, ArrayList<BoundingBox> boundingBoxes)
    {
        // these should have shapes right?
        ArrayList<BoundingBox> shapesInSubspace = new ArrayList<>();
        for (int i = 0; i < boundingBoxes.size(); i++)
        {
            BoundingBox current = boundingBoxes.get(i);
            for (int j = 0; j < current.getVertices().size(); j++)
            {
                Point vertex = current.getVertices().get(j);
                if (this.isVertexInsideSubspace(vertex, subspace) || this.isSubspaceInsideVertex(vertex, subspace))
                {
                    shapesInSubspace.add(current);
                    break;
                }
            }
        }
        return shapesInSubspace;
    }

    private Point computeVertex1(Subspace subspace)
    {
        return subspace.getUpperLeft();
    }

    private Point computeVertex2(Subspace subspace)
    {
        return new Point(subspace.getBottomRight().getX(), subspace.getUpperLeft().getY(),
                subspace.getUpperLeft().getZ());
    }

    private Point computeVertex3(Subspace subspace)
    {
        return new Point(subspace.getUpperLeft().getX(), subspace.getBottomRight().getY(),
                subspace.getUpperLeft().getZ());
    }

    private ArrayList<Subspace> medianSplit(Subspace subspace, ArrayList<Subspace> subspaces,
                                            ArrayList<BoundingBox> boundingBoxes)
    {
        char winningMagnitude = this.didAOrBWin(subspace);
        if (winningMagnitude == 'a')
        {
            // Vertical split
            Point p1 = this.computeVertex1(subspace);
            Point p2 = this.computeVertex2(subspace);
            Midpoint midpoint = this.midpoint2D(p1, p2);
            Subspace leftSubspace = this.computeLeftSubspace(subspace, midpoint);
            ArrayList<BoundingBox> shapesInLeftSubspace = this.computeShapesInSubspace(leftSubspace, boundingBoxes);
            leftSubspace.setBoxes(shapesInLeftSubspace);
            subspaces.add(leftSubspace);
            if (subspaces.size() < 20 && shapesInLeftSubspace.size() >2)
            {
                subspaces = this.medianSplit(leftSubspace, subspaces, boundingBoxes);
            }
            // ignore the IntelliJ warning here; we actually do want subspaces to be re-set after getting modified.
//            subspaces = this.medianSplit(leftSubspace, subspaces, boundingBoxes);
            Subspace rightSubspace = this.computeRightSubspace(subspace, midpoint);
            ArrayList<BoundingBox> shapesInRightSubspace = this.computeShapesInSubspace(rightSubspace, boundingBoxes);
            rightSubspace.setBoxes(shapesInRightSubspace);
            subspaces.add(rightSubspace);
            if (subspaces.size() > 20 || shapesInRightSubspace.size() < 3)
            {
                return subspaces;
            }
            subspaces = this.medianSplit(rightSubspace, subspaces, boundingBoxes);
        }
        else if (winningMagnitude == 'b')
        {
            // Horizontal split
            Point p1 = this.computeVertex1(subspace);
            Point p3 = this.computeVertex3(subspace);
            Midpoint midpoint = this.midpoint2D(p1, p3);
            Subspace topSubspace = this.computeTopSubspace(subspace, midpoint);
            ArrayList<BoundingBox> shapesInTopSubspace = this.computeShapesInSubspace(topSubspace, boundingBoxes);
            topSubspace.setBoxes(shapesInTopSubspace);
            subspaces.add(topSubspace);
            if (subspaces.size() < 20 && shapesInTopSubspace.size() > 2)
            {
                subspaces = this.medianSplit(topSubspace, subspaces, boundingBoxes);
            }
            subspaces = this.medianSplit(topSubspace, subspaces, boundingBoxes);
            Subspace bottomSubspace = this.computeBottomSubspace(subspace, midpoint);
            ArrayList<BoundingBox> shapesInBottomSubspace = this.computeShapesInSubspace(bottomSubspace, boundingBoxes);
            bottomSubspace.setBoxes(shapesInBottomSubspace);
            subspaces.add(bottomSubspace);
            if (subspaces.size() > 20 || shapesInBottomSubspace.size() < 3)
            {
                return subspaces;
            }
            subspaces = this.medianSplit(bottomSubspace, subspaces, boundingBoxes);
        }
        return subspaces;
    }

    public ArrayList<Subspace> computeSubspacesForScene(Scene scene)
    {
        // First iteration
        ArrayList<Subspace> subspaces = new ArrayList<>();
        BoundingBox boxAroundScene = this.computeBoxAroundScene(scene);
        char winningMagnitude = this.didAOrBWin(boxAroundScene);
        AcceleratedRaytracer ar = new AcceleratedRaytracer();
        ArrayList<BoundingBox> boundingBoxes = ar.computeBoundingBoxes(scene.getShapes());
        Subspace subspaceAroundScene = new Subspace(boxAroundScene);
        subspaces.add(subspaceAroundScene);
        if (winningMagnitude == 'a')
        {
            // Vertical split
            Point p1 = boxAroundScene.getVertex1();
            Point p2 = boxAroundScene.getVertex2();
            Midpoint midpoint = this.midpoint2D(p1, p2);
            Subspace leftSubspace = this.computeLeftSubspace(subspaceAroundScene, midpoint);
            ArrayList<BoundingBox> shapesInLeftSubspace = this.computeShapesInSubspace(leftSubspace, boundingBoxes);
            leftSubspace.setBoxes(shapesInLeftSubspace);
            subspaces.add(leftSubspace);
            if (subspaces.size() < 20 && shapesInLeftSubspace.size() > 2)
            {
                // ignore the IntelliJ warning here; we actually do want subspaces to be re-set after getting modified.
                subspaces = this.medianSplit(leftSubspace, subspaces, boundingBoxes);
            }
            Subspace rightSubspace = this.computeRightSubspace(subspaceAroundScene, midpoint);
            ArrayList<BoundingBox> shapesInRightSubspace = this.computeShapesInSubspace(rightSubspace, boundingBoxes);
            rightSubspace.setBoxes(shapesInRightSubspace);
            subspaces.add(rightSubspace);
            if (subspaces.size() > 20 || shapesInRightSubspace.size() < 3)
            {
                return subspaces;
            }
            subspaces = this.medianSplit(rightSubspace, subspaces, boundingBoxes);
        }
        else if (winningMagnitude == 'b')
        {
            // Horizontal split
            Point p1 = boxAroundScene.getVertex1();
            Point p3 = boxAroundScene.getVertex3();
            Midpoint midpoint = this.midpoint2D(p1, p3);
            Subspace topSubspace = this.computeTopSubspace(subspaceAroundScene, midpoint);
            ArrayList<BoundingBox> shapesInTopSubspace = this.computeShapesInSubspace(topSubspace, boundingBoxes);
            topSubspace.setBoxes(shapesInTopSubspace);
            subspaces.add(topSubspace);
            if (subspaces.size() < 20 && shapesInTopSubspace.size() > 2)
            {
                subspaces = this.medianSplit(topSubspace, subspaces, boundingBoxes);
            }
            Subspace bottomSubspace = this.computeBottomSubspace(subspaceAroundScene, midpoint);
            ArrayList<BoundingBox> shapesInBottomSubspace = this.computeShapesInSubspace(bottomSubspace, boundingBoxes);
            bottomSubspace.setBoxes(shapesInBottomSubspace);
            subspaces.add(bottomSubspace);
            if (subspaces.size() > 20 || shapesInBottomSubspace.size() < 3)
            {
                return subspaces;
            }
            subspaces = this.medianSplit(bottomSubspace, subspaces, boundingBoxes);
        }
        return subspaces;
    }
}
