package com.evenstar.model.textures;

import com.evenstar.model.vectors.Color;
import com.evenstar.model.vectors.Vector3D;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class ImageTexture implements Material
{
    private ByteBuffer pixels;
    private final double nx;
    private final double ny;
    private final Diffuse diffuse;
    private final String imageSource;

    public ImageTexture(ByteBuffer pixels, double nx, double ny, Diffuse diffuse, String imageSource)
    {
        this.pixels = pixels;
        this.nx = nx;
        this.ny = ny;
        this.diffuse = diffuse;
        this.imageSource = imageSource;
    }

    public Color getColorAtUV(double u, double v)
    {
        ByteBuffer copy = this.deepCopy(this.pixels);
//        System.out.println("Pixels remaining: " + pixels.remaining());
        byte[] arr = new byte[pixels.remaining()];
        //System.out.println("Arr length: " + arr.length);

        pixels.get(arr);
        //final CharBuffer charBuffer = StandardCharsets.UTF_8.decode(ByteBuffer.wrap(arr));
        //char[] arr2 = Arrays.copyOf(charBuffer.array(), charBuffer.limit());
//        System.out.println("ARRAY: " + Arrays.toString(arr));

        // for blue expecting 0.10588 0.65882 0.94118
        double i = u * nx;
        double j = (1 - v) * ny - 0.001;
        // Edge cases
        i = Math.max(i, 0);
        j = Math.max(j, 0);
        i = Math.min(i, nx - 1);
        j = Math.min(j, ny - 1);
//        System.out.println("i, j: " + i + ", " + j);
        i = Math.ceil(i);
        j = Math.ceil(j);
//        System.out.println("First index: " + (int)(3 * i + 3 * nx * j));
//        System.out.println("Second index: " + (int)(3 * i + 3 * nx * j + 1));
//        System.out.println("Third index: " + (int)(3 * i + 3 * nx * j + 2));

        // Now we have the right numbers, but they're not indexed properly
        double rNumerator = (arr[(int)(3 * i + 3 * nx * j)]);
        if (rNumerator < 0)
        {
            rNumerator = 256 - Math.abs(rNumerator);
        }
        double gNumerator = (arr[(int)(3 * i + 3 * nx * j + 1)]);
        if (gNumerator < 0)
        {
            gNumerator = 256 - Math.abs(gNumerator);
        }
        double bNumerator = (arr[(int)(3 * i + 3 * nx * j + 2)]);
        if (bNumerator < 0)
        {
            bNumerator = 256 - Math.abs(bNumerator);
        }
        // Convert to 0-1 scale
        double r = rNumerator / 255.0;
        double g = gNumerator / 255.0;
        double b = bNumerator / 255.0;
//        System.out.println("RGB: (" + r + ", " + g + ", " + b + ")");
        this.pixels = copy;
        return new Color(r, g, b);
    }

    public ByteBuffer getPixels()
    {
        return pixels;
    }

    public double getNx()
    {
        return nx;
    }

    public double getNy()
    {
        return ny;
    }

    public Diffuse getDiffuse()
    {
        return diffuse;
    }

    public String getImageSource()
    {
        return imageSource;
    }

    @Override
    public Vector3D getVector()
    {
        return new Vector3D(0, 0, 0);
    }

    // Deep copy of byte buffer from http://www.java2s.com/example/java-utility-method/bytebuffer-copy/deepcopy-bytebuffer-orig-7e676.html
    private ByteBuffer deepCopy(ByteBuffer orig)
    {
        int pos = orig.position(), lim = orig.limit();
        try
        {
            orig.position(0).limit(orig.capacity()); // set range to entire buffer
            ByteBuffer toReturn = deepCopyVisible(orig); // deep copy range
            toReturn.position(pos).limit(lim); // set range to original
            return toReturn;
        }
        finally
        { // do in finally in case something goes wrong we don't bork the orig
            orig.position(pos).limit(lim); // restore original
        }
    }

    private ByteBuffer deepCopyVisible(ByteBuffer orig)
    {
        int pos = orig.position();
        try
        {
            ByteBuffer toReturn;
            // try to maintain implementation to keep performance
            if (orig.isDirect())
            {
                toReturn = ByteBuffer.allocateDirect(orig.remaining());
            }
            else
            {
                toReturn = ByteBuffer.allocate(orig.remaining());
            }

            toReturn.put(orig);
            toReturn.order(orig.order());

            return (ByteBuffer) toReturn.position(0);
        }
        finally
        {
            orig.position(pos);
        }
    }
}
