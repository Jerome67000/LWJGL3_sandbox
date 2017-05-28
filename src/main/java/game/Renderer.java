package game;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Jerome on 27/05/2017.
 */
public class Renderer {

    public void init() {

    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
