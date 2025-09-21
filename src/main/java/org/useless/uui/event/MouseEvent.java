package org.useless.uui.event;

import org.intellij.lang.annotations.MagicConstant;
import org.useless.uui.Window;
import org.useless.uui.Event;

/**
 * 鼠标事件器<br>
 * 用于提供对应的鼠标事件
 * @see Event
 * @see Mouse
 * @see Window
 */
public abstract class MouseEvent implements Mouse {




    @Override
    public void clickEvent(@MagicConstant(intValues = {
            Mouse.LEFT_CLICK,Mouse.MIDDLE_CLICK,Mouse.RIGHT_CLICK
    }) int key )
    {

    }

    /**
     * 悬停状态
     */
    public void alreadyHovering() {

    }

    /**
     * 未悬停状态
     */
    public void notHovering() {

    }

}
