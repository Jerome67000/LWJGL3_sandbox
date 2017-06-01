package game;

import engine.EngineUtils;
import engine.Window;
import engine.graphics.Mesh;
import engine.graphics.ShaderProgram;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public class Renderer {

    ShaderProgram shaderProgram;

    public Renderer() {
    }

    public void init() throws Exception {
        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(EngineUtils.loadResource("/vertex.glsl"));
        shaderProgram.createFragmentShader(EngineUtils.loadResource("/fragment.glsl"));
        shaderProgram.link();
    }

    public void render(Window window, Mesh mesh) {
        clear();

        handleWindowRezise(window);

        shaderProgram.bind();

        // Draw the mesh
        glBindVertexArray(mesh.getVaoId());
        glEnableVertexAttribArray(0); // position
        glEnableVertexAttribArray(1); // colours
            glDrawElements(GL_TRIANGLES, mesh.getVertexCount(), GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(mesh.getVaoId());
        glBindVertexArray(0);

        shaderProgram.unbind();
    }

    private void handleWindowRezise(Window window) {
        if (window.isResized()) {
            System.out.println("LOG glViewport(): window resized to : " + window.getWidth() + "*" + window.getHeight());
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
    }

    public void cleanup() {
        if (shaderProgram != null) {
            shaderProgram.cleanup();
        }
    }
}
