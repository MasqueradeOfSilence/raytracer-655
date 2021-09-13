package com.evenstar.util;

import com.evenstar.model.Ray;
import com.evenstar.model.shapes.Hit;
import com.evenstar.model.shapes.Shape;

import java.util.ArrayList;

public class GeometricCalculator
{
    public boolean computeHits(Ray ray, double tMin, double tMax, Hit hit, ArrayList<Shape> shapes)
    {
        // Double check -- risk zone
        boolean hitAnything = false;
        double closestHitSoFar = tMax;
        for (int i = 0; i < shapes.size(); i++)
        {
            Shape currentShape = shapes.get(i);
            Hit currentHit = currentShape.hitByRay(ray, tMin, closestHitSoFar, hit);
            if (currentHit != null)
            {
                hitAnything = true;
                closestHitSoFar = hit.getT();
            }
        }
        return hitAnything;
    }
}
