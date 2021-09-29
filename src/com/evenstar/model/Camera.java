package com.evenstar.model;

import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;

public class Camera
{
    private final Point lookAt;
    private Point lookFrom;
    private final Point lookUp;
    private final int fieldOfView;

    public Camera(Point lookAt, Point lookFrom, Point lookUp, int fieldOfView)
    {
        this.lookAt = lookAt;
        this.lookFrom = lookFrom;
        this.lookUp = lookUp;
        this.fieldOfView = fieldOfView;
    }

    @Override
    public String toString()
    {
        return "Camera{" +
                "lookAt=" + lookAt +
                ", lookFrom=" + lookFrom +
                ", lookUp=" + lookUp +
                ", fieldOfView=" + fieldOfView +
                '}';
    }

    public Point getLookAt()
    {
        return lookAt;
    }

    public Point getLookFrom()
    {
        return lookFrom;
    }

    public Point getLookUp()
    {
        return lookUp;
    }

    public int getFieldOfView()
    {
        return fieldOfView;
    }

    public void setLookFrom(Point lookFrom)
    {
        this.lookFrom = lookFrom;
    }
}
