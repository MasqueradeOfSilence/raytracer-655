package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.Scene;
import com.evenstar.model.physics.BoundingBox;
import com.evenstar.model.physics.Hit;
import com.evenstar.model.physics.Subspace;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.vectors.*;
import com.evenstar.util.AcceleratedRaytracer;
import com.evenstar.util.ClassIdentifier;
import com.evenstar.util.Globals;

import java.util.ArrayList;
import java.util.Collections;

public class Intersector
{
    private ArrayList<Subspace> subspaces;
    public Intersector(Scene scene)
    {
        AcceleratedRaytracer ar = new AcceleratedRaytracer();
        this.subspaces = ar.computeSubspacesForScene(scene);
    }
    private boolean notParallel(Vector3D normal, Direction direction)
    {
        return Math.abs(VectorOperations.dotProduct(normal, direction.getVector())) > 0.000001;
    }

    private boolean isHitPointInsideOfTriangle(Vector3D hitPoint, Triangle triangle, Vector3D normal)
    {
        Vector3D edge1 = VectorOperations.subtractVectors(triangle.getVertex2().getVector(),
                triangle.getVertex1().getVector());
        Vector3D edge2 = VectorOperations.subtractVectors(triangle.getVertex3().getVector(),
                triangle.getVertex2().getVector());
        Vector3D edge3 = VectorOperations.subtractVectors(triangle.getVertex1().getVector(),
                triangle.getVertex3().getVector());
        Vector3D C1 = VectorOperations.subtractVectors(hitPoint, triangle.getVertex1().getVector());
        Vector3D C2 = VectorOperations.subtractVectors(hitPoint, triangle.getVertex2().getVector());
        Vector3D C3 = VectorOperations.subtractVectors(hitPoint, triangle.getVertex3().getVector());
        return VectorOperations.dotProduct(normal, VectorOperations.crossProduct(edge1, C1)) > -0.01 &&
                VectorOperations.dotProduct(normal, VectorOperations.crossProduct(edge2, C2)) > -0.01 &&
                VectorOperations.dotProduct(normal, VectorOperations.crossProduct(edge3, C3)) > -0.01;
    }

    private Hit computeTriangleHit(Ray ray, Triangle triangle)
    {
        Vector3D ab = VectorOperations.subtractVectors(triangle.getVertex2().getVector(),
                triangle.getVertex1().getVector());
        Vector3D ac = VectorOperations.subtractVectors(triangle.getVertex3().getVector(),
                triangle.getVertex1().getVector());
        TriangleNormal normal = new TriangleNormal(ab, ac);
        if (notParallel(normal.getVector(), ray.getDirection()))
        {
            double D = -VectorOperations.dotProduct(normal.getVector(), triangle.getVertex1().getVector());
            double t = -(VectorOperations.dotProduct(normal.getVector(), ray.getOrigin().getVector()) + D) /
                    VectorOperations.dotProduct(normal.getVector(), ray.getDirection().getVector());
            if (t > 0.001)
            {
                Vector3D hitPoint = VectorOperations.addVectors(ray.getOrigin().getVector(),
                        VectorOperations.multiplyByScalar(ray.getDirection().getVector(), t));
                if (isHitPointInsideOfTriangle(hitPoint, triangle, normal.getVector()))
                {
                    return new Hit(new Point(hitPoint), this.computeDistance(ray.getOrigin(), new Point(hitPoint)),
                            triangle, ray);
                }
            }
        }
        return null;
    }

    // maybe use the one above this

    private Vector3D computeSphereCenterToRayOrigin(Point sphereCenter, Point rayOrigin)
    {
        return VectorOperations.subtractVectors(sphereCenter.getVector(), rayOrigin.getVector());
    }

    private boolean sphereIsInFrontOfRay(Direction rayDirection, Vector3D sphereCenterToRayOrigin)
    {
        // We assume that if the camera is inside of a sphere, we simply clip that sphere.
        return VectorOperations.dotProduct(rayDirection.getVector(), sphereCenterToRayOrigin) > 0;
    }

    private Vector3D computePUV(Vector3D v, Vector3D u)
    {
        double numerator = VectorOperations.dotProduct(v, u);
        double denominator = v.length();
        double scalar = numerator / denominator;
        return VectorOperations.multiplyByScalar(v, scalar);
    }

    private Point computeQPrime(Point p, Vector3D puv)
    {
        return new Point(VectorOperations.addVectors(p.getVector(), puv));
    }

    private double computeDistance(Point q, Point qPrime)
    {
        Vector3D minuend = VectorOperations.subtractVectors(q.getVector(), qPrime.getVector());
        return minuend.length();
    }

    private double computeX(double r, Point qPrime, Point q)
    {
        double length = computeDistance(qPrime, q);
        return Math.sqrt(Math.pow(r, 2) - Math.pow(length, 2));
    }

    private double distanceFromRayOriginToSphereIntersection(Point qPrime, Point p, double x)
    {
        return this.computeDistance(qPrime, p) - x;
    }

    private Point computeIntersectionPoint(Point origin, Direction direction, double distance)
    {
        Vector3D firstTerm = origin.getVector();
        Vector3D secondTerm = VectorOperations.multiplyByScalar(direction.getVector(), distance);
        Vector3D i1 = VectorOperations.addVectors(firstTerm, secondTerm);
        return new Point(i1);
    }

    private Hit computeSphereHit(Ray ray, Sphere sphere)
    {
        Vector3D sphereCenterToRayOrigin = this.computeSphereCenterToRayOrigin(sphere.getCenter(), ray.getOrigin());
        if (this.sphereIsInFrontOfRay(ray.getDirection(), sphereCenterToRayOrigin))
        {
            Vector3D puv = computePUV(ray.getDirection().getVector(), sphereCenterToRayOrigin);
            Point qPrime = computeQPrime(ray.getOrigin(), puv);
            // distance from (center) to (the point at which the center projects onto the ray)
            double projectionDistance = this.computeDistance(sphere.getCenter(), qPrime);
            if (projectionDistance == sphere.getRadius())
            {
                return new Hit(qPrime, this.computeDistance(qPrime, ray.getOrigin()), sphere, ray);
            }
            else if (projectionDistance < sphere.getRadius())
            {
                Vector3D vpc = VectorOperations.subtractVectors(sphere.getCenter().getVector(), ray.getOrigin().getVector());
                if (vpc.length() > sphere.getRadius())
                {
                    // Ray is outside of the sphere, so we can keep going
                    double x = computeX(sphere.getRadius(), qPrime, sphere.getCenter());
                    double distanceFromRayOriginToSphereIntersection =
                            this.distanceFromRayOriginToSphereIntersection(qPrime, ray.getOrigin(), x);
                    Point i1 = this.computeIntersectionPoint(ray.getOrigin(), ray.getDirection(),
                            distanceFromRayOriginToSphereIntersection);
                    return new Hit(i1, distanceFromRayOriginToSphereIntersection, sphere, ray);
                }
            }
        }
        return null;
    }

    // REVISED INTERSECTION ALGORITHM
    public ArrayList<Hit> computeRayShapeHits(Ray ray, ArrayList<Shape> shapes, Scene scene)
    {
        ArrayList<Hit> hits = new ArrayList<>();
        AcceleratedRaytracer ar = new AcceleratedRaytracer();
        ArrayList<Subspace> intersectedSubspaces = new ArrayList<>();
        ArrayList<Double> rayToSubspaceDistances = new ArrayList<>();
        for (int i = 0; i < this.subspaces.size(); i++)
        {
            Subspace current = this.subspaces.get(i);
            // if ray intersects subspace, add to list
            if (ar.doesRayIntersectSubspace(ray, current))
            {
                intersectedSubspaces.add(current);
                double rayToSubspaceDistance = Math.min(VectorOperations.distance(ray.getOrigin().getVector(),
                        ar.getTMin().getVector()), VectorOperations.distance(ray.getOrigin().getVector(),
                        ar.getTMax().getVector()));
                rayToSubspaceDistances.add(rayToSubspaceDistance);
            }
        }
        if (intersectedSubspaces.size() == 0)
        {
            // Nothing hit -- eliminate ray, should just be the background color
            return hits;
        }

        assert(intersectedSubspaces.size() == rayToSubspaceDistances.size());

        // Next algorithm piece: Find smallest rayToSubspaceDistances number.
        // See if it intersects any shapes inside of it. If it does, add those to rayShapeHits.
        // If it doesn't, remove the item from both arrays, then find the smallest one again.
        // If this doesn't work, just iterate through them all. we still have the min.
        boolean finished = false;
        while (!finished)
        {
            if (intersectedSubspaces.size() == 0 || rayToSubspaceDistances.size() == 0)
            {
                finished = true;
            }
            else
            {
                int minIndex = rayToSubspaceDistances.indexOf(Collections.min(rayToSubspaceDistances));
                Subspace closestSubspace = intersectedSubspaces.get(minIndex);
                ArrayList<BoundingBox> bBoxesOfShapesInSubspace = closestSubspace.getBoxes();
                boolean hitSomething = false;
                for (int i = 0; i < bBoxesOfShapesInSubspace.size(); i++)
                {
                    BoundingBox current = bBoxesOfShapesInSubspace.get(i);
                    Globals.numIntersectionTests++;
                    if (ClassIdentifier.isSphere(current.getCorrespondingShape()))
                    {
                        Sphere sphere = (Sphere) current.getCorrespondingShape();
                        Hit sphereHit = computeSphereHit(ray, sphere);
                        if (sphereHit != null)
                        {
                            hitSomething = true;
                            hits.add(sphereHit);
                        }
                    }
                    else if (ClassIdentifier.isTriangle(current.getCorrespondingShape()))
                    {
                        Triangle triangle = (Triangle) current.getCorrespondingShape();
                        Hit triangleHit = computeTriangleHit(ray, triangle);
                        if (triangleHit != null)
                        {
                            hitSomething = true;
                            hits.add(triangleHit);
                        }
                    }
                }
                if (hitSomething)
                {
                    finished = true;
                }
                else
                {
                    if (rayToSubspaceDistances.size() > 0 && intersectedSubspaces.size() > 0)
                    {
                        rayToSubspaceDistances.remove(minIndex);
                        intersectedSubspaces.remove(minIndex);
                    }
                }
            }
        }

//        for (int i = 0; i < shapes.size(); i++)
//        {
//            Shape current = shapes.get(i);
//            Globals.numPixelsExamined++;
//            if (!ar.rayHitsBoundingBox(ray, current))
//            {
//                // this causes clipping and shouldn't be necessary once median split is finished
////                continue;//
//            }
//            Globals.numIntersectionTests++;
//            // Sphere case
//            if (ClassIdentifier.isSphere(current))
//            {
//                Sphere sphere = (Sphere) current;
//                Hit sphereHit = computeSphereHit(ray, sphere);
//                if (sphereHit != null)
//                {
//                    hits.add(sphereHit);
//                }
//            }
//            else if (ClassIdentifier.isTriangle(current))
//            {
//                Triangle triangle = (Triangle) current;
//                Hit triangleHit = computeTriangleHit(ray, triangle);
//                if (triangleHit != null)
//                {
//                    hits.add(triangleHit);
//                }
//            }
//        }
        return hits;
    }
}
