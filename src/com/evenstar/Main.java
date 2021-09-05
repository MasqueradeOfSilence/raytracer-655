package com.evenstar;

public class Main
{

    public static void main(String[] args)
    {
	    System.out.println("Hello raytracer!");
	    PPMRenderer renderer = new PPMRenderer();
	    renderer.generateDefaultGradientImage();
    }
}
