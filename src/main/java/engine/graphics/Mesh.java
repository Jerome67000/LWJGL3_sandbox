package engine.graphics;


import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

public class Mesh {

    private int vaoId;
    private List<Integer> vboIds = new ArrayList<>();
    private int vertexCount;

    public Mesh(float[] positions, float[] colours, int indices[]) {

        FloatBuffer posBuffer = null;
        FloatBuffer colBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            vertexCount = indices.length;

            // vertex array creation
            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // positions VBO
            int vboId = glGenBuffers();
            vboIds.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // colour VBO
            vboId = glGenBuffers();
            vboIds.add(vboId);
            colBuffer = MemoryUtil.memAllocFloat(colours.length);
            colBuffer.put(colours).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, colBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 3, GL_FLOAT, false, 0, 0);

            // indicies VBO
            vboId = glGenBuffers();
            vboIds.add(vboId);
            indicesBuffer = MemoryUtil.memAllocInt(indices.length);
            indicesBuffer.put(indices).flip();
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer  != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (colBuffer != null) {
                MemoryUtil.memFree(colBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    public void render() {
        // Draw the mesh
        glBindVertexArray(vaoId);
        glEnableVertexAttribArray(0); // position
        glEnableVertexAttribArray(1); // colours
        glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);
    }

    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIds) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
