package org.useless.gui.uir;

import org.useless.gui.template.Template;

/**
 * <h2>样式</h2>
 * 用以设置UI的统一样式
 * @see UIManager
 * @see Template
 */
public interface Style {

    /**
     * 应用样式
     * @param target 组件
     */
    void apply(Template target);

    /**
     * 样式表示
     * @return 用于注册查找
     */
    String getKey();

    /**
     * 重置方法
     * @param target 组件
     */
    void reset(Template target);

}
