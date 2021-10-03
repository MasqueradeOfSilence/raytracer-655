package com.evenstar.model.lights;

import com.evenstar.model.vectors.Color;

public interface Light
{
    Color getLightColor();
    void turnOff();
    void turnOn();
    boolean isOn();
}
