package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.model.lights.Light;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;
import com.evenstar.util.ClassIdentifier;
import com.evenstar.util.Constants;

public class Shadower
{
    private final Intersector intersector;

    public Shadower()
    {
        this.intersector = new Intersector();
    }

    private Point getOffsetPoint(Point original)
    {
        Vector3D offsetVector = VectorOperations.addScalar(original.getVector(), Constants.DEFAULT_OFFSET);
        return new Point(offsetVector);
    }

    private Ray computeShadowRay(Point hitPoint, Light light)
    {
        if (ClassIdentifier.isDirectionalLight(light))
        {
            Point offsetOrigin = getOffsetPoint(hitPoint);
            DirectionalLight directionalLight = (DirectionalLight) light;
            DirectionalLight newLight = new DirectionalLight(new Direction(-directionalLight.getDirectionToLight().getX(), directionalLight.getDirectionToLight().getY(), directionalLight.getDirectionToLight().getZ()), directionalLight.getLightColor());
            return new Ray(offsetOrigin, newLight.getDirectionToLight());
        }
        return null;
    }

    public boolean isInShadow(Scene scene, Shape shape)
    {
        // should I use hit point or normal?
        // normal = fully black, hit point = no color at all
        Ray shadowRay = this.computeShadowRay(shape.getHitPair().getHitPoint(), scene.getDirectionalLight());
        if (shadowRay == null)
        {
            return false;
        }

        for (int i = 0; i < scene.getShapes().size(); i++)
        {
            Shape currentShape = scene.getShapes().get(i);
            if (ClassIdentifier.isSphere(currentShape))
            {
                continue; // delete this
            }
            if (this.intersector.intersects(shadowRay, currentShape) != Constants.NO_INTERSECTION
                && !currentShape.equals(shape))
            {
                if (ClassIdentifier.isTriangle(currentShape))
                {
                    System.out.println("I have a triangle");
                }
                return true;
            }
        }
        return false;
    }
}
