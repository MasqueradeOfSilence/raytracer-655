package com.evenstar.model.vectors;

public final class VectorOperations
{
    public static Vector3D addVectors(Vector3D firstVector, Vector3D secondVector)
    {
        double x = firstVector.getX() + secondVector.getX();
        double y = firstVector.getY() + secondVector.getY();
        double z = firstVector.getZ() + secondVector.getZ();
        return new Vector3D(x, y, z);
    }

    public static Vector3D subtractVectors(Vector3D firstVector, Vector3D secondVector)
    {
        double x = firstVector.getX() - secondVector.getX();
        double y = firstVector.getY() - secondVector.getY();
        double z = firstVector.getZ() - secondVector.getZ();
        return new Vector3D(x, y, z);
    }

    public static double dotProduct(Vector3D firstVector, Vector3D secondVector)
    {
        return firstVector.getX() * secondVector.getX()
                + firstVector.getY() * secondVector.getY()
                + firstVector.getZ() * secondVector.getZ();
    }

    public static Vector3D crossProduct(Vector3D firstVector, Vector3D secondVector)
    {
        double x = firstVector.getY() * secondVector.getZ() - firstVector.getZ() * secondVector.getY();
        double y = firstVector.getZ() * secondVector.getX() - firstVector.getX() * secondVector.getZ();
        double z = firstVector.getX() * secondVector.getY() - firstVector.getY() * secondVector.getX();
        return new Vector3D(x, y, z);
    }

    public static Vector3D multiplyByScalar(Vector3D vector, double scalar)
    {
        double x = vector.getX() * scalar;
        double y = vector.getY() * scalar;
        double z = vector.getZ() * scalar;
        return new Vector3D(x, y, z);
    }

    public static Vector3D divideByScalar(Vector3D vector, double scalar)
    {
        double x = vector.getX() / scalar;
        double y = vector.getY() / scalar;
        double z = vector.getZ() / scalar;
        return new Vector3D(x, y, z);
    }

    public static Vector3D negativeVector(Vector3D vector)
    {
        double x = -vector.getX();
        double y = -vector.getY();
        double z = -vector.getZ();
        return new Vector3D(x, y, z);
    }

    public static Vector3D normalize(Vector3D vector)
    {
        double x = vector.getX() / vector.length();
        double y = vector.getY() / vector.length();
        double z = vector.getZ() / vector.length();
        return new Vector3D(x, y, z);
    }

    public static double distance(Vector3D vector1, Vector3D vector2)
    {
        return Math.sqrt(Math.pow(vector1.getX() - vector2.getX(), 2) +
                Math.pow(vector1.getY() - vector2.getY(), 2) +
                Math.pow(vector1.getZ() - vector2.getZ(), 2));
    }

    public static Vector3D multiplyVectors(Vector3D vector1, Vector3D vector2)
    {
        double x = vector1.getX() * vector2.getX();
        double y = vector1.getY() * vector2.getY();
        double z = vector1.getZ() * vector2.getZ();
        return new Vector3D(x, y, z);
    }

    public static Vector3D addScalar(Vector3D vector, double scalar)
    {
        double x = vector.getX() + scalar;
        double y = vector.getY() + scalar;
        double z = vector.getZ() + scalar;
        return new Vector3D(x, y, z);
    }

    public static Vector3D absoluteValue(Vector3D vector)
    {
        double x = Math.abs(vector.getX());
        double y = Math.abs(vector.getY());
        double z = Math.abs(vector.getZ());
        return new Vector3D(x, y, z);
    }

    public static Vector3D divideVectors(Vector3D vector1, Vector3D vector2)
    {
        double x = vector1.getX() / vector2.getX();
        double y = vector1.getY() / vector2.getY();
        double z = vector1.getZ() / vector2.getZ();
        return new Vector3D(x, y, z);
    }
}
