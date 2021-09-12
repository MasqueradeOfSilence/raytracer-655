import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VectorOperationsTest {

    @Test
    void addVectors()
    {
        Vector3D vector1 = new Vector3D(5, 5, 5);
        Vector3D vector2 = new Vector3D(2, 3, 4);
        Vector3D sum = VectorOperations.addVectors(vector1, vector2);
        assertEquals(sum, new Vector3D(7, 8, 9));
        assertNotEquals(sum, new Vector3D(1, 2, 3));
    }

    @Test
    void subtractVectors()
    {
        Vector3D vector1 = new Vector3D(5, 5, 5);
        Vector3D vector2 = new Vector3D(2, 3, 4);
        Vector3D minuend = VectorOperations.subtractVectors(vector1, vector2);
        assertEquals(minuend, new Vector3D(3, 2, 1));
        assertNotEquals(minuend, new Vector3D(1, 2, 3));
    }

    @Test
    void dotProduct()
    {
        Vector3D vector1 = new Vector3D(2, 7, 1);
        Vector3D vector2 = new Vector3D(8, 2, 8);
        double dotProduct = VectorOperations.dotProduct(vector1, vector2);
        assertEquals(dotProduct, 38);
    }

    @Test
    void crossProduct()
    {

    }

    @Test
    void multiplyByScalar()
    {
        Vector3D vector = new Vector3D(4, 5, 6);
        Vector3D product = VectorOperations.multiplyByScalar(vector, 10);
        assertEquals(product, new Vector3D(40, 50, 60));
    }

    @Test
    void divideByScalar()
    {
        Vector3D vector = new Vector3D(40, 50, 60);
        Vector3D quotient = VectorOperations.divideByScalar(vector, 10);
        assertEquals(quotient, new Vector3D(4, 5, 6));
    }
}