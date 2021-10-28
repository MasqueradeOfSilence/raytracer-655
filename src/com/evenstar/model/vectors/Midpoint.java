package com.evenstar.model.vectors;

import java.util.Objects;

public class Midpoint
{
    private final double x;
    private final double y;

    public Midpoint(double x, double y)
    {
        this.x = x;
        this.y = y;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Midpoint midpoint = (Midpoint) o;
        return Double.compare(midpoint.x, x) == 0 &&
                Double.compare(midpoint.y, y) == 0;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(x, y);
    }
}
