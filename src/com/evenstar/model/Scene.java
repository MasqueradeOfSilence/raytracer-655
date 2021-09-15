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
    private ArrayList<Light> lights;
    private Color backgroundColor;
    private ArrayList<Material> materials;
    private ArrayList<Shape> shapes;
    private DirectionalLight directionalLight;
    private AmbientLight ambientLight;

    public Scene()
    {
        this.lights = new ArrayList<>();
        this.materials = new ArrayList<>();
        this.shapes = new ArrayList<>();
    }

    public void setCamera(Camera camera)
    {
        this.camera = camera;
    }

    public void setLights(ArrayList<Light> lights)
    {
        this.lights = lights;
    }

    public void setBackgroundColor(Color backgroundColor)
    {
        this.backgroundColor = backgroundColor;
    }

    public void setMaterials(ArrayList<Material> materials)
    {
        this.materials = materials;
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

    // will need to re-generate
    @Override
    public String toString()
    {
        return "Scene{" +
                "camera=" + camera +
                ", lights=" + lights +
                ", backgroundColor=" + backgroundColor +
                ", materials=" + materials +
                ", shapes=" + shapes +
                ", directionalLight=" + directionalLight +
                ", ambientLight=" + ambientLight +
                '}';
    }
}
