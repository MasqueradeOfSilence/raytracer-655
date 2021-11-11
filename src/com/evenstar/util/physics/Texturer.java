package com.evenstar.util.physics;

import com.evenstar.model.UV;
import com.evenstar.model.physics.Hit;
import com.evenstar.model.shapes.Sphere;
import com.evenstar.model.textures.ImageTexture;
import com.evenstar.model.vectors.Color;

public class Texturer
{
    public Color getTextureColor(Sphere sphere, Hit hit)
    {
        return this.getTextureDiffuse(hit, (ImageTexture) sphere.getMaterial(), sphere);
    }

    private Color getTextureDiffuse(Hit hit, ImageTexture imageTexture, Sphere sphere)
    {
        UV uv = new UV(hit, sphere);
        return imageTexture.getColorAtUV(uv.getU(), uv.getV());
    }
}
