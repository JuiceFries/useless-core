package org.useless.gui.uir;

import org.useless.gui.drawing.Drawing;

/**
 * 层
 */
public interface Layer {

    /**
     * 绘图<br>
     * 如果你不集成的话可以自己去处理偏移问题
     * @param drawing 绘图接口
     */
    void draw(Drawing drawing);

    /**
     * 设置层
     * @param zIndex 层
     */
    void setZIndex(int zIndex);

    /**
     * @return 返回层
     */
    int getZIndex();

}