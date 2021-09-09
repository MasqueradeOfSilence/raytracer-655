package com.evenstar.util;

import com.evenstar.model.PPMImage;
import com.evenstar.util.PPMRenderer;

public class Raytracer
{
    private final PPMRenderer ppmRenderer;

    public Raytracer()
    {
        ppmRenderer = new PPMRenderer();
    }

    public void generateHelloWorldGraphicsImage()
    {
        ppmRenderer.generateDefaultGradientImage();
    }

    public void raytraceBlueSkyImage()
    {
        // Height was computed by (width / aspectRatio)
        PPMImage blueSkyImage = new PPMImage(400, 225);
        // Next: Create camera object, which will be at the origin right now
    }
}
