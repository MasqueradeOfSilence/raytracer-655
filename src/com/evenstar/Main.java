package com.evenstar;

import com.evenstar.model.Scene;
import com.evenstar.util.Raytracer;
import com.evenstar.util.SceneFileParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main
{
    private static String getSceneDescriptionFileName(String[] args)
    {
        return args[0];
    }

    private static int getSquareImageDimension(String[] args)
    {
        return Integer.parseInt(args[1]);
    }

    private static void readFileAndRaytrace(String sceneDescriptionFileName, int dimension)
    {
        Scanner scanner;
        SceneFileParser sceneFileParser = new SceneFileParser();
        try
        {
            scanner = new Scanner(new FileReader(sceneDescriptionFileName));
            // parse information from file into Scene object, then put the Scene object into the Raytracer.
            Scene scene = sceneFileParser.parseSceneFile(scanner);
            Raytracer raytracer = new Raytracer(scene);
            raytracer.render(dimension, sceneDescriptionFileName);
            scanner.close();
        }
        catch (FileNotFoundException e)
        {
            System.err.println("Oops! File wasn't found!");
            e.printStackTrace();
        }
    }

    public static void main(String[] args)
    {
	    System.out.println("Hello raytracer!");
	    // Scene name and image size should be read from command line.
        String sceneDescriptionFileName = getSceneDescriptionFileName(args);
        int squareImageDimension = getSquareImageDimension(args);
        System.out.println("Parsing image " + sceneDescriptionFileName + " with dimension " + squareImageDimension);

        readFileAndRaytrace(sceneDescriptionFileName, squareImageDimension);
    }
}
