package org.useless.uui.event;

import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

/**
 * 键盘事件<br>
 * 用于处理键盘事件
 * @see Input
 * @see GLFW
 */
public class KeyboardEvent implements Input {

    // 提供的几个常用的常量
    public final static int KEY_ESC = GLFW_KEY_ESCAPE;
    public final static int KEY_SPACE = GLFW_KEY_SPACE;
    public final static int KEY_ENTER = GLFW_KEY_ENTER;
    public final static int KEY_LEFT_SHIFT = GLFW_KEY_LEFT_SHIFT;
    public final static int KET_RIGHT_SHIFT = GLFW_KEY_RIGHT_SHIFT;

    public final static int KEY_W = GLFW_KEY_W;
    public final static int KEY_A = GLFW_KEY_A;
    public final static int KEY_S = GLFW_KEY_S;
    public final static int KEY_D = GLFW_KEY_D;


    @Override
    public void pressKey(int key) {

    }

    @Override
    public void loosenKey(int key) {

    }

    @Override
    public void longPress(int key) {

    }

    @Override
    public void inputKey(char input) {

    }

}
