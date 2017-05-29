package game;

import engine.EngineUtils;
import engine.graphics.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Jerome on 27/05/2017.
 */
public class Renderer {

    ShaderProgram shaderProgram;

    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(EngineUtils.loadRessource("/resources/vertex.glsl"));
        shaderProgram.createFragmentShader(EngineUtils.loadRessource("/resources/fragment.glsl"));
        shaderProgram.link();
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }
}
