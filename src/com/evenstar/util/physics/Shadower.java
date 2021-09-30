package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.model.lights.Light;
import com.evenstar.model.physics.Hit;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;
import com.evenstar.util.ClassIdentifier;
import com.evenstar.util.Constants;

import java.util.ArrayList;

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
            return new Ray(offsetOrigin, directionalLight.getDirectionToLight());
        }
        return null;
    }

    public boolean isInShadow(Scene scene, Shape shape, Hit hit)
    {
        Ray shadowRay = this.computeShadowRay(hit.getHitPoint(), scene.getDirectionalLight());
        if (shadowRay == null)
        {
            return false;
        }
        ArrayList<Hit> hits = this.intersector.computeRayShapeHits(shadowRay, scene.getShapes());
        return hits.size() > 0;
    }
}
