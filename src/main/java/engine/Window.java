package engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Wrapper for the GLFW window.
 */
public class Window {

    private long windowHandle;
    private final String title;
    private int width;
    private int height;
    private boolean resized;

    public Window(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.resized = false;
    }

    public void init() {
        // Setup an error callback. The default implementation will print the error message in System.err.
        GLFWErrorCallback.createPrint(System.err).set();

        initGLFW();
        configureGLFW();

        setupResizeCallback();
        setupKeyCallback();

        // Get the thread stack and push a new frame
        try (MemoryStack stack = stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            // Get the windowHandle size passed to glfwCreateWindow
            glfwGetWindowSize(windowHandle, pWidth, pHeight);

            // Get the resolution of the primary monitor
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

            // Center the windowHandle
            glfwSetWindowPos(windowHandle, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
        } // the stack frame is popped automatically

        // Make the OpenGL context current
        glfwMakeContextCurrent(windowHandle);

        // Enable v-sync
        glfwSwapInterval(1);

        glfwShowWindow(windowHandle);

        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
    }

    private void initGLFW() {
        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");
    }

    private void configureGLFW() {
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_RESIZABLE, resized ? GLFW_TRUE : GLFW_FALSE); // the windowHandle will be resizable
        createWindow();
    }

    private void setupResizeCallback() {
        glfwSetFramebufferSizeCallback(windowHandle, (window, width, height) -> {
            System.out.println("LOG glfwSetFramebufferSizeCallback(): window resized to : " + width + "*" + height);
            this.width = width;
            this.height = height;
            this.resized = true;
        });
    }

    /**
     * Setup a key callback. It will be called every time a key is pressed, repeated or released.
     */
    private void setupKeyCallback() {
        glfwSetKeyCallback(windowHandle, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE)
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });
    }

    private void createWindow() {
        windowHandle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (windowHandle == NULL)
            throw new RuntimeException("Failed to create the GLFW windowHandle");
    }

    public void update() {
        glfwSwapBuffers(windowHandle);
        glfwPollEvents();
    }

    public void setClearColor(float r, float g, float b, float alpha) {
        glClearColor(r, g, b, alpha);
    }

    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_PRESS;
    }

    public boolean isKeyReleased(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_RELEASE;
    }

    public boolean isKeyRepeated(int keyCode) {
        return glfwGetKey(windowHandle, keyCode) == GLFW_REPEAT;
    }

    public boolean windowShouldClose() {
        return glfwWindowShouldClose(windowHandle);
    }

    public void setResized(boolean resized) {
        this.resized = resized;
    }

    public boolean isResized() {
        return resized;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}