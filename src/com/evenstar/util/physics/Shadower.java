package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.AreaLight;
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

    public Shadower(Scene scene)
    {
        this.intersector = new Intersector(scene);
    }

    private Point getOffsetPoint(Point original)
    {
        Vector3D offsetVector = VectorOperations.addScalar(original.getVector(), Constants.DEFAULT_OFFSET);
        return new Point(offsetVector);
    }

    private Ray computeShadowRay(Point hitPoint, Light light)
    {
        if (ClassIdentifier.isDirectionalLight(light) && light.isOn())
        {
            Point offsetOrigin = getOffsetPoint(hitPoint);
            DirectionalLight directionalLight = (DirectionalLight) light;
            return new Ray(offsetOrigin, directionalLight.getDirectionToLight());
        }
        else if (ClassIdentifier.isPointLight(light) && light.isOn())
        {
            Point offsetOrigin = getOffsetPoint(hitPoint);
            PointLight pointLight = (PointLight) light;
            return new Ray(offsetOrigin, new Direction(pointLight.getLocation().getVector()));
        }
        else if (ClassIdentifier.isAreaLight(light) && light.isOn())
        {
            Point offsetOrigin = getOffsetPoint(hitPoint);
            AreaLight areaLight = (AreaLight) light;
            return new Ray(offsetOrigin, new Direction(areaLight.getLocation().getVector()));
        }
        return null;
    }

    public boolean isInShadow(Scene scene, Hit hit)
    {
        // Area lights should not be in shadow
        if (ClassIdentifier.isEmissive(hit.getCorrespondingShape().getMaterial()))
        {
            return false;
        }
        Ray shadowRay = this.computeShadowRay(hit.getHitPoint(), scene.getDirectionalLight());
        if (shadowRay == null)
        {
            return false;
        }
        ArrayList<Shape> shapesMinusAreaLights = new ArrayList<>();
        ArrayList<Shape> sceneShapes = scene.getShapes();
        // Area lights should not cast shadows.
        for (int i = 0; i < sceneShapes.size(); i++)
        {
            Shape currentShape = scene.getShapes().get(i);
            if (!ClassIdentifier.isEmissive(currentShape.getMaterial()))
            {
                shapesMinusAreaLights.add(currentShape);
            }
        }
        ArrayList<Hit> hits = this.intersector.computeRayShapeHits(shadowRay, shapesMinusAreaLights, scene);
        for (int i = 0; i < scene.getMiscellaneousLights().size(); i++)
        {
            Light currentLight = scene.getMiscellaneousLights().get(i);
            if ((ClassIdentifier.isPointLight(currentLight) || ClassIdentifier.isAreaLight(currentLight))
                    && currentLight.isOn())
            {
                Ray newShadowRay = this.computeShadowRay(hit.getHitPoint(), currentLight);
                if (newShadowRay == null)
                {
                    continue;
                }
                hits.addAll(this.intersector.computeRayShapeHits(newShadowRay, scene.getShapes(), scene));
            }
        }
        return hits.size() > 0;
    }
}
