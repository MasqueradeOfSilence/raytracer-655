package com.evenstar;

import com.evenstar.model.Raytracer;
import com.evenstar.util.PPMRenderer;

public class Main
{

    public static void main(String[] args)
    {
	    System.out.println("Hello raytracer!");
	    Raytracer raytracer = new Raytracer();
	    raytracer.generateHelloWorldGraphicsImage();
    }
}
