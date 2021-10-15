import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.util.AcceleratedRaytracer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AcceleratedRaytracerTest
{
    /**
     * Java is super precise so I have to allow for a bit of error
     */
    @Test
    void createBoundingBoxSphere()
    {
        AcceleratedRaytracer ar = new AcceleratedRaytracer();

        /*
            Sphere 1
        */
        Sphere sphere1 = new Sphere(new Point(0.35, 0, -0.1), 0.05, new Diffuse(
                new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), 32
        ));
        BoundingBox boundingBox = ar.createBoundingBoxSphere(sphere1);
        // Front
        assertEquals(boundingBox.getVertex1(), new Point(0.3, 0.05, -0.05));
        assertEquals(boundingBox.getVertex2(), new Point(0.39999999999999997, 0.05, -0.05));
        assertEquals(boundingBox.getVertex3(), new Point(0.3, -0.05, -0.05));
        assertEquals(boundingBox.getVertex4(), new Point(0.39999999999999997, -0.05, -0.05));
        // Back
        assertEquals(boundingBox.getVertex5(), new Point(0.3, 0.05, -0.15000000000000002));
        assertEquals(boundingBox.getVertex6(), new Point(0.39999999999999997, 0.05, -0.15000000000000002));
        assertEquals(boundingBox.getVertex7(), new Point(0.3, -0.05, -0.15000000000000002));
        assertEquals(boundingBox.getVertex8(), new Point(0.39999999999999997, -0.05, -0.15000000000000002));

        /*
            Sphere 2
        */
        Sphere sphere2 = new Sphere(new Point(0.2, 0, -0.1), 0.075, new Diffuse(
           new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), 32
        ));
        boundingBox = ar.createBoundingBoxSphere(sphere2);
        // Front
        assertEquals(boundingBox.getVertex1(), new Point(0.125, 0.075, -0.02500000000000001));
        assertEquals(boundingBox.getVertex2(), new Point(0.275, 0.075, -0.02500000000000001));
        assertEquals(boundingBox.getVertex3(), new Point(0.125, -0.075, -0.02500000000000001));
        assertEquals(boundingBox.getVertex4(), new Point(0.275, -0.075, -0.02500000000000001));
        // Back
        assertEquals(boundingBox.getVertex5(), new Point(0.125, 0.075, -0.175));
        assertEquals(boundingBox.getVertex6(), new Point(0.275, 0.075, -0.175));
        assertEquals(boundingBox.getVertex7(), new Point(0.125, -0.075, -0.175));
        assertEquals(boundingBox.getVertex8(), new Point(0.275, -0.075, -0.175));

        /*
            Sphere 3
        */
        Sphere sphere3 = new Sphere(new Point(-0.6, 0, 0), 0.3, new Diffuse(
                new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), 32
        ));
        boundingBox = ar.createBoundingBoxSphere(sphere3);
        // Front
        assertEquals(boundingBox.getVertex1(), new Point(-0.8999999999999999, 0.3, 0.3));
        assertEquals(boundingBox.getVertex2(), new Point(-0.3, 0.3, 0.3));
        assertEquals(boundingBox.getVertex3(), new Point(-0.8999999999999999, -0.3, 0.3));
        assertEquals(boundingBox.getVertex4(), new Point(-0.3, -0.3, 0.3));
        // Back
        assertEquals(boundingBox.getVertex5(), new Point(-0.8999999999999999, 0.3, -0.3));
        assertEquals(boundingBox.getVertex6(), new Point(-0.3, 0.3, -0.3));
        assertEquals(boundingBox.getVertex7(), new Point(-0.8999999999999999, -0.3, -0.3));
        assertEquals(boundingBox.getVertex8(), new Point(-0.3, -0.3, -0.3));

        /*
            Negative test cases
         */
        assertNotEquals(boundingBox.getVertex8(), new Point(-0.4, -0.3, -0.3));
    }
}