package org.useless.gui.template.control;

import java.util.ArrayList;
import java.util.List;
import org.useless.gui.data.Color;
import org.useless.gui.template.Control;
import org.useless.gui.data.BasicTools;
import org.useless.gui.data.Location;
import org.useless.gui.data.Size;
import org.useless.gui.drawing.Drawing;
import org.useless.gui.event.Mouse;
import org.useless.gui.event.MouseEvent;
import org.useless.gui.font.Font;

/**
 * 没啥卵用懒得写了
 */
@Deprecated(since = "0.0.3")
public class Button implements Control {

    private Location location = new Location(10,10);
    private Size size = new Size(60,40);
    private Drawing drawing;
    private String text;
    private Color background = new Color(55,55,55,1.0f);
    private final List<Mouse> mouseList = new ArrayList<>();
    private boolean init = false;
    private boolean selected = false;
    private int arcW = 8,arcH = 8;
    private int zIndex = 500;
    private Location mousePosition;
    private Font font = new Font(Font.DEFAULT,20);

    public Button() {
        this("");
    }

    public Button(String text) {
        this.text = text;
        addMouseEvent(new MouseEvent() {
            @Override
            public void clickEvent(int key, int x, int y) {
                selected = key == Mouse.LEFT_CLICK && BasicTools.isMouseOver(mousePosition, Button.this);
            }

            @Override
            public void mouseCoordinates(Location location) {
                mousePosition = location;
            }
        });
    }

    // stm =====

    @Override
    public void draw(Drawing drawing) {
        this.drawing = drawing;
        if (!init && drawing.getHandle().glHandle() != 0) {
            drawing.registerEvent(this);
            init = true;
        }

        if (BasicTools.isMouseOver(mousePosition,this)) {
            drawing.setColor(background);
            drawing.drawRoundedRectangle(getX(), getY(), getWidth(), getHeight(), getArcWidth(), getArcHeight());
            drawing.setColor(new Color(200,200,200, 0.1f));
            drawing.drawRoundedRectangle(getX(), getY(), getWidth(), getHeight(), getArcWidth(), getArcHeight());
        } else {
            drawing.setColor(background);
            drawing.drawRoundedRectangle(getX(), getY(), getWidth(), getHeight(), getArcWidth(), getArcHeight());
        }
        if (selected) {
            drawing.setWireFrame(true);
            drawing.setLineWidth(3);
            drawing.setColor(new Color(19,0, 237, 1f));
            drawing.drawRoundedRectangle(getX(), getY(), getWidth(), getHeight(), getArcWidth(), getArcHeight());
            drawing.setWireFrame(false);
        }

        drawing.setColor(Color.WHITE);
        drawing.setFont(font);
        float tx = ( getX() + ((float) getWidth() /2) ) -  ( (float) font.getSize() * text.length() / 2);
        float ty = getY() + ((float) getHeight() /2) + (float) font.getSize() /3;
        drawing.drawText(text,tx,ty);
    }

    @Override
    public void addMouseEvent(Mouse mouse) {
        mouseList.add(mouse);
    }



    // set =====


    public void setFont(Font font) {
        this.font = font;
    }

    public void setArc(int arcW, int arcH) {
        this.arcW = arcW;
        this.arcH = arcH;
    }


    public void setText(String text) {
        this.text = text;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
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

    @Override
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

    // get =====


    @Override
    public int getX() {
        return location.getX();
    }

    @Override
    public int getY() {
        return location.getY();
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
    public Drawing getDraw() {
        return drawing;
    }

    @Override
    public List<Mouse> getMouseList() {
        return mouseList;
    }

    @Override
    public Color getBackground() {
        return background;
    }

    @Override
    public int getArcWidth() {
        return arcW;
    }

    @Override
    public int getArcHeight() {
        return arcH;
    }

    public boolean isSelected() {
        return selected;
    }

    public Font getFont() {
        return font;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

}
