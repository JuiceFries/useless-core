package org.useless.uui.arrange;

import org.jetbrains.annotations.NotNull;
import org.useless.uui.Arrange;
import org.useless.uui.Window;
import org.useless.uui.template.Container;
import org.useless.uui.template.Template;

/**
 * 流动排布
 * @see Arrange
 * @see Window
 * @see Container
 * @see Template
 * @since 0.0.3
 */
public class FlowArrange implements Arrange {

    @Override
    public void rearrange(@NotNull Container container) {
        int x = 0, y = 0;
        for (Template child : container.getTemplates()) {
            if (x + child.getWidth() > container.getWidth()) {
                x = 0;
                y += child.getHeight(); // 换行？不存在的
            }
            child.setLocation(x, y);
            x += child.getWidth();
        }
    }

    @Override
    public void rearrange(@NotNull Window window) {
        int x = 0, y = 0;
        for (Template child : window.getAllTemplates()) {
            if (x + child.getWidth() > window.getSize().width) {
                x = 0;
                y += child.getHeight(); // 换行？不存在的
            }
            child.setLocation(x, y);
            x += child.getWidth();
        }
    }

}