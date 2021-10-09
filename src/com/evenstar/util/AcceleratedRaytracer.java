package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.vectors.Pixel;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.VectorOperations;

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
        double frontZ = center.getZ() - radius;
        double backZ = center.getZ() + radius;
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

    public boolean isPointCOnLineBetweenTwoPoints(Point a, Point b, Point c)
    {
        return VectorOperations.distance(a.getVector(), c.getVector()) +
                VectorOperations.distance(b.getVector(), c.getVector()) ==
                VectorOperations.distance(a.getVector(), b.getVector());
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
//                System.out.println(createBoundingBoxSphere(sphere).toString());
            }
        }
        return toReturn;
    }

    private boolean isPointOnABoundingBox(Point point, ArrayList<BoundingBox> boundingBoxes)
    {
        for (BoundingBox currentBox : boundingBoxes)
        {
            if (point.equals(currentBox.getVertex1()) || point.equals(currentBox.getVertex2()) ||
                point.equals(currentBox.getVertex3()) || point.equals(currentBox.getVertex4()) ||
                point.equals(currentBox.getVertex5()) || point.equals(currentBox.getVertex6()) ||
                point.equals(currentBox.getVertex7()) || point.equals(currentBox.getVertex8()))
            {
                return true;
            }
            if (isPointCOnLineBetweenTwoPoints(currentBox.getVertex1(), currentBox.getVertex2(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex3(), currentBox.getVertex4(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex1(), currentBox.getVertex3(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex2(), currentBox.getVertex4(), point) ||

                isPointCOnLineBetweenTwoPoints(currentBox.getVertex1(), currentBox.getVertex5(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex2(), currentBox.getVertex6(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex3(), currentBox.getVertex7(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex4(), currentBox.getVertex8(), point) ||

                isPointCOnLineBetweenTwoPoints(currentBox.getVertex5(), currentBox.getVertex6(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex7(), currentBox.getVertex8(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex5(), currentBox.getVertex7(), point) ||
                isPointCOnLineBetweenTwoPoints(currentBox.getVertex6(), currentBox.getVertex8(), point))
            {
                return true;
            }
        }
        return false;
    }

    public boolean hitsBoundingBox(Point point, Scene scene)
    {
        ArrayList<BoundingBox> boundingBoxes = computeBoundingBoxes(scene.getShapes());
        return isPointOnABoundingBox(point, boundingBoxes);
    }

    private Point getPointInCameraSpace(int dimension, Raytracer raytracer, Camera camera, int i, int j)
    {
        double x = ((2 * (i + .5)) / dimension) - 1;
        double y = 1 - ((2 * (j + .5)) / dimension);
        double z = raytracer.computeDistanceToImagePlane(camera.getFieldOfView());
        return new Point(x, y, z);
    }

    public PPMImage drawBoundingBoxes(PPMImage finalImage, Scene scene, int dimension, Raytracer raytracer)
    {
        ArrayList<BoundingBox> boundingBoxes = computeBoundingBoxes(scene.getShapes());
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                Point currentPoint = this.getPointInCameraSpace(dimension, raytracer, scene.getCamera(), j, i);
//                System.out.println("Current point: " + currentPoint.toString()); // -2.000726465346332
                if (isPointOnABoundingBox(currentPoint, boundingBoxes))
                {
                    System.out.println("hit bounding box edge");
                    finalImage.addPixel(new Pixel(0, 0, 1), i, j);
                }
            }
        }
        return finalImage;
    }
}
