package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.vectors.Point;
import com.evenstar.util.art.BoundingBoxDrawer;

import java.util.ArrayList;

/**
 * The AcceleratedRaytracer will implement the bounding box and median-split algorithms to speed up the raytracing.
 */
public class AcceleratedRaytracer
{
    public BoundingBox createBoundingBoxSphere(Sphere sphere)
    {
        Point center = sphere.getCenter();
        double x = center.getX();
        double y = center.getY();
        double radius = sphere.getRadius();
        double frontZ = center.getZ() + radius;
        double backZ = center.getZ() - radius;
        // Front half
        Point vertex1 = new Point((x - radius), (y + radius), frontZ);
        Point vertex2 = new Point((x + radius), (y + radius), frontZ);
        Point vertex3 = new Point((x - radius), (y - radius), frontZ);
        Point vertex4 = new Point((x + radius), (y - radius), frontZ);
        // Back half
        Point vertex5 = new Point((x - radius), (y + radius), backZ);
        Point vertex6 = new Point((x + radius), (y + radius), backZ);
        Point vertex7 = new Point((x - radius), (y - radius), backZ);
        Point vertex8 = new Point((x + radius), (y - radius), backZ);
        return new BoundingBox(vertex1, vertex2, vertex3, vertex4,
                vertex5, vertex6, vertex7, vertex8);
    }

    private ArrayList<BoundingBox> computeBoundingBoxes(ArrayList<Shape> shapes)
    {
        ArrayList<BoundingBox> toReturn = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++)
        {
            Shape current = shapes.get(i);
            if (ClassIdentifier.isSphere(current))
            {
                Sphere sphere = (Sphere) current;
                toReturn.add(createBoundingBoxSphere(sphere));
                // Print out created bounding box
                System.out.println(createBoundingBoxSphere(sphere).toString());
            }
        }
        return toReturn;
    }

    // For testing and visualization purposes
    public PPMImage drawBoundingBoxes(PPMImage finalImage, Scene scene, int dimension, Raytracer raytracer)
    {
        ArrayList<BoundingBox> boundingBoxes = computeBoundingBoxes(scene.getShapes());
        BoundingBoxDrawer boundingBoxDrawer = new BoundingBoxDrawer();
        return boundingBoxDrawer.drawBoundingBoxes(finalImage, scene, dimension, raytracer, boundingBoxes);
    }
}
