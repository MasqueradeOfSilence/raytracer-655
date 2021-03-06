package com.evenstar.util;

import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.physics.Subspace;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.vectors.Point;
import com.evenstar.util.art.BoundingBoxDrawer;
import com.evenstar.util.physics.SubspaceComputer;

import java.util.ArrayList;
import java.util.Collections;

/**
 * The AcceleratedRaytracer will implement the bounding box and median-split algorithms to speed up the raytracing.
 */
public class AcceleratedRaytracer
{
    private Point tMin = new Point(0, 0, 0);
    private Point tMax = new Point(0, 0, 0);

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

    public BoundingBox createBoundingBoxTriangle(Triangle triangle)
    {
        ArrayList<Double> xValues = new ArrayList<>();
        xValues.add(triangle.getVertex1().getX());
        xValues.add(triangle.getVertex2().getX());
        xValues.add(triangle.getVertex3().getX());
        double maxX = Collections.max(xValues);
        double minX = Collections.min(xValues);
        ArrayList<Double> yValues = new ArrayList<>();
        yValues.add(triangle.getVertex1().getY());
        yValues.add(triangle.getVertex2().getY());
        yValues.add(triangle.getVertex3().getY());
        double maxY = Collections.max(yValues);
        double minY = Collections.min(yValues);
        ArrayList<Double> zValues = new ArrayList<>();
        zValues.add(triangle.getVertex1().getZ());
        zValues.add(triangle.getVertex2().getZ());
        zValues.add(triangle.getVertex3().getZ());
        double maxZ = Collections.max(zValues);
        double minZ = Collections.min(zValues);
        Point vertex1 = new Point(minX, maxY, maxZ);
        Point vertex2 = new Point(maxX, maxY, maxZ);
        Point vertex3 = new Point(minX, minY, maxZ);
        Point vertex4 = new Point(maxX, minY, maxZ);
        Point vertex5 = new Point(minX, maxY, minZ);
        Point vertex6 = new Point(maxX, maxY, minZ);
        Point vertex7 = new Point(minX, minY, minZ);
        Point vertex8 = new Point(maxX, minY, minZ);
        return new BoundingBox(vertex1, vertex2, vertex3, vertex4,
            vertex5, vertex6, vertex7, vertex8);
    }

    public boolean doesRayIntersectSubspace(Ray ray, Subspace subspace)
    {
        Point bogus = new Point(0, 0, 0);
        Point vertex2 = new Point(subspace.getBottomRight().getX(), subspace.getUpperLeft().getY(),
                subspace.getUpperLeft().getZ());
        Point vertex7 = new Point(subspace.getUpperLeft().getX(), subspace.getBottomRight().getY(),
                subspace.getBottomRight().getZ());
        BoundingBox bBox = new BoundingBox(bogus, vertex2, bogus, bogus, bogus, bogus, vertex7, bogus);
        return this.doesRayIntersectBoundingBox(ray, bBox);
    }

    public boolean doesRayIntersectBoundingBox(Ray ray, BoundingBox boundingBox)
    {
        Point minimum = boundingBox.getVertex7();
        Point maximum = boundingBox.getVertex2();
        double tMinX = (minimum.getX() - ray.getOrigin().getX()) / ray.getDirection().getX();
        double tMaxX = (maximum.getX() - ray.getOrigin().getX()) / ray.getDirection().getX();
        if (tMinX > tMaxX)
        {
            double savedTMinX = tMinX;
            double savedTMaxX = tMaxX;
            tMinX = savedTMaxX;
            tMaxX = savedTMinX;
        }
        double tMinY = (minimum.getY() - ray.getOrigin().getY()) / ray.getDirection().getY();
        double tMaxY = (maximum.getY() - ray.getOrigin().getY()) / ray.getDirection().getY();
        if (tMinY > tMaxY)
        {
            double savedTMinY = tMinY;
            double savedTMaxY = tMaxY;
            tMinY = savedTMaxY;
            tMaxY = savedTMinY;
        }
        if ((tMinX > tMaxY) || (tMinY > tMaxX))
        {
            return false;
        }
        if (tMinY > tMinX)
        {
            tMinX = tMinY;
        }
        if (tMaxY < tMaxX)
        {
            tMaxX = tMaxY;
        }
        double tMinZ = (minimum.getZ() - ray.getOrigin().getZ()) / ray.getDirection().getZ();
        double tMaxZ = (maximum.getZ() - ray.getOrigin().getZ()) / ray.getDirection().getZ();
        if (tMinZ > tMaxZ)
        {
            double savedTMinZ = tMinZ;
            double savedTMaxZ = tMaxZ;
            tMinZ = savedTMaxZ;
            tMaxZ = savedTMinZ;
        }
        if ((tMinX > tMaxZ) || (tMinZ > tMaxX))
        {
            return false;
        }
        if (tMinZ > tMinX)
        {
            tMinX = tMinZ;
        }
        if (tMaxZ < tMaxX)
        {
            tMaxX = tMaxZ;
        }
        this.tMin = new Point(tMinX, tMinY, tMinZ);
        this.tMax = new Point(tMaxX, tMaxY, tMaxZ);
        return true;
    }

    public ArrayList<BoundingBox> computeBoundingBoxes(ArrayList<Shape> shapes)
    {
        ArrayList<BoundingBox> toReturn = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++)
        {
            Shape current = shapes.get(i);
            if (ClassIdentifier.isSphere(current))
            {
                Sphere sphere = (Sphere) current;
                BoundingBox bBox = createBoundingBoxSphere(sphere);
                bBox.setCorrespondingShape(sphere);
                toReturn.add(bBox);
                // Print out created bounding box
                //System.out.println(createBoundingBoxSphere(sphere).toString());
            }
            else if (ClassIdentifier.isTriangle(current))
            {
                Triangle triangle = (Triangle) current;
                BoundingBox bBox = createBoundingBoxTriangle(triangle);
                bBox.setCorrespondingShape(triangle);
                toReturn.add(bBox);
                //System.out.println(createBoundingBoxTriangle(triangle).toString());
            }
        }
        return toReturn;
    }

    public ArrayList<Subspace> computeSubspacesForScene(Scene scene)
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        return subspaceComputer.computeSubspacesForScene(scene);
    }

    public boolean rayHitsBoundingBox(Ray ray, Shape current)
    {
        ArrayList<Shape> shapes = new ArrayList<>();
        shapes.add(current);
        // change to support just one
        ArrayList<BoundingBox> boundingBoxes = this.computeBoundingBoxes(shapes);
        return this.doesRayIntersectBoundingBox(ray, boundingBoxes.get(0));
    }

    // For testing and visualization purposes
    public PPMImage drawBoundingBoxes(PPMImage finalImage, Scene scene, int dimension, Raytracer raytracer)
    {
        ArrayList<BoundingBox> boundingBoxes = computeBoundingBoxes(scene.getShapes());
        BoundingBoxDrawer boundingBoxDrawer = new BoundingBoxDrawer();
        return boundingBoxDrawer.drawBoundingBoxes(finalImage, scene, dimension, raytracer, boundingBoxes);
    }

    public Point getTMin()
    {
        return tMin;
    }

    public Point getTMax()
    {
        return tMax;
    }
}
