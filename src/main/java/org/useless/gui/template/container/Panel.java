package org.useless.gui.template.container;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.useless.gui.data.Color;
import org.useless.gui.template.Container;
import org.useless.gui.template.Template;
import org.useless.gui.data.Location;
import org.useless.gui.data.Size;
import org.useless.gui.drawing.Drawing;
import org.useless.gui.event.Input;
import org.useless.gui.event.Mouse;
import org.useless.gui.uir.IllegalComponentException;

import static java.lang.System.err;

/**
 * 面板<br>
 * 一个基础的面板,提供基础的功能
 * @see Template
 * @see org.useless.gui.template.Container
 * @see Drawing
 * @since 0.0.1
 * @version 0.4
 */
public class Panel implements Container {

    private Location location = new Location(50,50);
    private Size size = new Size(50,50);
    private Color background = new Color(50,50,50,1f);
    private boolean visible = true;
    private int zIndex = 500;
    private final List<Template> templateList = new ArrayList<>();
    private Drawing drawing;
    private final List<Input> inputList = new ArrayList<>();
    private final List<Mouse> mouseList = new ArrayList<>();
    private boolean init = true;
    public Panel() {}

    // stm =====

    @Override
    public void draw(@NotNull Drawing drawing) {
        if (drawing.getHandle().glHandle() != 0) this.drawing = drawing;
        if (init && drawing.getHandle().glHandle() != 0) {
            drawing.registerEvent(this);
            init = false;
        }
        if (!isVisible()) return; // 面板不可见直接滚蛋

        drawing.pushState();
        drawing.translate(getX(), getY()); // 相对坐标转换
        // 1. 画面板背景（圆角矩形）
        drawing.setColor(background);
        drawing.drawRoundedRectangle(0, 0, getWidth(), getHeight(), 8,8); // 给点圆角好看

        // 2. 画所有子控件（简单粗暴按添加顺序）
        for (Template child : templateList) {
            // 子控件如果是容器，检查它的可见性
            if (child instanceof Container containerChild) {
                if (!containerChild.isVisible()) continue; // 容器不可见就跳过
            }
            // 普通控件直接画（没可见性就默认显示）
            child.draw(drawing);
        }
        drawing.popState();
    }

    @Override
    public void draw(Consumer<Drawing> draw) {
        if (drawing == null) {
            err.println("警告：Drawing实例未初始化，请先检查draw(Drawing)方法!");
            return;
        }
        if (!isVisible()) return;
        drawing.pushState();
        drawing.translate(getX(),getY());
        draw.accept(drawing);
        drawing.popState();
    }

    @Override
    public void add(Template template) {
        if (template == null) throw new NullPointerException("所添加的容器不能为空!");
        else {
            if (template instanceof Container) {
                if (((Container) template).isRootContainer()) {
                    throw new IllegalComponentException("根容器不能被添加到任何容器上!");
                } else if (template == this) {
                    throw new IllegalComponentException("窗口不能添加自己!就像水杯不能把自己放进去一样！");
                } else {
                    templateList.add(template);
                }
            } else {
                templateList.add(template);
            }
        }
    }

    @Override
    public void add(Template @NotNull ... template) {
        for (Template templates : template) {
            this.add(templates);
        }
    }

    @Override
    public void remove(Template template) {
        if (template == null) throw new NullPointerException("要移除的组件不存在!你是想要移除空气?");
        else if (template == this) throw new IllegalStateException("组件无法移除自身!就好比把箱子从箱子里拿出!");
        else templateList.remove(template);
    }

    @Override
    public void remove(Template... templates) {
        for (Template template : templates) {
            this.remove(template);
        }
    }

    @Override
    public void addInputEvent(Input input) {
        inputList.add(input);
    }

    @Override
    public void addMouseEvent(Mouse mouse) {
        mouseList.add(mouse);
    }

    @Override
    public void removeInputEvent(Input input) {
        inputList.remove(input);
    }

    @Override
    public void removeMouseEvent(Mouse mouse) {
        mouseList.remove(mouse);
    }


    // set =====

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void setLocation(int x, int y) {
        setLocation(new Location(x, y));
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
    }

    @Override
    public void setSize(int width, int height) {
        setSize(new Size(width, height));
    }

    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    @Override
    public void setBounds(Location location, Size size) {
        setLocation(location);
        setSize(size);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        setBounds(new Location(x, y),new Size(width, height));
    }

    @Override
    public void setBackground(Color background) {
        this.background = background;
    }

    // get =====

    @Override
    public Template getTemplateList(int index) {
        return templateList.get(index);
    }

    @Override
    public List<Template> getTemplateList() {
        return templateList;
    }

    @Override
    public Drawing getDraw() {
        return drawing;
    }

    @Override
    public List<Input> getInputList() {
        return inputList;
    }

    @Override
    public List<Mouse> getMouseList() {
        return mouseList;
    }

    @Override
    public int getY() {
        return location.getY();
    }

    @Override
    public int getX() {
        return location.getX();
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getWidth() {
        return size.width;
    }

    @Override
    public int getHeight() {
        return size.height;
    }

    public Size getSize() {
        return size;
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

}
