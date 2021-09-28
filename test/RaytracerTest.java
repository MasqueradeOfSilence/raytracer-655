import com.evenstar.model.Camera;
import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
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
        assertEquals(ray, new Ray(new Point(0, 0, 1), new Direction(-0.998046875, 0.998046875,
                1.880726465346332)));
    }
}