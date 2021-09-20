import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector3DTest {

    @Test
    void getUnitVector()
    {
        Vector3D vector = new Vector3D(5, 14, 10);
        Vector3D unitVector = vector.getUnitVector();
        // It's easier to have the same precision as Java does.
        assertEquals(unitVector, new Vector3D(
                0.2790727860929738,
                0.7814038010603266,
                0.5581455721859476));
    }

    @Test
    void length()
    {
        Vector3D vector = new Vector3D(2, 4, 4);
        double length = vector.length();
        assertEquals(length, 6);
    }

    @Test
    void normalize()
    {
        Vector3D vector = new Vector3D(3, 4, 5);
        vector.normalize();
        assertEquals(vector, new Vector3D(0.4242640687119285, 0.565685424949238,
                0.7071067811865475));
    }

    @Test
    void testEquals()
    {
        Vector3D vector1 = new Vector3D(1924, 6, 12);
        Vector3D vector2 = new Vector3D(1924, 6, 12);
        Vector3D rogue = new Vector3D(6, 1924, 12);
        assertEquals(vector1, vector2);
        assertNotEquals(vector1, rogue);
        assertNotEquals(vector2, rogue);
    }
}