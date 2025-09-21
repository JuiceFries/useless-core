package org.useless.uui.uir;

import org.useless.uui.Drawing;

public interface Layer {

    /**
     * 绘图
     * @param drawing 绘图
     * @see Drawing
     */
    void draw(Drawing drawing);

    /**
     * 获取层
     * @return 层索引
     */
    int getZIndex();

    /**
     * 设置层
     * @param zIndex 层索引
     */
    void setZIndex(int zIndex); // 加个setter
}