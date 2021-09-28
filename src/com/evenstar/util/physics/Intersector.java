package com.evenstar.util.physics;

import com.evenstar.model.Ray;
import com.evenstar.model.shapes.Shape;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.shapes.Triangle;
import com.evenstar.model.vectors.*;
import com.evenstar.util.Constants;

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
}
