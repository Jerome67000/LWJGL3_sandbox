package engine.graphics;

import static org.lwjgl.opengl.GL20.*;

/**
 * Created by jgully on 29/05/2017.
 */
public class ShaderProgram {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderCode) {
        vertexShaderId = this.createShader(shaderCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) {
        fragmentShaderId = this.createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int glVertexShader) {
        return 0;
    }

}
