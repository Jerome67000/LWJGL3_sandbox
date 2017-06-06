package engine.graphics;


import engine.PNGDecoder;

import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;


public class Texture {

    private int id;

    public Texture(String fileName) throws Exception {
        id = load(fileName);
    }

    private int load(String fileName) throws Exception {
        PNGDecoder decoder = new PNGDecoder(Texture.class.getResourceAsStream(fileName));
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
        decoder.decode(byteBuffer,decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
        byteBuffer.flip();

        int texId = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texId);
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuffer);
        glGenerateMipmap(GL_TEXTURE_2D);

        return texId;
    }


    public int getId() {
        return id;
    }

    public void cleanup() {
        glDeleteTextures(id);
    }
}
