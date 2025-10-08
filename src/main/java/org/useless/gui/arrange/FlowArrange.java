package org.useless.gui.arrange;

import org.useless.gui.template.Container;
import org.useless.gui.template.Template;
import org.useless.gui.data.Location;

import java.util.List;

/**
 * 流式布局 - 从左到右排列，超出宽度自动换行
 */
public class FlowArrange implements Arrange {
    private int hGap = 5; // 水平间距
    private int vGap = 5; // 垂直间距
    private int margin = 5; // 边距

    public FlowArrange() {}

    public FlowArrange(int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    @Override
    public void rearrange(Container container) {
        List<Template> children = container.getTemplateList();
        if (children.isEmpty()) return;

        int x = margin;
        int y = margin;
        int maxHeightInRow = 0;
        int containerWidth = container.getWidth();

        for (Template child : children) {
            int childWidth = child.getWidth();
            int childHeight = child.getHeight();

            // 换行检查
            if (x + childWidth > containerWidth - margin) {
                x = margin;
                y += maxHeightInRow + vGap;
                maxHeightInRow = 0;
            }

            // 设置位置
            child.setLocation(new Location(x, y));

            // 更新行高和位置
            x += childWidth + hGap;
            maxHeightInRow = Math.max(maxHeightInRow, childHeight);
        }
    }

    // 设置间距的方法
    public void setGap(int hGap, int vGap) {
        this.hGap = hGap;
        this.vGap = vGap;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
