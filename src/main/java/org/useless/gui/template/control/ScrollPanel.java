package org.useless.gui.template.control;

import org.useless.gui.data.Color;
import org.useless.gui.drawing.Drawing;
import org.useless.gui.template.Container;
import org.useless.gui.template.Control;
import org.useless.gui.template.Template;
import org.useless.gui.uir.IllegalComponentException;

/**
 * 滚动面板<br>
 */
public class ScrollPanel extends BlankControl {

    private Template template;
    private boolean vertical = true;
    private boolean level = true;
    private int arcWidth = 8,arcHeight = 8;

    public ScrollPanel(Template template) {
        setBackground(Color.BLACK);
        switch (template) {
            case null -> throw new NullPointerException("面板为空!");
            case Container container -> {
                if (container.isRootContainer()) {
                    throw new IllegalComponentException("根容器禁止添加到任何模板之上！");
                } else {
                    this.template = container;
                }
            }
            case Control control -> {
                if (control instanceof ScrollPanel) {
                    throw new IllegalComponentException("滚动面板禁止被嵌套!\n请不要乱套娃!");
                } else {
                    this.template = control;
                }
            }
            default -> {
            }
        }
    }

    @Override
    public void draw(Drawing drawing) {
        super.draw(drawing); // 调用父方法注册事件
        drawing.setClip(getX(), getY(), getWidth(), getHeight()); // 裁剪区域

        drawing.setColor(getBackground()); // 设置背景色
        drawing.drawRoundedRectangle(getX()+1,getY()+1,getWidth()-2,getHeight()-2,arcWidth,arcHeight); // 绘制背景

        template.draw(drawing);
        template.setBounds(getX(),getY(),getWidth(),getHeight());

        drawing.resetClip(); // 重置裁剪区域
    }

    // set =====

    /**
     * 启用垂直滚动
     * @param vertical 垂直滚动
     */
    public void setVertical(boolean vertical) {
        this.vertical = vertical;
    }

    /**
     * 启用水平滚动
     * @param level 水平滚动
     */
    public void setLevel(boolean level) {
        this.level = level;
    }

    @Override
    public void setArc(int arcWidth ,int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    // get =====


    public boolean isLevel() {
        return level;
    }

    public boolean isVertical() {
        return vertical;
    }

    @Override
    public int getArcHeight() {
        return arcHeight;
    }

    @Override
    public int getArcWidth() {
        return arcWidth;
    }
}
