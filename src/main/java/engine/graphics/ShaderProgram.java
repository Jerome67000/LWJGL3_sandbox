package engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;


public class ShaderProgram {

    private final int programId;
    private int vertexShaderId;
    private int fragmentShaderId;

    private Map<String, Integer> uniforms = new HashMap<>();

    public ShaderProgram() throws Exception {
        programId = glCreateProgram();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    public void createVertexShader(String shaderSourceCode) throws Exception {
        vertexShaderId = this.createShader(shaderSourceCode, GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderSourceCode) throws Exception {
        fragmentShaderId = this.createShader(shaderSourceCode, GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderSourceCode, int shaderType) throws Exception {

        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderSourceCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return 0;
    }

    public void createUniform(String uniformName) throws Exception {

        int uniformLocation = glGetUniformLocation(programId, uniformName);

        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }

        uniforms.put(uniformName, uniformLocation);
    }

    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".colour");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");
    }

    // Dump the matrix into a float buffer
    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(name), false, fb);
        }
    }

    public void setUniform(String name, int value) {
        glUniform1i(uniforms.get(name), value);
    }

    public void setUniform(String name, float value) {
        glUniform1f(uniforms.get(name), value);
    }

    public void setUniform(String name, Vector3f value) {
        glUniform3f(uniforms.get(name), value.x, value.y, value.z);
    }

    public void setUniform(String name, Vector4f value) {
        glUniform4f(uniforms.get(name), value.x, value.y, value.z, value.w);
    }

    public void setUniform(String name, PointLight pointLight) {
        setUniform(name + ".colour", pointLight.getColor());
        setUniform(name + ".position", pointLight.getPosition());
        setUniform(name + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(name + ".att.constant", att.getConstant());
        setUniform(name + ".att.linear", att.getLinear());
        setUniform(name + ".att.exponent", att.getExponent());
    }

    public void setUniform(String name, Material mat) {
        setUniform(name + ".ambient", mat.getAmbientColour());
        setUniform(name + ".diffuse", mat.getDiffuseColour());
        setUniform(name + ".specular", mat.getSpecularColour());
        setUniform(name + ".hasTexture", mat.isTextured() ? 1 : 0);
        setUniform(name + ".reflectance", mat.getReflectance());
    }

    public void link() throws Exception {

        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }
    }

    public void bind() {
        glUseProgram(programId);
    }

    public void unbind() {
        glUseProgram(0);
    }

    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
