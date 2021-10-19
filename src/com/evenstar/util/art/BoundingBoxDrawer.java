package com.evenstar.util.art;

import com.evenstar.model.Camera;
import com.evenstar.model.PPMImage;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Pixel;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.VectorOperations;
import com.evenstar.util.AcceleratedRaytracer;
import com.evenstar.util.Raytracer;

import java.util.ArrayList;

public class BoundingBoxDrawer
{
    public boolean isPointCOnLineBetweenTwoPoints(Point a, Point b, Point c)
    {
        a = new Point(a.getX(), a.getY(), 0);
        b = new Point(b.getX(), b.getY(), 0);
        c = new Point(c.getX(), c.getY(), 0);
        double distance1 = VectorOperations.distance(a.getVector(), c.getVector()) +
                VectorOperations.distance(b.getVector(), c.getVector());
        double distance2 = VectorOperations.distance(a.getVector(), b.getVector());
        return Math.abs(distance2 - distance1) < 0.0001;
    }

    private boolean almostEqual(Point coordinate, Point boundingBoxPoint)
    {
        if ((coordinate.getX() < boundingBoxPoint.getX() + 0.01 && coordinate.getX() > boundingBoxPoint.getX() - 0.01) &&
                (coordinate.getY() < boundingBoxPoint.getY() + 0.01 && coordinate.getY() > boundingBoxPoint.getY() - 0.01))
        {
            return true;
        }
        return false;
    }

    private boolean isPointOnBoundingBoxEdge(Point point, ArrayList<BoundingBox> boundingBoxes)
    {
        for (BoundingBox currentBox : boundingBoxes)
        {
            if (this.almostEqual(point, currentBox.getVertex1()) || this.almostEqual(point, currentBox.getVertex2())
                    || this.almostEqual(point, currentBox.getVertex3()) || this.almostEqual(point, currentBox.getVertex4())
                    || this.almostEqual(point, currentBox.getVertex5()) || this.almostEqual(point, currentBox.getVertex6())
                    || this.almostEqual(point, currentBox.getVertex7()) || this.almostEqual(point, currentBox.getVertex8()))
            {
                System.out.println("The point: " + point.toString());
                return true;
            }
            else if (isPointCOnLineBetweenTwoPoints(currentBox.getVertex1(), currentBox.getVertex2(), point) ||
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

    private Point getPointInCameraSpace(int dimension, Raytracer raytracer, Camera camera, int i, int j)
    {
        double x = ((2 * (i + .5)) / dimension) - 1;
        double y = 1 - ((2 * (j + .5)) / dimension);
        // Magic number for testing
        double z = raytracer.computeDistanceToImagePlane(camera.getFieldOfView()) + .16;
        x /= -z;
        y /= -z;
        z /= -z;
        return new Point(x, y, z);
    }
    private boolean doesRayHitBox(Point current, Camera camera, ArrayList<BoundingBox> boundingBoxes)
    {
        AcceleratedRaytracer ar = new AcceleratedRaytracer();
        Ray ray = new Ray(current, new Direction(camera.getLookFrom().getVector()));
        for (int i = 0; i < boundingBoxes.size(); i++)
        {
            BoundingBox currentBox = boundingBoxes.get(i);
            //doesRayIntersectBoundingBox
            if (ar.doesRayIntersectBoundingBox(ray, currentBox))
            {
                return true;
            }
        }
        return false;
    }

    // It's not perfect, but it at least provides a visual of some approximation.
    public PPMImage drawBoundingBoxes(PPMImage finalImage, Scene scene, int dimension, Raytracer raytracer,
                                      ArrayList<BoundingBox> boundingBoxes)
    {
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                Point currentPoint = this.getPointInCameraSpace(dimension, raytracer, scene.getCamera(), j, i);
                if (this.isPointOnBoundingBoxEdge(currentPoint, boundingBoxes))
                {
                    finalImage.addPixel(new Pixel(1, 0, 0), i, j);
                }
                else if (this.doesRayHitBox(currentPoint, scene.getCamera(), boundingBoxes))
                {
                    finalImage.addPixel(new Pixel(0, 0, 1), i, j);
                }
            }
        }
        return finalImage;
    }
}
