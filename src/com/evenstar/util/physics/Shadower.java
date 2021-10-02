package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.model.lights.Light;
import com.evenstar.model.lights.PointLight;
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
        else if (ClassIdentifier.isPointLight(light))
        {
            Point offsetOrigin = getOffsetPoint(hitPoint);
            PointLight pointLight = (PointLight) light;
            return new Ray(offsetOrigin, new Direction(pointLight.getLocation().getVector()));
        }
        return null;
    }

    public boolean isInShadow(Scene scene, Shape shape, Hit hit)
    {
        Ray shadowRay = this.computeShadowRay(hit.getHitPoint(), scene.getDirectionalLight());
//        ArrayList<Hit> pointHits;
//        for (int i = 0; i < scene.getMiscellaneousLights().size(); i++)
//        {
//            Light currentLight = scene.getMiscellaneousLights().get(i);
//            if (ClassIdentifier.isPointLight(currentLight))
//            {
//                PointLight pointLight = (PointLight) currentLight;
//                Ray shadowRayPoint = this.computeShadowRay(hit.getHitPoint(), pointLight);
//                if (shadowRayPoint != null)
//                {
//                    pointHits = this.intersector.computeRayShapeHits(shadowRayPoint, scene.getShapes());
//                    if (pointHits.size() > 0)
//                    {
//                        return true;
//                    }
//                }
//            }
//        }
        if (shadowRay == null)
        {
            return false;
        }
        ArrayList<Hit> hits = this.intersector.computeRayShapeHits(shadowRay, scene.getShapes());
        return hits.size() > 0;
    }
}
