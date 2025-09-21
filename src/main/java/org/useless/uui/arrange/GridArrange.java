package org.useless.uui.arrange;

import org.jetbrains.annotations.NotNull;
import org.useless.uui.Arrange;
import org.useless.uui.Window;
import org.useless.uui.template.Container;
import org.useless.uui.template.Template;

import java.util.List;

/**
 * <h2>网格排列</h2>
 * @see Arrange
 * @see Window
 * @see Container
 * @see Template
 * @since 0.0.3
 */
public class GridArrange implements Arrange {

    /**
     * 列
     */
    private final int cols;

    /**
     * 构造方法
     * @param cols 列
     */
    public GridArrange(int cols) { this.cols = cols; }

    @Override
    public void rearrange(@NotNull Container container) {
        doGridLayout(container.getWidth(), container.getTemplates(), cols);
    }

    @Override
    public void rearrange(@NotNull Window window) {
        doGridLayout(window.getSize().width, window.getAllTemplates(), cols);
    }

    /**
     * 用来布局的
     * @param totalWidth 总宽度
     * @param templates 总高的
     * @param columns 列
     * @see List
     * @see Template
     */
    private void doGridLayout(int totalWidth, @NotNull List<Template> templates, int columns) {
        int colWidth = totalWidth / columns;
        // 计算最大行高
        int maxRowHeight = templates.stream()
                .mapToInt(Template::getHeight)
                .max()
                .orElse(30); // 默认值保底

        for (int i = 0; i < templates.size(); i++) {
            Template child = templates.get(i);
            int row = i / columns;
            int col = i % columns;
            child.setLocation(col * colWidth, row * maxRowHeight); // 用统一行高
        }
    }

}