package com.evenstar.model.textures;

import com.evenstar.model.vectors.Color;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class BackgroundImage
{
    // Can only be 512x512 currently
    private final String fileName;
    private ByteBuffer bb;

    public BackgroundImage(String fileName)
    {
        this.fileName = fileName;
        this.createByteBuffer();
    }

    public String getFileName()
    {
        return fileName;
    }

    public Color getColorAtIndex(int i, int j)
    {
        i /= 2;
        j /= 2;
        ByteBuffer copy = this.deepCopy(this.bb);
        byte[] arr = new byte[bb.remaining()];
        bb.get(arr);
        double rNumerator = (arr[(3 * i + 3 * 512 * j)]);
        if (rNumerator < 0)
        {
            rNumerator = 256 - Math.abs(rNumerator);
        }
        double gNumerator = (arr[(3 * i + 3 * 512 * j + 1)]);
        if (gNumerator < 0)
        {
            gNumerator = 256 - Math.abs(gNumerator);
        }
        double bNumerator = (arr[(3 * i + 3 * 512 * j + 2)]);
        if (bNumerator < 0)
        {
            bNumerator = 256 - Math.abs(bNumerator);
        }
        // Convert to 0-1 scale
        double r = rNumerator / 255.0;
        double g = gNumerator / 255.0;
        double b = bNumerator / 255.0;
        this.bb = copy;
        return new Color(r, g, b);
    }

    private void createByteBuffer()
    {
        String fileName = "texture/" + this.fileName;
        IntBuffer xBuffer = BufferUtils.createIntBuffer(1);
        xBuffer.put(512);
        xBuffer.flip();
        IntBuffer yBuffer = BufferUtils.createIntBuffer(1);
        yBuffer.put(512);
        yBuffer.flip();
        IntBuffer channelBuffer = BufferUtils.createIntBuffer(1);
        channelBuffer.put(3);
        channelBuffer.flip();
        this.bb = STBImage.stbi_load(fileName, xBuffer, yBuffer, channelBuffer, 0);
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
