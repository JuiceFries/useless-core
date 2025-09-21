package org.useless.uui;

import org.useless.uui.template.Container;

/**
 * 布置接口<br>
 * 用于布置模板的坐标
 * @see Window
 * @see Container
 * @since 0.0.3
 */
public interface Arrange {

    /**
     * 排布接口
     * @param container 组件
     * @see Container
     */
    void rearrange(Container container);

    /**
     * 排布接口
     * @param window 窗口
     * @see Window
     */
    void rearrange(Window window);
}
