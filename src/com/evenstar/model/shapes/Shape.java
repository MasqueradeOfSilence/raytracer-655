package com.evenstar.model.shapes;

import com.evenstar.model.Ray;

public interface Shape
{
    Hit hitByRay(Ray ray, double tMin, double tMax, Hit hit);
}
