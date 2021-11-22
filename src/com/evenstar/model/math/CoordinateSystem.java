package com.evenstar.model.math;

import com.evenstar.model.vectors.SphereNormal;

public class CoordinateSystem
{
    private final SphereNormal N;
    private final SphereNormal NT;
    private final SphereNormal Nb;

    public CoordinateSystem(SphereNormal n, SphereNormal NT, SphereNormal nb)
    {
        N = n;
        this.NT = NT;
        Nb = nb;
    }

    public SphereNormal getN()
    {
        return N;
    }

    public SphereNormal getNT()
    {
        return NT;
    }

    public SphereNormal getNb()
    {
        return Nb;
    }
}
