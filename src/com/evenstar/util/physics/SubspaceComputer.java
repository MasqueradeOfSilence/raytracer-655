package com.evenstar.util.physics;

import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.physics.Subspace;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.vectors.Point;
import com.evenstar.util.AcceleratedRaytracer;

import java.util.ArrayList;

public class SubspaceComputer
{
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
        ArrayList<Subspace> subspaces = new ArrayList<>();
        BoundingBox boxAroundScene = this.computeBoxAroundScene(scene);
        // delete this
        System.out.println("Computing subspaces...");
        return subspaces;
    }
}
