package org.useless.gui.event;

import org.useless.gui.template.container.Window;

/**
 * 窗口事件监听器基本约定
 * @see Event
 */
public interface Root extends Event {

    /**
     * 窗口拖动事件<br>
     * 在窗口发生拖动时执行<br>
     * @see Window
     * @see Event
     */
    void DragEvent();

    /**
     * 窗口尺寸事件<br>
     * 窗口发生大小变化包括缩放窗口最大化最小化时执行
     * @see Window
     * @see Event
     */
    void SizeEvent();

    /**
     * 用于窗口关闭方法<br>
     * 窗口关闭时执行
     * @see Window
     * @see Event
     */
    void CloseEvent();
}
