package engine;


import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;


public class MouseInput {

    private final Vector2d currentPos = new Vector2d(0,0);
    private final Vector2d previousPos = new Vector2d(0,0);
    private final Vector2f displVec = new Vector2f(0,0);
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    public MouseInput() {
    }

    public void init(Window window) {
        GLFW.glfwSetCursorPosCallback(window.getWindowHandle(), (window1, xpos, ypos) -> currentPos.set(xpos, ypos));
        GLFW.glfwSetCursorEnterCallback(window.getWindowHandle(), (window1, entered) -> inWindow = entered);
        GLFW.glfwSetMouseButtonCallback(window.getWindowHandle(), (window1, button, action, mods) -> {
            leftButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rightButtonPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }

    public void input(Window window) {
        displVec.set(0,0);
        if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
            updateDisplVec();
        }
        previousPos.set(currentPos);

        // LOG
        if (inWindow && false) {
            System.out.println("Mouse activated: " + inWindow + " " + currentPos.x + " " + currentPos.y);
            if (leftButtonPressed) {
                System.out.println("Left button pressed");
            }
            if (rightButtonPressed) {
                System.out.println("Right button pressed");
            }
        }
    }

    private void updateDisplVec() {
        double deltax = currentPos.x - previousPos.x;
        double deltay = currentPos.y - previousPos.y;
        boolean rotateX = deltax != 0;
        boolean rotateY = deltay != 0;
        if (rotateX) {
            displVec.y = (float) deltax;
        }
        if (rotateY) {
            displVec.x = (float) deltay;
        }
    }

    public Vector2f getDisplVec() {
        return displVec;
    }

    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }
}
