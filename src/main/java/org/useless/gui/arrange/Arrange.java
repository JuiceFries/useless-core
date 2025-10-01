package org.useless.gui.arrange;

import org.useless.gui.template.Container;

/**
 * 布置接口<br>
 * 用于布置模板的坐标
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

}
