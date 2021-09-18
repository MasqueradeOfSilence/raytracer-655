package com.evenstar.util;

import com.evenstar.model.PPMImage;
import com.evenstar.model.Scene;
public class Raytracer
{
    private final PPMRenderer ppmRenderer;

    public Raytracer()
    {
        ppmRenderer = new PPMRenderer();
    }

    public void render(Scene scene, int dimensions)
    {
        // antialiasing logic will double dimensions here
        this.raytrace(scene, dimensions);
    }

    /**
     * Raytrace only currently works for square pixels
     * @param scene: our 3D scene represented in a data structure
     * @param dimension: how big our image will be. Can double for antialiasing
     */
    private void raytrace(Scene scene, int dimension)
    {
        // with antialiasing, look here
        PPMImage renderedImage = new PPMImage(dimension, dimension);
        for (int i = 0; i < dimension; i++)
        {
            for (int j = 0; j < dimension; j++)
            {
                
            }
        }
    }
}
