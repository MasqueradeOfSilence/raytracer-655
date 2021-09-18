import com.evenstar.model.Scene;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.textures.Diffuse;
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
        scanner.close();

        // Testing sceneII.rayTracing
        
    }
}
