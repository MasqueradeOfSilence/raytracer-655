package com.evenstar.util;

import com.evenstar.model.Camera;
import com.evenstar.model.Scene;
import com.evenstar.model.lights.AmbientLight;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.model.lights.PointLight;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.textures.Amber;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.textures.Glass;
import com.evenstar.model.textures.Reflective;
import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;

import java.util.Scanner;

public class SceneFileParser
{
    private void skipWord(Scanner scanner)
    {
        scanner.next();
    }

    private Scene handleTriangles(Scanner scanner, Scene scene)
    {
        Point vertex1 = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        Point vertex2 = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        Point vertex3 = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipWord(scanner);
        String materialType = scanner.next();
        switch (materialType)
        {
            case "Diffuse" ->
            {
                Vector3D xyz = new Vector3D(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                this.skipWord(scanner);
                Vector3D specularHighlight = new Vector3D(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                this.skipWord(scanner);
                int phongConstant = scanner.nextInt();
                Diffuse diffuse = new Diffuse(xyz, specularHighlight, phongConstant);
                Triangle triangle = new Triangle(vertex1, vertex2, vertex3, diffuse);
                scene.addShape(triangle);
            }
            case "Reflective" ->
            {
                Vector3D xyz = new Vector3D(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                Reflective reflective = new Reflective(xyz);
                Triangle triangle = new Triangle(vertex1, vertex2, vertex3, reflective);
                scene.addShape(triangle);
            }
            default ->
            {
                System.err.println("Not a valid material!");
            }
        }

        return scene;
    }

    private Scene handleSpheres(Scanner scanner, Scene scene)
    {
        this.skipWord(scanner);
        Point center = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipWord(scanner);
        double radius = scanner.nextDouble();
        this.skipWord(scanner);
        String materialType = scanner.next();
        Vector3D xyz = new Vector3D(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        switch (materialType)
        {
            case "Diffuse" ->
            {
                /* It may seem confusing, but diffuse objects have specular highlights
                    and phong constants, NOT reflective objects.*/
                this.skipWord(scanner);
                Vector3D specularHighlight = new Vector3D(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
                this.skipWord(scanner);
                int phongConstant = scanner.nextInt();
                Diffuse diffuse = new Diffuse(xyz, specularHighlight, phongConstant);
                Sphere sphere = new Sphere(center, radius, diffuse);
                scene.addShape(sphere);
            }
            case "Reflective" ->
            {
                Reflective reflective = new Reflective(xyz);
                Sphere sphere = new Sphere(center, radius, reflective);
                scene.addShape(sphere);
            }
            case "Glass" ->
            {
                Glass glass = new Glass(xyz);
                Sphere sphere = new Sphere(center, radius, glass);
                scene.addShape(sphere);
            }
            // A translucent material
            case "Amber" ->
            {
                Amber amber = new Amber(xyz);
                Sphere sphere = new Sphere(center, radius, amber);
                scene.addShape(sphere);
            }
            default -> System.err.println("Not a valid material");
        }
        return scene;
    }

    private Scene handlePointLights(Scanner scanner, Scene scene)
    {
        Point location = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipWord(scanner);
        Color color = new Color(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        PointLight pointLight = new PointLight(color, location);
        scene.addMiscLight(pointLight);
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
                    scene = this.handleSpheres(scanner, scene);
                    break;
                }
                case "triangle":
                {
                    // Reassigning "scene" because Java passes by value, not reference, and we modify it
                    scene = this.handleTriangles(scanner, scene);
                    break;
                }
                case "pointlight":
                {
                    scene = this.handlePointLights(scanner, scene);
                    break;
                }
                //<editor-fold desc="Not yet implemented switch cases">
                case "polygon":
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
        this.skipWord(scanner);
        return new Color(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
    }

    private AmbientLight getAmbientLight(Scanner scanner)
    {
        this.skipWord(scanner);
        return new AmbientLight(new Color(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble()));
    }

    private DirectionalLight getDirectionalLight(Scanner scanner)
    {
        this.skipWord(scanner);
        Direction directionToLight = new Direction(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipWord(scanner);
        Color directionalLightColor = new Color(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        return new DirectionalLight(directionToLight, directionalLightColor);
    }

    private Camera getCameraData(Scanner scanner)
    {
        this.skipWord(scanner);
        Point lookAt = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipWord(scanner);
        Point lookFrom = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipWord(scanner);
        Point lookUp = new Point(scanner.nextDouble(), scanner.nextDouble(), scanner.nextDouble());
        this.skipWord(scanner);
        int fieldOfView = scanner.nextInt();
        return new Camera(lookAt, lookFrom, lookUp, fieldOfView);
    }

    private Scene parseCameraInformation(Scanner scanner, Scene scene)
    {
        Camera camera = getCameraData(scanner);
        scene.setCamera(camera);
        return scene;
    }

    private Scene parseDirectionalLightInformation(Scanner scanner, Scene scene)
    {
        DirectionalLight directionalLight = getDirectionalLight(scanner);
        scene.setDirectionalLight(directionalLight);
        return scene;
    }

    private Scene parseAmbientLightInformation(Scanner scanner, Scene scene)
    {
        AmbientLight ambientLight = getAmbientLight(scanner);
        scene.setAmbientLight(ambientLight);
        return scene;
    }

    private Scene parseBackgroundColor(Scanner scanner, Scene scene)
    {
        Color backgroundColor = getBackgroundColor(scanner);
        scene.setBackgroundColor(backgroundColor);
        return scene;
    }

    private Scene parseCommonInformation(Scanner scanner, Scene scene)
    {
        scene = parseCameraInformation(scanner, scene);
        scene = parseDirectionalLightInformation(scanner, scene);
        scene = parseAmbientLightInformation(scanner, scene);
        scene = parseBackgroundColor(scanner, scene);
        return scene;
    }

    public Scene parseSceneFile(Scanner scanner)
    {
        Scene scene = new Scene();
        scene = parseCommonInformation(scanner, scene);
        // Shapes, lights, additional materials, textured shapes
        scene = readDynamicObjects(scanner, scene);

        System.out.println("Scene so far: " + scene.toString());

        return scene;
    }


}
