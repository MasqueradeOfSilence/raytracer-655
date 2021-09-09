package com.evenstar.model;

public class Camera
{
    double aspect_ratio = 16.0 / 9.0;
    double viewport_height = 2.0;
    double viewport_width = aspect_ratio * viewport_height;
    double focal_length = 1.0;
    Point origin = new Point(0, 0, 0);
    
}
