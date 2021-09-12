package com.evenstar.model.shapes;

import com.evenstar.model.Ray;

public interface Shape
{
    boolean hit(Ray ray, double tMin, double tMax, Hit hit);
}
