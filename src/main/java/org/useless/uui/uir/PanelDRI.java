package org.useless.uui.uir;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.useless.uui.template.container.Panel;

/**
 * <h2>面板绘制器</h2>
 * 用来提供默认绘制方法
 * @see Panel
 * @since 0.0.3
 */
public interface PanelDRI {

    /**
     * 初始化
     * @return 默认UI
     */
    @Contract(value = " -> new", pure = true)
    static @NotNull PanelDRI init() {
        return new PanelUI();
    }

    /**
     * 背景绘制
     * @param panel 面板
     * @deprecated 没啥用
     */
    @Deprecated(since = "0.0.3")
    void drawBackground(Panel panel);

}
