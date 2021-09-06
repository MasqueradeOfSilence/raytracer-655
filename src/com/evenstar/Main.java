package com.evenstar;

import com.evenstar.util.PPMRenderer;

public class Main
{

    public static void main(String[] args)
    {
	    System.out.println("Hello raytracer!");
	    PPMRenderer renderer = new PPMRenderer();
	    renderer.generateDefaultGradientImage();
    }
}
