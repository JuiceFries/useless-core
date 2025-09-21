package org.useless.uui.template;

import org.useless.uui.Color;
import org.useless.uui.Drawing;
import org.useless.uui.Event;
import org.useless.uui.data.Location;
import org.useless.uui.data.Size;
import org.useless.uui.event.Input;
import org.useless.uui.event.MouseEvent;
import org.useless.uui.uir.Layer;

import java.util.List;

/**
 * 基础的模板接口<br>
 * 提供了基础通用的API方法
 * @since 0.0.2
 * @see Location
 * @see Size
 * @see Color
 * @see Input
 * @see MouseEvent
 * @see Layer
 */
public sealed interface Template extends Layer permits Container, Control {

    // stm =====

    /**
     * 注册鼠标事件
     * @param mouse 事件
     * @see MouseEvent
     */
    default void enrolledMouse(MouseEvent mouse) {}

    /**
     * 注册键盘事件
     * @param input 事件
     * @see Input
     */
    default void enrolledInput(Input input) {}

    /**
     * 移除事件器
     * @param event 事件
     */
    default void logoutEvent(Event event) {}

    // set =====

    /**
     * 设置模板的坐标
     * @param location 起始坐标
     * @see Location
     */
    void setLocation(Location location);

    /**
     * 设置模板的坐标
     * @param x 横向坐标
     * @param y 纵向坐标
     */
    void setLocation(int x, int y);

    /**
     * 设置模板的大小
     * @param size 大小
     * @see Size
     */
    void setSize(Size size);

    /**
     * 设置模板的大小
     * @param width 宽度
     * @param height 高度
     */
    void setSize(int width, int height);

    default void setZIndex(int zIndex) {}

    /**
     * 设置绝对位置
     * @param x 横向坐标
     * @param y 纵向坐标
     * @param width 宽度
     * @param height 高度
     */
    void setBounds(int x, int y, int width, int height);

    /**
     * 设置绝对位置
     * @param location 坐标
     * @param size 大小
     */
    void setBounds(Location location, Size size);

    /**
     * 设置背景色
     * @param background 背景色
     */
    void setBackground(Color background);

    // get =====

    /**
     * 获取坐标
     * @return 横向坐标
     */
    int getX();

    /**
     * 获取坐标
     * @return 总想坐标
     */
    int getY();

    /**
     * 获取坐标
     * @return 坐标
     */
    Location getLocation();

    /**
     * @return 获取宽度
     */
    int getWidth();

    /**
     * @return 高度
     */
    int getHeight();

    /**
     * @return 获取大小
     */
    Size getSize();

    default int getZIndex() {
        return 300;
    }

    /**
     * @return 背景色
     */
    Color getBackground();

    /**
     * @return 获取鼠标事件列表
     */
    default List<MouseEvent> getMouseEventList() {
        return List.of();
    }

    /**
     * @return 获取输入事件列表
     */
    default List<Input> getInputList() {
        return List.of();
    }

    /**
     * 获取画笔
     * @return 画笔
     */
    Drawing getDrawing();

}
