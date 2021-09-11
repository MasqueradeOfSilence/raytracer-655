package com.evenstar;

import com.evenstar.util.Raytracer;

public class Main
{

    public static void main(String[] args)
    {
	    System.out.println("Hello raytracer!");
	    Raytracer raytracer = new Raytracer();
        raytracer.raytraceHelloWorldImage();
        raytracer.raytraceBlueSkyImage();
        // Future: Prompt for menu options.
    }
}
