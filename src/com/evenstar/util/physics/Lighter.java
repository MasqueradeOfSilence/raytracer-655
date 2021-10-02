package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.model.lights.Light;
import com.evenstar.model.lights.PointLight;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.vectors.*;
import com.evenstar.util.ClassIdentifier;
import com.evenstar.util.Constants;

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
                                  Diffuse diffuse, Vector3D hitPoint, Ray ray)
    {
        Vector3D hitToCamera = VectorOperations.subtractVectors(hitPoint, ray.getOrigin().getVector());
        hitToCamera.normalize();
        double dotProduct = Math.max(VectorOperations.dotProduct(normal, directionToLight.getVector()), 0);
        Vector3D shiny = VectorOperations.subtractVectors(VectorOperations.multiplyByScalar(VectorOperations.
                multiplyByScalar(normal, 2), dotProduct), directionToLight.getVector());
        Vector3D specular = VectorOperations.multiplyByScalar(diffuse.getSpecularHighlight(),
                Math.pow((VectorOperations.dotProduct(hitToCamera, shiny)), diffuse.getPhongConstant()));

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

    private Direction pointLightDirection(Point originOfPointLight, Point surfacePoint)
    {
        Vector3D numerator = VectorOperations.subtractVectors(originOfPointLight.getVector(),
                surfacePoint.getVector());
        double denominator = numerator.length();
        Vector3D result = VectorOperations.divideByScalar(numerator, denominator);
        return new Direction(result);
    }

    public Color getFinalColor(Color baseColor, Vector3D normalAtHitPoint, Light light, Diffuse diffuseMaterial,
                                      Vector3D hitPoint, Ray ray, Scene scene)
    {
        // Ambient lights won't have a direction, but all others will, and no ambient lights will be passed here
        Direction directionToLight = ((DirectionalLight)light).getDirectionToLight();
        Color ambient = computeAmbient(Constants.DEFAULT_COEFFICIENT, scene, diffuseMaterial);
        Color diffuse = computeDiffuse(baseColor, Constants.DEFAULT_COEFFICIENT, normalAtHitPoint, light,
                directionToLight);
        Color specular = computeSpecular(normalAtHitPoint, directionToLight,
                Constants.DEFAULT_COEFFICIENT, diffuseMaterial, hitPoint, ray);
//        for (int i = 0; i < scene.getMiscellaneousLights().size(); i++)
//        {
//            Light currentLight = scene.getMiscellaneousLights().get(i);
//            if (ClassIdentifier.isPointLight(currentLight))
//            {
//                PointLight pointLight = (PointLight) currentLight;
//                Direction locationOfPointLight = this.pointLightDirection(pointLight.getLocation(), new Point(hitPoint));
//                Color diffusePoint = computeDiffuse(baseColor, Constants.DEFAULT_COEFFICIENT, normalAtHitPoint, pointLight,
//                        locationOfPointLight);
//                Color specularPoint = computeSpecular(normalAtHitPoint, locationOfPointLight, Constants.DEFAULT_COEFFICIENT,
//                        diffuseMaterial, hitPoint, ray);
//                diffuse = new Color(VectorOperations.addVectors(diffuse.getVector(), diffusePoint.getVector()));
//                specular = new Color(VectorOperations.addVectors(specular.getVector(), specularPoint.getVector()));
//            }
//        }
        return this.phongLighting(ambient, diffuse, specular);
    }
}
