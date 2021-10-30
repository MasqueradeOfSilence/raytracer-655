import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.physics.Subspace;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.textures.Diffuse;
import com.evenstar.model.vectors.Midpoint;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.util.AcceleratedRaytracer;
import com.evenstar.util.physics.SubspaceComputer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SubspaceComputerTest
{
    private Scene computeDiffuse1Scene()
    {
        Scene diffuse1 = new Scene();
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
        diffuse1.addShape(sphere1);
        diffuse1.addShape(sphere2);
        diffuse1.addShape(sphere3);
        diffuse1.addShape(triangle1);
        diffuse1.addShape(triangle2);
        return diffuse1;
    }

    @Test
    void computeBoxAroundScene()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = computeDiffuse1Scene();
        BoundingBox boxAroundShape = subspaceComputer.computeBoxAroundScene(scene);
        double highestX = boxAroundShape.getVertex2().getX();
        double lowestX = boxAroundShape.getVertex7().getX();
        double highestY = boxAroundShape.getVertex2().getY();
        double lowestY = boxAroundShape.getVertex7().getY();
        double highestZ = boxAroundShape.getVertex2().getZ();
        double lowestZ = boxAroundShape.getVertex7().getZ();
        // Adding the delta parameter for small rounding differences
        assertEquals(highestX, 0.4, 0.001);
        assertEquals(lowestX, -0.9, 0.001);
        assertEquals(highestY, 0.3, 0.001);
        assertEquals(lowestY, -0.5, 0.001);
        assertEquals(highestZ, 0.3, 0.001);
        assertEquals(lowestZ, -0.4, 0.001);
    }

    @Test
    void distance2D()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Point p1 = new Point(-0.9, 0.3, 0.3);
        Point p2 = new Point(0.4, 0.3, 0.3);
        Point p3 = new Point(-0.9, -0.5, 0.3);
        double a = subspaceComputer.distance2D(p1, p2);
        double b = subspaceComputer.distance2D(p1, p3);
        assertEquals(a, 1.3, 0.001);
        assertEquals(b, 0.8, 0.001);
    }

    @Test
    void computeLargestMagnitudeExtentOfBoundingBox()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = computeDiffuse1Scene();
        BoundingBox boxAroundShape = subspaceComputer.computeBoxAroundScene(scene);
        double largestMagnitudeExtent = subspaceComputer.computeLargestMagnitudeExtentOfBoundingBox(boxAroundShape);
        assertEquals(largestMagnitudeExtent, 1.3, 0.001);
    }

    @Test
    void midpoint2D()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Point p1 = new Point(-0.9, 0.3, 0.3);
        Point p2 = new Point(0.4, 0.3, 0.3);
        Midpoint midpoint = subspaceComputer.midpoint2D(p1, p2);
        assertEquals(midpoint, new Midpoint(-0.25, 0.3));
    }

    @Test
    void didAOrBWin()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = computeDiffuse1Scene();
        BoundingBox boxAroundShape = subspaceComputer.computeBoxAroundScene(scene);
        char didAOrBWin = subspaceComputer.didAOrBWin(boxAroundShape);
        assertEquals(didAOrBWin, 'a');
        assertNotEquals(didAOrBWin, 'b');
    }

    @Test
    void isVerticalSplit()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = computeDiffuse1Scene();
        BoundingBox boxAroundShape = subspaceComputer.computeBoxAroundScene(scene);
        char didAOrBWin = subspaceComputer.didAOrBWin(boxAroundShape);
        boolean isVerticalSplit = subspaceComputer.isVerticalSplit(didAOrBWin);
        assertTrue(isVerticalSplit);
    }

    @Test
    void computeLeftSubspace()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = computeDiffuse1Scene();
        BoundingBox boxAroundScene = subspaceComputer.computeBoxAroundScene(scene);
        Subspace leftSubspace = subspaceComputer.computeLeftSubspace(new Subspace(boxAroundScene),
                new Midpoint(-0.25, 0.3));
        // really the x is -0.9
        assertEquals(leftSubspace.getUpperLeft(), new Point(-0.8999999999999999, 0.3, 0.3));
        assertEquals(leftSubspace.getBottomRight(), new Point(-0.25, -0.5, -0.4));
    }

    @Test
    void computeRightSubspace()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = computeDiffuse1Scene();
        BoundingBox boxAroundScene = subspaceComputer.computeBoxAroundScene(scene);
        Subspace rightSubspace = subspaceComputer.computeRightSubspace(new Subspace(boxAroundScene),
                new Midpoint(-0.25, 0.3));
        assertEquals(rightSubspace.getUpperLeft(), new Point(-0.25, 0.3, 0.3));
        assertEquals(rightSubspace.getBottomRight(), new Point(0.39999999999999997, -0.5, -0.4));
    }

    @Test
    void computeTopSubspace()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Subspace parent = new Subspace(new Point(-0.25, 0.3, 0.3), new Point(0.4, -0.5, -0.4));
        Subspace topSubspace = subspaceComputer.computeTopSubspace(parent, new Midpoint(-0.25, -0.1));
        assertEquals(topSubspace.getUpperLeft(), new Point(-0.25, 0.3, 0.3));
        assertEquals(topSubspace.getBottomRight(), new Point(0.4, -0.1, -0.4));
    }

    @Test
    void computeBottomSubspace()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Subspace parent = new Subspace(new Point(-0.25, 0.3, 0.3), new Point(0.4, -0.5, -0.4));
        Subspace bottomSubspace = subspaceComputer.computeBottomSubspace(parent, new Midpoint(-0.25, -0.1));
        assertEquals(bottomSubspace.getUpperLeft(), new Point(-0.25, -0.1, 0.3));
        assertEquals(bottomSubspace.getBottomRight(), new Point(0.4, -0.5, -0.4));
    }

    @Test
    void computeShapesInSubspace()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = computeDiffuse1Scene();
        AcceleratedRaytracer ar = new AcceleratedRaytracer();
        Subspace subspace2 = new Subspace(new Point(-0.9, 0.3, 0.3), new Point(-0.25, -0.5, -0.4));
        ArrayList<BoundingBox> shapesInSubspace = subspaceComputer.computeShapesInSubspace(subspace2,
                ar.computeBoundingBoxes(scene.getShapes()));
        assertEquals(shapesInSubspace.size(), 2);
    }

    @Test
    void subspaces4And5()
    {
        SubspaceComputer sc = new SubspaceComputer();
        Subspace subspace3 = new Subspace(new Point(-0.25, 0.3, 0.3), new Point(0.4, -0.5, -0.4));
        char aOrB = sc.didAOrBWin(subspace3);
        assertEquals(aOrB, 'b');
        Point p1 = sc.computeVertex1(subspace3);
        Point p3 = sc.computeVertex3(subspace3);
        Midpoint midpoint = sc.midpoint2D(p1, p3);
        Subspace subspace4 = sc.computeTopSubspace(subspace3, midpoint);
        assertEquals(subspace4, new Subspace(new Point(-0.25, 0.3, 0.3), new Point(0.4, -0.1, -0.4)));
    }

    /*
        Expected subspaces

        SUBSPACE 1:
        - Upper Left = -0.9, 0.3, 0.3
        - Bottom Right = 0.4, -0.5, -0.4
        - SHAPES: All

        SUBSPACE 2:
        - Upper Left = -0.9, 0.3, 0.3
        - Bottom Right: -0.25, -0.5, -0.4
        - Shapes: Sphere 3, Triangle 2

        SUBSPACE 3:
        - Upper Left: -0.25, 0.3, 0.3
        - Bottom Right: 0.4, -0.5, -0.4
        - Shapes: Sphere 1, Sphere 2, Triangle 1, Triangle 2

        SUBSPACE 4:
        - Upper Left: -0.25, 0.3, 0.3
        - Bottom Right: 0.4, -0.1, -0.4
        - Shapes: Sphere 1, Sphere 2, Triangle 1, Triangle 2

        SUBSPACE 5:
        - Upper Left: -0.25, -0.1, 0.3
        - Bottom Right: 0.4, -0.5, -0.4

        SUBSPACE 6:
        - Upper Left: -0.25, 0.3, 0.3
        - Bottom Right: 0.075, -0.1, -0.4

        SUBSPACE 7:
        - Upper Left: 0.075, 0.3, 0.3
        - Bottom Right: 0.4, -0.1, -0.4

        The rest are on my notesheet.

     */

    @Test
    void computeSubspacesForScene()
    {
        SubspaceComputer subspaceComputer = new SubspaceComputer();
        Scene scene = computeDiffuse1Scene();
        ArrayList<Subspace> subspaces = subspaceComputer.computeSubspacesForScene(scene);
        assertEquals(subspaces.size(), 21);
        Subspace subspace1 = subspaces.get(0);
        assertEquals(subspace1.getBoxes().size(), 5);
        Subspace subspace2 = subspaces.get(1);
        assertEquals(subspace2.getBoxes().size(), 2);
        Subspace subspace3 = subspaces.get(2);
        assertEquals(subspace3.getBoxes().size(), 4);
        Subspace subspace4 = subspaces.get(3);
        assertEquals(subspace4.getBoxes().size(), 4);
        Subspace subspace5 = subspaces.get(4);
        assertEquals(subspace5.getBoxes().size(), 2);
        Subspace subspace6 = subspaces.get(5);
        assertEquals(subspace6.getBoxes().size(), 2);
        Subspace subspace7 = subspaces.get(6);
        assertEquals(subspace7.getBoxes().size(), 3);
        Subspace subspace8 = subspaces.get(7);
        assertEquals(subspace8.getBoxes().size(), 1);
        Subspace subspace9 = subspaces.get(8);
        assertEquals(subspace9.getBoxes().size(), 3);
        Subspace subspace10 = subspaces.get(9);
        assertEquals(subspace10.getBoxes().size(), 2);
        Subspace subspace11 = subspaces.get(10);
        assertEquals(subspace11.getBoxes().size(), 3);
        Subspace subspace12 = subspaces.get(11);
        assertEquals(subspace12.getBoxes().size(), 3);
        Subspace subspace13 = subspaces.get(12);
        assertEquals(subspace13.getBoxes().size(), 3);
        Subspace subspace14 = subspaces.get(13);
        assertEquals(subspace14.getBoxes().size(), 3);
        Subspace subspace15 = subspaces.get(14);
        assertEquals(subspace15.getBoxes().size(), 1);
        Subspace subspace16 = subspaces.get(15);
        assertEquals(subspace16.getBoxes().size(), 3);
        Subspace subspace17 = subspaces.get(16);
        assertEquals(subspace17.getBoxes().size(), 3);
        Subspace subspace18 = subspaces.get(17);
        assertEquals(subspace18.getBoxes().size(), 2);
        Subspace subspace19 = subspaces.get(18);
        assertEquals(subspace19.getBoxes().size(), 2);
        Subspace subspace20 = subspaces.get(19);
        assertEquals(subspace20.getBoxes().size(), 2);
        Subspace subspace21 = subspaces.get(20);
        assertEquals(subspace21.getBoxes().size(), 2);
    }
}