import com.evenstar.model.Scene;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.textures.Reflective;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.util.SceneFileParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Scanner;

public class SceneFileParserTest
{
    @Test
    void parseSceneFile() throws FileNotFoundException
    {
        // Testing diffuse.rayTracing
        SceneFileParser parser = new SceneFileParser();
        Scanner scanner = new Scanner(new FileReader("img/diffuse.rayTracing"));
        Scene scene = parser.parseSceneFile(scanner);
        assertEquals(scene.getCamera().getLookAt().getVector(), new Vector3D(0, 0, 0));
        assertEquals(scene.getCamera().getLookFrom().getVector(), new Vector3D(0, 0, 1));
        assertEquals(scene.getCamera().getLookUp().getVector(), new Vector3D(0, 1, 0));
        assertEquals(scene.getCamera().getFieldOfView(), 28);
        assertEquals(scene.getDirectionalLight().getDirectionToLight(), new Vector3D(1, 0, 0));
        assertEquals(scene.getDirectionalLight().getLightColor().getVector(), new Vector3D(1, 1, 1));
        assertEquals(scene.getAmbientLight().getLightColor().getVector(), new Vector3D(.1, .1, .1));
        assertEquals(scene.getBackgroundColor().getVector(), new Vector3D(.2, .2, .2));
        ArrayList<Shape> shapes = scene.getShapes();
        assertEquals(shapes.size(), 5);
        assertEquals(shapes.get(0), new Sphere(new Point(.35, 0, -.1), .05,
                new Diffuse(new Vector3D(1, 1, 1), new Vector3D(1, 1, 1), 4)));
        assertEquals(shapes.get(1), new Sphere(new Point(.2, 0, -.1), .075,
                new Diffuse(new Vector3D(1, 0, 0), new Vector3D(.5, 1, .5), 32)));
        assertEquals(shapes.get(2), new Sphere(new Point(-.6, 0, 0), .3,
                new Diffuse(new Vector3D(0, 1, 0), new Vector3D(.5, 1, .5), 32)));
        assertEquals(shapes.get(3), new Triangle(new Point(-.2, .1, .1), new Point(-.2, -.5, .2),
                new Point(-.2, .1, -.3), new Diffuse(new Vector3D(1, 1, 0),
                new Vector3D(1, 1, 1), 4)));
        assertEquals(shapes.get(4), new Triangle(new Point(.3, -.3, -.4), new Point(0, .3, -.1),
                new Point(-.3, -.3, .2), new Diffuse(new Vector3D(0, 0, 1),
                new Vector3D(1, 1, 1), 32)));

        scanner.close();

        // Testing sceneII.rayTracing
        Scanner scanner2 = new Scanner(new FileReader("img/SceneII.rayTracing"));
        Scene scene2 = parser.parseSceneFile(scanner2);
        assertEquals(scene2.getCamera().getLookAt().getVector(), new Vector3D(0, 0, 0));
        assertEquals(scene2.getCamera().getLookFrom().getVector(), new Vector3D(0, 0, 1.2));
        assertEquals(scene2.getCamera().getLookUp().getVector(), new Vector3D(0, 1, 0));
        assertEquals(scene2.getCamera().getFieldOfView(), 55);
        assertEquals(scene2.getDirectionalLight().getDirectionToLight(), new Vector3D(0, 1, 0));
        assertEquals(scene2.getDirectionalLight().getLightColor().getVector(), new Vector3D(1, 1, 1));
        assertEquals(scene2.getAmbientLight().getLightColor().getVector(), new Vector3D(0, 0, 0));
        assertEquals(scene2.getBackgroundColor().getVector(), new Vector3D(.2, .2, .2));
        ArrayList<Shape> shapes2 = scene2.getShapes();
        assertEquals(shapes2.size(), 3);
        assertEquals(shapes2.get(0), new Sphere(new Point(0, .3, 0), .2,
                new Reflective(new Vector3D(.75, .75, .75))));
        assertEquals(shapes2.get(1), new Triangle(new Point(0, -.5, .5), new Point(1, .5, 0),
                new Point(0, -.5, -.5), new Diffuse(new Vector3D(0, 0, 1),
                new Vector3D(1, 1, 1), 4)));
        assertEquals(shapes2.get(2), new Triangle(new Point(0, -.5, .5), new Point(0, -.5, -.5),
                new Point(-1, .5, 0), new Diffuse(new Vector3D(1, 1, 0),
                new Vector3D(1, 1, 1), 4)));
        scanner2.close();
    }
}
