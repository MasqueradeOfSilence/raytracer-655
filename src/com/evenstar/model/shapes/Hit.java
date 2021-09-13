package com.evenstar.model.shapes;

import com.evenstar.model.Ray;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.Vector3D;
import com.evenstar.model.vectors.VectorOperations;

public class Hit
{
    private Point whereHit;
    private Vector3D normal;
    private double t;
    private boolean isFrontFacing;

    public Hit(Point whereHit, Vector3D normal, double t)
    {
        this.whereHit = whereHit;
        this.normal = normal;
        this.t = t;
    }

    public Hit()
    {

    }

    public Point getWhereHit()
    {
        return whereHit;
    }

    public Vector3D getNormal()
    {
        return normal;
    }

    public double getT()
    {
        return t;
    }

    public boolean isFrontFacing()
    {
        return isFrontFacing;
    }

    public void setT(double t)
    {
        this.t = t;
    }

    public void setWhereHit(Point whereHit)
    {
        this.whereHit = whereHit;
    }

    public void setFaceNormal(Ray ray, Vector3D outwardNormal)
    {
        // It's front facing if the dot product is negative
        this.isFrontFacing = VectorOperations.dotProduct(ray.getDirection().getVector(), outwardNormal) < 0;
        this.normal = this.isFrontFacing ? outwardNormal : VectorOperations.negativeVector(outwardNormal);
    }
}
