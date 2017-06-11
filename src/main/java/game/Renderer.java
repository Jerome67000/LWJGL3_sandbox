package game;

import engine.EngineUtils;
import engine.GameItem;
import engine.Window;
import engine.graphics.Camera;
import engine.graphics.Mesh;
import engine.graphics.ShaderProgram;
import engine.graphics.Transformation;
import org.joml.Matrix4f;

import static org.lwjgl.opengl.GL11.*;


public class Renderer {

    // Field of View in Radians
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;
    private final Transformation transformation;
    private ShaderProgram shaderProgram;

    public Renderer() {
        transformation = new Transformation();
    }

    public void init(Window window) throws Exception {
        window.setClearColor(0,0,0,0.0f);

        shaderProgram = new ShaderProgram();
        shaderProgram.createVertexShader(EngineUtils.loadResource("/shaders/vertex.glsl"));
        shaderProgram.createFragmentShader(EngineUtils.loadResource("/shaders/fragment.glsl"));
        shaderProgram.link();

        shaderProgram.createUniform("projectionMatrix");
        shaderProgram.createUniform("modelViewMatrix");
        shaderProgram.createUniform("texture_sampler");
        shaderProgram.createUniform("colour");
        shaderProgram.createUniform("useColour");
    }

    public void render(Window window, Camera camera, GameItem[] gameItems) {
        clear();

        handleWindowRezise(window);

        shaderProgram.bind();
        shaderProgram.setUniform("texture_sampler", 0);

        this.updateProjectionMatrix(window);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);

        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix  = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
            shaderProgram.setUniform("colour", mesh.getColour());
            shaderProgram.setUniform("useColour", mesh.isTextured() ?  0 : 1);

            mesh.render();
        }

        shaderProgram.unbind();
    }

    private void handleWindowRezise(Window window) {
        if (window.isResized()) {
            System.out.println("LOG glViewport(): window resized to : " + window.getWidth() + "*" + window.getHeight());
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
    }

    private void updateProjectionMatrix(Window window) {
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);

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
