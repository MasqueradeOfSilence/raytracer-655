package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.physics.Hit;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.vectors.*;
import com.evenstar.util.ClassIdentifier;
import com.evenstar.util.Constants;

import java.util.ArrayList;

public class Intersector
{
    public double intersects(Ray ray, Shape shape)
    {
        if (shape.getClass().toString().contains("Triangle"))
        {
            return this.triangleIntersection(ray, (Triangle) shape);
        }
        else
        {
            return this.sphereIntersection(ray, (Sphere) shape);
        }
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
        return VectorOperations.dotProduct(normal, VectorOperations.crossProduct(edge1, C1)) > 0 &&
                VectorOperations.dotProduct(normal, VectorOperations.crossProduct(edge2, C2)) > 0 &&
                VectorOperations.dotProduct(normal, VectorOperations.crossProduct(edge3, C3)) > 0;
    }

    private double triangleIntersection(Ray ray, Triangle triangle)
    {
        Vector3D ab = VectorOperations.subtractVectors(triangle.getVertex2().getVector(),
                triangle.getVertex1().getVector());
        Vector3D ac = VectorOperations.subtractVectors(triangle.getVertex3().getVector(),
                triangle.getVertex1().getVector());
        Vector3D normal = VectorOperations.crossProduct(ab, ac);
        normal.normalize();
        if (notParallel(normal, ray.getDirection()))
        {
            double D = -VectorOperations.dotProduct(normal, triangle.getVertex1().getVector());
            double t = -(VectorOperations.dotProduct(normal, ray.getOrigin().getVector()) + D) /
                    VectorOperations.dotProduct(normal, ray.getDirection().getVector());
            if (t < 0)
            {
                Vector3D hitPoint = VectorOperations.addVectors(ray.getOrigin().getVector(),
                        VectorOperations.multiplyByScalar(ray.getDirection().getVector(), t));
                if (isHitPointInsideOfTriangle(hitPoint, triangle, normal))
                {
                    return VectorOperations.distance(ray.getOrigin().getVector(), hitPoint);
                }
            }
        }
        return Constants.NO_INTERSECTION;
    }

    private double sphereIntersection(Ray ray, Sphere sphere)
    {
        Vector3D originToCenter = VectorOperations.subtractVectors(ray.getOrigin().getVector(),
                sphere.getCenter().getVector());
        double a = VectorOperations.dotProduct(ray.getDirection().getVector(),
                ray.getDirection().getVector());
        double b = 2.0 * VectorOperations.dotProduct(originToCenter, ray.getDirection().getVector());
        double c = VectorOperations.dotProduct(originToCenter, originToCenter) -
                (sphere.getRadius() * sphere.getRadius());
        double discriminant = Math.pow(b, 2) - (4 * a * c);
        if (discriminant < 0.0)
        {
            return Constants.NO_INTERSECTION;
        }
        else
        {
            double t0 = (-b - Math.sqrt(discriminant)) / (2.0 * a);
            double t1 = (-b + Math.sqrt(discriminant)) / (2.0 * a);
            if (t0 < 0)
            {
                return t0;
            }
            else if (t1 < 0)
            {
                return t1;
            }
            return Constants.NO_INTERSECTION;
        }
    }

    public Triangle setHitPointTriangle(Ray ray, Triangle triangle)
    {
        Vector3D ab = VectorOperations.subtractVectors(triangle.getVertex2().getVector(),
                triangle.getVertex1().getVector());
        Vector3D ac = VectorOperations.subtractVectors(triangle.getVertex3().getVector(),
                triangle.getVertex1().getVector());
        Vector3D normal = VectorOperations.crossProduct(ab, ac);
        normal.normalize();
        if (notParallel(normal, ray.getDirection()))
        {
            double D = -VectorOperations.dotProduct(normal, triangle.getVertex1().getVector());
            double t = -(VectorOperations.dotProduct(normal, ray.getOrigin().getVector()) + D) /
                    VectorOperations.dotProduct(normal, ray.getDirection().getVector());
            if (t < 0)
            {
                Vector3D hitPoint = VectorOperations.addVectors(ray.getOrigin().getVector(),
                        VectorOperations.multiplyByScalar(ray.getDirection().getVector(), t));
                if (isHitPointInsideOfTriangle(hitPoint, triangle, normal))
                {
                    triangle.setHitPair(new HitPair(new Point(hitPoint), normal));
                }
            }
        }
        return triangle;
    }

    public Sphere setHitPointSphere(double distanceToHitPoint, Ray ray, Sphere sphere)
    {
        Vector3D hitPoint = VectorOperations.addVectors(ray.getOrigin().getVector(),
                VectorOperations.multiplyByScalar(ray.getDirection().getVector(), distanceToHitPoint));
        double normalX = (hitPoint.getX() - sphere.getCenter().getX()) / sphere.getRadius();
        double normalY = (hitPoint.getY() - sphere.getCenter().getY()) / sphere.getRadius();
        double normalZ = (hitPoint.getZ() - sphere.getCenter().getZ()) / sphere.getRadius();
        Vector3D normal = new Vector3D(normalX, normalY, normalZ);
        normal.normalize();
        HitPair hitPair = new HitPair(new Point(hitPoint), normal);
        sphere.setHitPair(hitPair);
        return sphere;
    }

    // ignore everything above this comment for now

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
    public ArrayList<Hit> computeRayShapeHits(Ray ray, ArrayList<Shape> shapes)
    {
        ArrayList<Hit> hits = new ArrayList<>();
        for (int i = 0; i < shapes.size(); i++)
        {
            Shape current = shapes.get(i);
            // Sphere case
            if (ClassIdentifier.isSphere(current))
            {
                Sphere sphere = (Sphere) current;
                Hit sphereHit = computeSphereHit(ray, sphere);
                if (sphereHit != null)
                {
                    hits.add(sphereHit);
                }
            }
        }
        return hits;
    }
}
