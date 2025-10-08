package org.useless.gui.event;

import org.intellij.lang.annotations.MagicConstant;
import org.useless.gui.data.Location;

/**
 * 鼠标接口<br>
 * 用于提供鼠标的部分接口
 * @see Event
 */
public interface Mouse extends Event{

    /**
     * 左键
     */
    int LEFT_CLICK= 1;

    /**
     * 右键
     */
    int RIGHT_CLICK= 3;

    /**
     * 中建
     */
    int MIDDLE_CLICK= 2;

    /**
     * 点击事件
     * @param key 按键
     */
    void clickEvent(@MagicConstant(intValues = {
            Mouse.LEFT_CLICK,Mouse.MIDDLE_CLICK,Mouse.RIGHT_CLICK
    })int key );

    /**
     * 点击事件
     * @param key 按键
     * @param x 横向坐标
     * @param y 纵向坐标
     */
    void clickEvent(@MagicConstant(intValues = {
            Mouse.LEFT_CLICK,Mouse.MIDDLE_CLICK,Mouse.RIGHT_CLICK
    }) int key,int x,int y);

    /**
     * 获取鼠标坐标
     * @param location 鼠标坐标
     */
    void mouseCoordinates(Location location);

    void scrollEvent(int data);

    void isRelease(boolean release);
}
