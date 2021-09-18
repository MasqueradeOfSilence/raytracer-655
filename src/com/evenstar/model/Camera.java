package com.evenstar.model;

import com.evenstar.model.vectors.Direction;
import com.evenstar.model.vectors.Point;
import com.evenstar.model.vectors.VectorOperations;

public class Camera
{
    private final Direction lookAt;
    private final Direction lookFrom;
    private final Direction lookUp;
    private final int fieldOfView;

    public Camera(Direction lookAt, Direction lookFrom, Direction lookUp, int fieldOfView)
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
}
