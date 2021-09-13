package com.evenstar.util;

import com.evenstar.model.*;
import com.evenstar.model.shapes.Hit;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.vectors.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PPMRenderer
{
    public void writeImageToFile(PPMImage image, String name)
    {
        assert image.getPixels().size() > 0;
        try
        {
            FileWriter imageFileWriter = new FileWriter(name);
            imageFileWriter.write(image.getBeginningPartOfPPM());
            for (int i = 0; i < image.getPixels().size(); i++)
            {
                for (int j = 0; j < image.getPixels().get(i).size(); j++)
                {
                    imageFileWriter.write(image.getPixels().get(i).get(j) + " ");
                }
                imageFileWriter.write("\n");
            }
            imageFileWriter.close();
            System.out.println("Wrote image " + name);
        }
        catch (IOException e)
        {
            System.out.println("Oops! An error occurred with file I/O. Lmao");
            e.printStackTrace();
        }
    }

    public String computeProgress(int height, int currentScanLine)
    {
        return Math.round(((double)(height - 1 - currentScanLine) / (height - 1)) * 100) + "%";
    }

    public Color translateColorFrom1To255Scale(Color color)
    {
        int int_r = (int)(255.999 * color.r());
        int int_g = (int)(255.999 * color.g());
        int int_b = (int)(255.999 * color.b());
        return new Color(int_r, int_g, int_b);
    }

    public Color skyColorWithSphereAndGround(Ray ray, ArrayList<Shape> shapes)
    {
        Hit hit = new Hit();
        GeometricCalculator calculator = new GeometricCalculator();
        if (calculator.computeHits(ray, 0, Constants.infinity, hit, shapes))
        {
            return new Color(VectorOperations.multiplyByScalar((VectorOperations.addVectors(hit.getNormal(),
                    new Color(1, 1, 1).getVector())), 0.5));
        }
        Direction unitDirection = new Direction(ray.getDirection().getVector().getUnitVector());
        double t = 0.5 * (unitDirection.getY() + 1.0);
        Color firstColor = new Color(VectorOperations.multiplyByScalar(new Color(1.0, 1.0, 1.0).getVector(), 1.0 - t));
        Color secondColor = new Color(VectorOperations.multiplyByScalar(new Color(0.5, 0.7, 1.0).getVector(), t));
        return new Color(VectorOperations.addVectors(firstColor.getVector(), secondColor.getVector()));
    }

    public Color skyColorWithSphere(Ray ray)
    {
        Sphere normalSphere = new Sphere(new Point(0, 0, -1), 0.5);
        double t = MathCalculations.sphereHitByRay(normalSphere, ray);
        if (t > 0.0)
        {
            Vector3D vector = VectorOperations.subtractVectors(ray.at(t).getVector(), new Vector3D(0, 0, -1));
            Vector3D n = vector.getUnitVector();
            return new Color(VectorOperations.multiplyByScalar(new Vector3D(n.getX() + 1, n.getY() + 1, n.getZ() + 1), 0.5));
        }
        Direction unitDirection = ray.getDirection();
        // Create a gradient from blue to white with a red sphere
        t = 0.5 * (unitDirection.getY() + 1.0);
        Color firstColor = new Color(VectorOperations.multiplyByScalar(new Color(1.0, 1.0, 1.0).getVector(), 1.0 - t));
        Color secondColor = new Color(VectorOperations.multiplyByScalar(new Color(0.5, 0.7, 1.0).getVector(), t));
        return new Color(VectorOperations.addVectors(firstColor.getVector(), secondColor.getVector()));
    }

    public Direction computeRayDirectionBasedOnCamera(Camera camera, double u, double v)
    {
        Point point1 = new Point(VectorOperations.multiplyByScalar(camera.getHorizontal().getVector(), u));
        Point point2 = new Point(VectorOperations.multiplyByScalar(camera.getVertical().getVector(), v));
        Point point3 = new Point(VectorOperations.addVectors(camera.getLowerLeftCorner().getVector(), point1.getVector()));
        Point point4 = new Point(VectorOperations.addVectors(point3.getVector(), point2.getVector()));
        return new Direction(VectorOperations.subtractVectors(point4.getVector(), camera.getOrigin().getVector()));
    }
}
