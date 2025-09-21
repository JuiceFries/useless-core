package org.useless.uui.event;

import org.intellij.lang.annotations.MagicConstant;
import org.useless.uui.Event;

/**
 * 鼠标接口<br>
 * 用于提供鼠标的部分接口
 * @see Event
 */
public interface Mouse {

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
     * @param key 事件
     */
    void clickEvent(@MagicConstant(intValues = {
            Mouse.LEFT_CLICK,Mouse.MIDDLE_CLICK,Mouse.RIGHT_CLICK
    })int key );

}
