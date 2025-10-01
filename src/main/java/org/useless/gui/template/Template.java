package org.useless.gui.template;

import java.util.List;
import org.useless.gui.data.Color;
import org.useless.gui.data.Location;
import org.useless.gui.data.Size;
import org.useless.gui.drawing.Drawing;
import org.useless.gui.event.Input;
import org.useless.gui.event.Mouse;
import org.useless.gui.uir.Layer;

/**
 * 基础的模板接口<br>
 * 提供了基础通用的API方法
 * @since 0.0.2
 */
public sealed interface Template extends Layer permits Control, Container {

    // set ====

    /**
     * 设置模板位置
     * @param location 位置
     */
    void setLocation(Location location);

    /**
     * 设置模板位置
     * @param x 横向位置
     * @param y 纵向位置
     */
    void setLocation(int x,int y);

    /**
     * 设置模板大小
     * @param size 窗口大小
     */
    void setSize(Size size);

    /**
     * 设置模板大小
     * @param width 宽
     * @param height 高
     */
    void setSize(int width,int height);

    void setBounds(Location location,Size size);

    void setBounds(int x,int y,int width,int height);

    /**
     * 设置窗口背景色
     * @param background 背景色
     */
    void setBackground(Color background);

    @Override
    default void setZIndex(int zIndex) {

    }

    // get =====

    /**
     * @return 模板位置
     */
    Location getLocation();

    /**
     * @return 横向坐标
     */
    int getX();

    /**
     * @return 纵向坐标
     */
    int getY();

    /**
     * @return 模板大小
     */
    Size getSize();

    /**
     * @return 模板宽度
     */
    int getWidth();

    /**
     * @return 模板高度
     */
    int getHeight();

    /**
     * @return 获取背景色
     */
    Color getBackground();

    @Override
    default int getZIndex() {
        return 500;
    }

    Drawing getDraw();

    // stm =====

    /**
     * 绘图<br>
     * 如果你不集成的话可以自己去处理偏移问题
     * @param drawing 绘图接口
     */
    @Override
    void draw(Drawing drawing);

    /**
     * 添加键盘事件
     * @param input 事件
     */
    void addInputEvent(Input input);

    /**
     * 添加鼠标事件
     * @param mouse 事件
     */
    void addMouseEvent(Mouse mouse);

    /**
     * 移除事件
     * @param input 事件
     */
    void removeInputEvent(Input input);

    /**
     * 移出事件
     * @param mouse 事件
     */
    void removeMouseEvent(Mouse mouse);

    /**
     * @return 返回事件列表
     */
    List<Input> getInputList();

    /**
     * @return 返回事件列表
     */
    List<Mouse> getMouseList();

}
