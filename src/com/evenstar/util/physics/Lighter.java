package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.AreaLight;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.model.lights.Light;
import com.evenstar.model.lights.PointLight;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.textures.Phong;
import com.evenstar.model.vectors.*;
import com.evenstar.util.ClassIdentifier;
import com.evenstar.util.Constants;

import java.util.ArrayList;

public class Lighter
{
    private boolean isDirectionalLight(Light light)
    {
        return light.getClass().toString().contains("Directional");
    }

    private Color computeAmbient(double ambientCoefficient, Scene scene, Diffuse diffuse)
    {
        Vector3D combined = VectorOperations.multiplyVectors(diffuse.getVector(),
                scene.getAmbientLight().getLightColor().getVector());
        return new Color(VectorOperations.multiplyByScalar(combined,
                ambientCoefficient));
    }

    /**
     * Formula: kd I max(0, n · l)
     * Source: http://www.cs.cornell.edu/courses/cs4620/2011fa/lectures/08raytracingWeb.pdf
     * @param diffuseColor the computed diffuse color (for a bare-bones implementation, we would just return this)
     * @param diffuseCoefficient parameter to strengthen/weaken the diffuse. Using 1 is fine
     * @param normalAtHitPoint the normal of the object where we've hit it
     * @param light we will want to iterate through all lights
     * @param directionToLight if the light is directional, this will simply be the light's direction
     * @return diffuse component
     */
    private Color computeDiffuse(Color diffuseColor, double diffuseCoefficient, Vector3D normalAtHitPoint,
                                 Light light, Direction directionToLight)
    {
        Vector3D strengthened = VectorOperations.multiplyByScalar(diffuseColor.getVector(),
                diffuseCoefficient);

        Color strengthenedColor = new Color(VectorOperations.multiplyVectors(strengthened,
                light.getLightColor().getVector()));
        if (this.isDirectionalLight(light))
        {
            double dotProduct = VectorOperations.dotProduct(normalAtHitPoint, directionToLight.getVector());
            double max = Math.max(0, dotProduct);
            return new Color(VectorOperations.multiplyByScalar(strengthenedColor.getVector(), max));
        }
        return strengthenedColor;
    }

    /**
     * @param normal the normal we've hit on the object
     * @param directionToLight if the light is directional, this will simply be the light's direction
     * @param specularCoefficient Not the phong constant. Just a scalar.
     * @return specular/Phong component of the lighting model
     */
    private Color computeSpecular(Vector3D normal, Direction directionToLight, double specularCoefficient,
                                  Diffuse diffuse, Vector3D hitPoint, Ray ray, int n)
    {
        Vector3D hitToCamera = VectorOperations.subtractVectors(hitPoint, ray.getOrigin().getVector());
        hitToCamera.normalize();
        double dotProduct = Math.max(VectorOperations.dotProduct(normal, directionToLight.getVector()), 0);
        Vector3D shiny = VectorOperations.subtractVectors(VectorOperations.multiplyByScalar(VectorOperations.
                multiplyByScalar(normal, 2), dotProduct), directionToLight.getVector());
        double factor = diffuse.getPhongConstant();
        if (n != 1)
        {
            factor = n;
        }
        Vector3D specular = VectorOperations.multiplyByScalar(diffuse.getSpecularHighlight(),
                Math.pow((VectorOperations.dotProduct(hitToCamera, shiny)), factor));

        return new Color(VectorOperations.multiplyByScalar(specular, specularCoefficient));
    }

    private Color phongLighting(Color ambient, Color diffuse, Color specular)
    {
        double newX = Math.max(Math.min(ambient.getVector().getX() + diffuse.getVector().getX() +
                specular.getVector().getX(), 1), 0);
        double newY = Math.max(Math.min(ambient.getVector().getY() + diffuse.getVector().getY() +
                specular.getVector().getY(), 1), 0);
        double newZ = Math.max(Math.min(ambient.getVector().getZ() + diffuse.getVector().getZ() +
                specular.getVector().getZ(), 1), 0);
        return new Color(newX, newY, newZ);
    }

    private Direction pointLightDirection(Point originOfPointLight, Point hitPoint)
    {
        Vector3D minuend = VectorOperations.subtractVectors(originOfPointLight.getVector(),
                hitPoint.getVector());
        minuend.normalize();
        return new Direction(minuend);
    }

    // This gives us soft shadows and light falloff.
    private Vector3D computeLightIntensityAtPoint(Direction lightDirection, double baseIntensity, Color lightColor)
    {
        Vector3D scaled = VectorOperations.multiplyByScalar(lightColor.getVector(), baseIntensity);
        double denominator = 4 * Math.PI * lightDirection.getVector().length();
        return VectorOperations.divideByScalar(scaled, denominator);
    }

    private boolean isInReachOfPointLight(double intersectionDistance, double distanceBetweenHitPointAndLight)
    {
        return Math.abs(intersectionDistance) > Math.abs(distanceBetweenHitPointAndLight);
    }

    public Color getFinalColor(Color baseColor, Vector3D normalAtHitPoint, DirectionalLight directionalLight, Diffuse diffuseMaterial,
                                      Vector3D hitPoint, Ray ray, Scene scene, int n, double specularCoefficient)
    {
        ArrayList<Color> colorsToCombine = new ArrayList<>();
        if (directionalLight.isOn())
        {
            // Ambient lights won't have a direction, but all others will, and no ambient lights will be passed here
            Direction directionToLight = directionalLight.getDirectionToLight();
            Color ambient = computeAmbient(Constants.DEFAULT_COEFFICIENT, scene, diffuseMaterial);
            Color diffuse = computeDiffuse(baseColor, Constants.DEFAULT_COEFFICIENT, normalAtHitPoint, directionalLight,
                    directionToLight);
            Color specular = computeSpecular(normalAtHitPoint, directionToLight,
                    specularCoefficient, diffuseMaterial, hitPoint, ray, n);
            colorsToCombine.add(this.phongLighting(ambient, diffuse, specular));
        }
        for (int i = 0; i < scene.getMiscellaneousLights().size(); i++)
        {
            Light currentLight = scene.getMiscellaneousLights().get(i);
            if (ClassIdentifier.isPointLight(currentLight) && currentLight.isOn())
            {
                PointLight pointLight = (PointLight) currentLight;
                Direction directionToLight = pointLightDirection(pointLight.getLocation(), new Point(hitPoint));
                // Distance from hit point to ray origin, vs. hit point to point light origin
                double hitPointToRayOrigin = VectorOperations.subtractVectors(hitPoint, ray.getOrigin().getVector()).length();
                double hitPointToPointLightOrigin = VectorOperations.subtractVectors(hitPoint, pointLight.getLocation().getVector()).length();
                if (!isInReachOfPointLight(hitPointToRayOrigin, hitPointToPointLightOrigin))
                {
                    continue;
                }
                Color ambient = computeAmbient(Constants.DEFAULT_COEFFICIENT, scene, diffuseMaterial);
                Color diffuse = computeDiffuse(baseColor, Constants.DEFAULT_COEFFICIENT, normalAtHitPoint, directionalLight,
                        directionToLight);
                Color specular = computeSpecular(normalAtHitPoint, directionToLight,
                        Constants.DEFAULT_COEFFICIENT, diffuseMaterial, hitPoint, ray, n);
                Vector3D phongVector = this.phongLighting(ambient, diffuse, specular).getVector();
                Vector3D sum = VectorOperations.addVectors(phongVector, computeLightIntensityAtPoint(directionToLight,
                        1, pointLight.getLightColor()));
                Vector3D toReturn = new Vector3D(Math.min(sum.getX(), 1), Math.min(sum.getY(), 1), Math.min(sum.getZ(), 1));
                colorsToCombine.add(new Color(toReturn));
            }
            else if (ClassIdentifier.isAreaLight(currentLight) && currentLight.isOn())
            {
                AreaLight areaLight = (AreaLight) currentLight;
                Direction directionToLight = pointLightDirection(areaLight.getLocation(), new Point(hitPoint));
                // Distance from hit point to ray origin, vs. hit point to point light origin
                double hitPointToRayOrigin = VectorOperations.subtractVectors(hitPoint, ray.getOrigin().getVector()).length();
                double hitPointToPointLightOrigin = VectorOperations.subtractVectors(hitPoint, areaLight.getLocation().getVector()).length();
                if (!isInReachOfPointLight(hitPointToRayOrigin, hitPointToPointLightOrigin))
                {
                    continue;
                }
                Color ambient = computeAmbient(Constants.DEFAULT_COEFFICIENT, scene, diffuseMaterial);
                Color diffuse = computeDiffuse(baseColor, Constants.DEFAULT_COEFFICIENT, normalAtHitPoint, directionalLight,
                        directionToLight);
                Color specular = computeSpecular(normalAtHitPoint, directionToLight,
                        Constants.DEFAULT_COEFFICIENT, diffuseMaterial, hitPoint, ray, n);
                Vector3D phongVector = this.phongLighting(ambient, diffuse, specular).getVector();
                Vector3D sum = VectorOperations.addVectors(phongVector, computeLightIntensityAtPoint(directionToLight,
                        1, areaLight.getLightColor()));
                Vector3D toReturn = new Vector3D(Math.min(sum.getX(), 1), Math.min(sum.getY(), 1), Math.min(sum.getZ(), 1));
                colorsToCombine.add(new Color(toReturn));
            }
        }
        if (colorsToCombine.size() > 0)
        {
            double runningRed = 0;
            double runningGreen = 0;
            double runningBlue = 0;
            for (int i = 0; i < colorsToCombine.size(); i++)
            {
                Color current = colorsToCombine.get(i);
                runningRed += current.r();
                runningGreen += current.g();
                runningBlue += current.b();
            }
            runningRed = Math.max(Math.min(runningRed, 1), 0);
            runningGreen = Math.max(Math.min(runningGreen, 1), 0);
            runningBlue = Math.max(Math.min(runningBlue, 1), 0);
            return new Color(runningRed, runningGreen, runningBlue);
        }
        return baseColor;
    }
}
