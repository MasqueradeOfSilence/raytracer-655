package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.AmbientLight;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.textures.Material;
import com.evenstar.model.textures.Specular;
import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;

import java.util.Scanner;

public class SceneFileParser
{
    private void skipLine(Scanner scanner)
    {
        scanner.next();
    }

    private Scene handleTriangles(Scanner scanner, Scene scene)
    {
        return scene;
    }

    private Scene handleSpheres(Scanner scanner, Scene scene)
    {
        this.skipLine(scanner);
        Point center = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipLine(scanner);
        double radius = scanner.nextDouble();
        String materialType = scanner.next();
        Vector3D xyz = new Vector3D(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        switch (materialType)
        {
            case "Reflective":
            {
                this.skipLine(scanner);
                Vector3D specularHighlight = new Vector3D(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                this.skipLine(scanner);
                int phongConstant = scanner.nextInt();
                Specular specular = new Specular(xyz, specularHighlight, phongConstant);
                Sphere sphere = new Sphere(center, radius, specular);
                scene.addShape(sphere);
                break;
            }
            case "Diffuse":
            {
                Diffuse diffuse = new Diffuse(xyz);
                Sphere sphere = new Sphere(center, radius, diffuse);
                scene.addShape(sphere);
                break;
            }
            default:
                break;
        }
        return scene;
    }

    private Scene readDynamicObjects(Scanner scanner, Scene scene)
    {
        while (scanner.hasNext())
        {
            String objectType = scanner.next();
            switch (objectType.toLowerCase())
            {
                case "sphere":
                {
                    // Reassigning "scene" because Java passes by value, not reference, and we modify it
                    scene = handleSpheres(scanner, scene);
                    break;
                }
                case "triangle":
                {
                    // Reassigning "scene" because Java passes by value, not reference, and we modify it
                    scene = handleTriangles(scanner, scene);
                    break;
                }
                //<editor-fold desc="Not yet implemented">
                case "polygon":
                {
                    break;
                }
                case "point_light":
                {
                    break;
                }
                case "area_light":
                {
                    break;
                }
                case "sphere_light":
                {
                    break;
                }
                case "material":
                {
                    break;
                }
                case "textured_sphere":
                {
                    break;
                }
                case "textured_triangle":
                {
                    break;
                }
                case "textured_polygon":
                {
                    break;
                }
                //</editor-fold>
                default:
                    break;
            }
        }
        return scene;
    }

    private Color getBackgroundColor(Scanner scanner)
    {
        this.skipLine(scanner);
        return new Color(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
    }

    private AmbientLight getAmbientLight(Scanner scanner)
    {
        this.skipLine(scanner);
        return new AmbientLight(new Color(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
    }

    private DirectionalLight getDirectionalLight(Scanner scanner)
    {
        this.skipLine(scanner);
        Direction directionToLight = new Direction(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipLine(scanner);
        Color directionalLightColor = new Color(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        return new DirectionalLight(directionToLight, directionalLightColor);
    }

    private Camera getCameraData(Scanner scanner)
    {
        this.skipLine(scanner);
        Direction lookAt = new Direction(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipLine(scanner);
        Direction lookFrom = new Direction(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipLine(scanner);
        Direction lookUp = new Direction(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipLine(scanner);
        int fieldOfView = scanner.nextInt();
        return new Camera(lookAt, lookFrom, lookUp, fieldOfView);
    }

    public Scene parseSceneFile(Scanner scanner, int dimension)
    {
        Scene scene = new Scene();

        // Camera
        Camera camera = getCameraData(scanner);
        scene.setCamera(camera);

        // Directional light
        DirectionalLight directionalLight = getDirectionalLight(scanner);
        scene.setDirectionalLight(directionalLight);

        // Ambient light
        AmbientLight ambientLight = getAmbientLight(scanner);
        scene.setAmbientLight(ambientLight);

        // Background color
        Color backgroundColor = getBackgroundColor(scanner);
        scene.setBackgroundColor(backgroundColor);

        // Shapes, lights, additional materials, textured shapes
        scene = readDynamicObjects(scanner, scene);

        System.out.println("Scene so far: " + scene.toString());

        return scene;
    }


}
