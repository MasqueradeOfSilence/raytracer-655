import com.evenstar.model.vectors.Point;
import com.evenstar.util.AcceleratedRaytracer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AcceleratedRaytracerTest
{
    @Test
    void isPointCOnLineBetweenTwoPoints()
    {
        Point a = new Point(1, 3, 1);
        Point b = new Point(2, 1, 2);
        Point c = new Point(3, 2, 1);
        AcceleratedRaytracer ar = new AcceleratedRaytracer();
        boolean isPointOnLine = ar.isPointCOnLineBetweenTwoPoints(a, b, c);
        assertFalse(isPointOnLine);
    }
}