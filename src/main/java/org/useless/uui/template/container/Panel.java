package org.useless.uui.template.container;

import org.useless.uui.Arrange;
import org.useless.uui.Color;
import org.useless.uui.Drawing;
import org.useless.uui.Event;
import org.useless.uui.data.Location;
import org.useless.uui.data.Size;
import org.useless.uui.event.Input;
import org.useless.uui.event.MouseEvent;
import org.useless.uui.template.Container;
import org.useless.uui.template.Template;
import org.useless.uui.uir.FullName;
import org.useless.uui.uir.PanelDRI;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>容器</h2>
 * 一个基础的面板容器<br>
 * 提供了基础的添加组件的功能
 * @see Arrange
 * @see Drawing
 * @see Location
 * @see Size
 * @see Container
 * @author JuiceFries
 * @version 0.0.3
 */
public class Panel implements Container {

    /**
     * 坐标
     */
    private Location location = new Location();

    /**
     * 大小
     */
    private Size size = new Size(50,50);

    /**
     * 排布
     */
    private Arrange arrange;

    /**
     * 画笔
     */
    private Drawing drawing;

    /**
     * 层级
     */
    private int zIndex = 300;

    /**
     * 键盘事件
     */
    private final List<Input> input = new ArrayList<>();

    /**
     * 鼠标事件
     */
    private final List<MouseEvent> mouse = new ArrayList<>();

    /**
     * 背景色
     */
    private Color background = Color.GRAY;

    /**
     * 模板列表
     */
    private final List<Template> templates = new ArrayList<>();

    /**
     * UI渲染器
     */
    PanelDRI dri = PanelDRI.init();

    /**
     * 无参构造方法
     */
    public Panel() {
        this(null);
    }

    /**
     * 带有排布设置容器
     * @see Arrange
     */
    public Panel(Arrange arrange) {
        this.arrange = arrange;
    }

    // stm =====

    @Override
    public void draw(Drawing drawing) {
        this.drawing = drawing;
        drawing.pushState();
        drawing.translate(getX(), getY()); // 先平移坐标系到面板位置

        dri.drawBackground(this);

        // 绘制子组件（已经在相对坐标系内）
        for (Template child : getTemplates()) {
            child.draw(drawing);
        }

        drawing.popState();
    }

    @Override
    public void enrolledMouse(MouseEvent mouse) {
        this.mouse.add(mouse);
    }

    @Override
    public void enrolledInput(Input input) {
        this.input.add(input);
    }

    @Override
    public void enrolled(Template... templates) {
        if (templates == null) return;
        for (Template template : templates) {
            if (template != null) this.templates.add(template);
        }
        // 添加后自动重新布局
        if (arrange != null) arrange.rearrange(this);
    }

    @Override
    public void enrolled(Template template) {
        if (template == null) return;
        this.templates.add(template);
        if (arrange != null) arrange.rearrange(this);
    }

    @Override
    public void logout(Template template) {
        if (templates.remove(template) && arrange != null) {
            arrange.rearrange(this);
        }
    }

    @Override
    public void logout(Template... templates) {
        if (templates == null || templates.length == 0) return;

        boolean modified = false;
        for (Template template : templates) {
            if (template != null) modified |= this.templates.remove(template);
        }

        if (modified && arrange != null) arrange.rearrange(this);
    }

    @Override
    public void logoutEvent(Event event) {
        if (event instanceof MouseEvent) {
            mouse.remove(event);
        } else if(event instanceof Input) {
            input.remove(event);
        }
    }

    // set =====

    @Override
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    /**
     * 设置UI
     * @param dri UI渲染器
     */
    public void setDri(PanelDRI dri) {
        this.dri = dri;
    }

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
    public void setBounds(Location location, Size size) {
        setLocation(location);
        setSize(size);
    }

    @Override
    public void setBounds(int x, int y, int width, int height) {
        setBounds(new Location(x, y),new Size(width, height));
    }

    @Override
    public void setArrange(Arrange arrange) {
        this.arrange = arrange;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    // get =====

    @Override
    public int getWidth() {
        return size.width;
    }

    @Override
    public int getHeight() {
        return size.height;
    }

    @Override
    public int getX() {
        return location.x;
    }

    @Override
    public int getY() {
        return location.y;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public Size getSize() {
        return size;
    }

    /**
     * 获取布局
     * @return 布局
     */
    public Arrange getArrange() {
        return arrange;
    }

    @Override
    public Drawing getDrawing() {
        return drawing;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public List<MouseEvent> getMouseEventList() {
        return this.mouse;
    }

    @Override
    public List<Input> getInputList() {
        return this.input;
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public List<Template> getTemplates() {
        return this.templates;
    }

    /**
     * 获取UI渲染器
     * @return 渲染器
     */
    public PanelDRI getDri() {
        return dri;
    }

}
