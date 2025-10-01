package org.useless.gui.template.control;

import java.util.ArrayList;
import java.util.List;
import org.useless.gui.data.Color;
import org.useless.gui.template.Control;
import org.useless.gui.data.Location;
import org.useless.gui.data.Size;
import org.useless.gui.drawing.Drawing;
import org.useless.gui.event.Input;
import org.useless.gui.event.Mouse;

/**
 * 这是个实现接口把手写抽后的实现，<br>
 * 它没有任何渲染实现，但是已经帮你把基础字段和事件绑定给写好了。<br>
 * 自己用去吧!
 * @see Control
 * @see Drawing
 * @since 0.0.3
 */
public class BlankControl implements Control {
    private Location location = new Location(0,0);
    private Size size = new Size(50,50);
    private Color background = new Color(60,60,60,1f);
    private final List<Input> inputList = new ArrayList<>();
    private final List<Mouse> mouseList = new ArrayList<>();
    private int zIndex = 500;
    private Drawing drawing;
    private boolean init = false;
    private int arcWidth = 9,arcHeight = 9;

    // stm =====
    public void draw(Drawing drawing) {
        this.drawing = drawing;
        if (!init && drawing != null) {
            drawing.registerEvent(this);
            init = true;
        }
    }

    @Override
    public void addMouseEvent(Mouse mouse) {
        mouseList.add(mouse);
    }

    @Override
    public void addInputEvent(Input input) {
        inputList.add(input);
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
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
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
    public void setBackground(Color background) {
        this.background = background;
    }

    public void setArc(int arcWidth,int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    // get =====


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
    public int getWidth() {
        return size.width;
    }

    @Override
    public int getHeight() {
        return size.height;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public List<Mouse> getMouseList() {
        return mouseList;
    }

    @Override
    public List<Input> getInputList() {
        return inputList;
    }

    @Override
    public Drawing getDraw() {
        return drawing;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public int getArcWidth() {
        return arcWidth;
    }

    @Override
    public int getArcHeight() {
        return arcHeight;
    }

}
