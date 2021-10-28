import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.util.physics.SubspaceComputer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubspaceComputerTest
{
    @Test
    void computeBoxAroundScene()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = new Scene();
        Sphere sphere1 = new Sphere(new Point(0.35, 0, -0.1), 0.05, new Diffuse(new Vector3D(1, 1, 1),
                new Vector3D(1, 1, 1), 4));
        Sphere sphere2 = new Sphere(new Point(0.2, 0, -0.1), 0.075, new Diffuse(new Vector3D(1, 0, 0),
                new Vector3D(0.5, 1, 0.5), 32));
        Sphere sphere3 = new Sphere(new Point(-0.6, 0, 0), 0.3, new Diffuse(new Vector3D(0, 1, 0),
                new Vector3D(0.5, 1, 0.5), 32));
        Triangle triangle1 = new Triangle(new Point(-.2, .1, .1), new Point(-.2, -.5, .2),
                new Point(-.2, .1, -.3), new Diffuse(new Vector3D(1, 1, 0),
                new Vector3D(1, 1, 1), 4));
        Triangle triangle2 = new Triangle(new Point(.3, -.3, -.4), new Point(0, .3, -.1),
                new Point(-.3, -.3, .2), new Diffuse(new Vector3D(0, 0, 1),
                new Vector3D(1, 1, 1), 32));
        scene.addShape(sphere1);
        scene.addShape(sphere2);
        scene.addShape(sphere3);
        scene.addShape(triangle1);
        scene.addShape(triangle2);
        BoundingBox boxAroundShape = subspaceComputer.computeBoxAroundScene(scene);
        double highestX = boxAroundShape.getVertex2().getX();
        double lowestX = boxAroundShape.getVertex7().getX();
        double highestY = boxAroundShape.getVertex2().getY();
        double lowestY = boxAroundShape.getVertex7().getY();
        double highestZ = boxAroundShape.getVertex2().getZ();
        double lowestZ = boxAroundShape.getVertex7().getZ();
        // Adding the delta parameter for small rounding differences
        assertEquals(highestX, 0.4, 0.01);
        assertEquals(lowestX, -0.9, 0.01);
        assertEquals(highestY, 0.3, 0.01);
        assertEquals(lowestY, -0.5, 0.01);
        assertEquals(highestZ, 0.3, 0.01);
        assertEquals(lowestZ, -0.4, 0.01);
    }
}