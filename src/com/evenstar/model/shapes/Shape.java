package com.evenstar.model.shapes;

import com.evenstar.model.textures.Material;
import com.evenstar.model.vectors.HitPair;

public interface Shape
{
    Material getMaterial();
    HitPair getHitPair();
}
