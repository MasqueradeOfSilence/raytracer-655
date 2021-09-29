import com.evenstar.model.Camera;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.vectors.*;
import com.evenstar.util.Raytracer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RaytracerTest
{
    @Test
    void buildRay()
    {
        int i1 = 0;
        int j1 = 0;
        int dimension = 512;
        Camera camera = new Camera(new Point(0, 0, 0), new Point(0, 0, 1), new Point(0, 1, 0),
                28);
        Raytracer raytracer = new Raytracer(new Scene());
        Ray ray = raytracer.buildRay(i1, j1, dimension, camera);
        Direction direction = new Direction(-0.998046875, 0.998046875, -2.030726465346332);//-1.880726465346332);
        direction.getVector().normalize();
        assertEquals(ray, new Ray(new Point(0, 0, 1), direction));
    }

    @Test
    void computeColorOfPixel()
    {
        Camera camera = new Camera(new Point(0, 0, 0), new Point(0, 0, 1), new Point(0, 1, 0),
                28);
        int i1 = 97;
        int j1 = 283;
        int i2 = 33;
        int j2 = 196;
        int i3 = 112;
        int j3 = 288;
        int i4 = 320;
        int j4 = 262;
        int i5 = 435;
        int j5 = 264;
        int dimension = 512;
        Sphere greenSphere = new Sphere(new Point(-0.6, 0, 0), 0.3, new Diffuse(new Vector3D(0, 1, 0),
                new Vector3D(0.5, 1, 0.5), 32));
        Sphere redSphere = new Sphere(new Point(0.2, 0, -0.1), 0.075, new Diffuse(new Vector3D(1, 0, 0),
                new Vector3D(0.5, 1, 0.5), 32));
        Sphere whiteSphere = new Sphere(new Point(0.35, 0, -0.1), 0.05, new Diffuse(new Vector3D(1, 1, 1),
                new Vector3D(1, 1, 1), 4));
        Scene scene = new Scene();
        scene.addShape(greenSphere);
        scene.addShape(redSphere);
        scene.addShape(whiteSphere);
        Raytracer raytracer = new Raytracer(scene);
        Ray ray = raytracer.buildRay(i1, j1, dimension, camera);
        Color backgroundColor = new Color(.2, .2, .2);
        Pixel greenColor = raytracer.computeColorOfPixel(ray, backgroundColor);
        assertNotEquals(greenColor.getColor(), backgroundColor);

        Ray ray2 = raytracer.buildRay(i2, j2, dimension, camera);
        Pixel greenColor2 = raytracer.computeColorOfPixel(ray2, backgroundColor);
        assertNotEquals(greenColor2.getColor(), backgroundColor);
        Ray ray3 = raytracer.buildRay(i3, j3, dimension, camera);
        // Distortion case failed
        Pixel grayColor = raytracer.computeColorOfPixel(ray3, backgroundColor);
        assertEquals(grayColor.getColor(), backgroundColor);
        Ray ray4 = raytracer.buildRay(i4, j4, dimension, camera);
        Pixel redColor = raytracer.computeColorOfPixel(ray4, backgroundColor);
        assertNotEquals(redColor.getColor(), backgroundColor);
        Ray ray5 = raytracer.buildRay(i5, j5, dimension, camera);
        Pixel whiteColor = raytracer.computeColorOfPixel(ray5, backgroundColor);
        assertNotEquals(whiteColor.getColor(), backgroundColor);
    }
}