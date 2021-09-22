package com.evenstar.model;

import com.evenstar.model.lights.AmbientLight;
import com.evenstar.model.lights.DirectionalLight;
import com.evenstar.model.lights.Light;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.textures.Material;
import com.evenstar.model.vectors.Color;

import java.util.ArrayList;

public class Scene
{
    private Camera camera;
    // Additional lights (beyond the standard ambient and directional)
    private ArrayList<Light> miscellaneousLights;
    private Color backgroundColor;
    private ArrayList<Shape> shapes;
    private DirectionalLight directionalLight;
    private AmbientLight ambientLight;

    public Scene()
    {
        this.miscellaneousLights = new ArrayList<>();
        this.shapes = new ArrayList<>();
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    public void setMiscellaneousLights(ArrayList<Light> miscellaneousLights)
    {
        this.miscellaneousLights = miscellaneousLights;
    }

    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }


    public void setShapes(ArrayList<Shape> shapes)
    {
        this.shapes = shapes;
    }

    public void setDirectionalLight(DirectionalLight directionalLight)
    {
        this.directionalLight = directionalLight;
    }

    public void setAmbientLight(AmbientLight ambientLight)
    {
        this.ambientLight = ambientLight;
    }

    public void addShape(Shape shape)
    {
        this.shapes.add(shape);
    }

    public Camera getCamera()
    {
        return camera;
    }

    public ArrayList<Light> getMiscellaneousLights()
    {
        return miscellaneousLights;
    }

    public Color getBackgroundColor()
    {
        return backgroundColor;
    }

    public ArrayList<Shape> getShapes()
    {
        return shapes;
    }

    public DirectionalLight getDirectionalLight()
    {
        return directionalLight;
    }

    public AmbientLight getAmbientLight()
    {
        return ambientLight;
    }

    @Override
    public String toString()
    {
        return "Scene{" +
                "camera=" + camera + "\n" +
                "misc lights=" + miscellaneousLights + "\n" +
                "backgroundColor=" + backgroundColor + "\n" +
                "shapes=" + shapes + "\n" +
                "directionalLight=" + directionalLight + "\n" +
                "ambientLight=" + ambientLight +
                '}';
    }
}