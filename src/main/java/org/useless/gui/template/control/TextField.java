package org.useless.gui.template.control;

import org.useless.gui.data.BasicTools;
import org.useless.gui.data.Color;
import org.useless.gui.data.Location;
import org.useless.gui.drawing.Drawing;
import org.useless.gui.event.KeyboardEvent;
import org.useless.gui.event.Mouse;
import org.useless.gui.event.MouseEvent;
import org.useless.gui.font.Font;
import org.useless.gui.uir.annotation.FullName;

import static org.useless.gui.data.BasicTools.isMouseOver;
import static org.useless.gui.data.BasicTools.selectionBoxDraw;
import static org.useless.gui.data.BasicTools.setCursor;
import static org.useless.gui.data.Color.WHITE;
import static org.useless.gui.data.CursorType.ARROW;
import static org.useless.gui.data.CursorType.IBEAM;
import static org.useless.gui.font.Font.DEFAULT;

public class TextField extends BlankControl {
    private int arcWidth = 9, arcHeight = 9;
    private boolean selected = false;
    private Location mousePosition;
    private String text;
    private Font font = new Font(DEFAULT, 16);
    private int okay = 0;
    private int column = 0;
    private float cursorPosition = getX() + 5;

    public TextField() {
        this("");
    }

    public TextField(String text) {
        this.text = text;
        setBackground(new Color(60, 60, 60, 1f));
        rDE();
    }

    @Override
    public void draw(Drawing drawing) {
        super.draw(drawing);
        drawing.setClip(getX(), getY(), getWidth(), getHeight());

        // 绘制背景
        drawing.setColor(getBackground());
        drawing.drawRoundedRectangle(getX() + 3, getY() + 3, getWidth() - 6, getHeight() - 6, getArcWidth(), getArcHeight());

        setCursor(this, isMouseOver(mousePosition, this) ? IBEAM : ARROW);
        selectionBoxDraw(selected, 4, 3, this, new Color(19, 0, 237, 1f));

        drawing.setFont(font);
        drawing.setColor(WHITE);
        drawing.setCharSpacing(3f);

        float wx = (getX() + 15f);
        float wy = (getY() + 6) + ((float) getHeight() / 2 + ((float) (font.getSize() / 2) / 5.5f));

        drawing.drawText(text,wx,wy);

        drawing.setColor(Color.BLACK);
        cursorPosition++;
        if (cursorPosition >= 400) {
            cursorPosition = getX()+5;
        }
        drawing.setLineWidth(2f);
        drawing.drawLine(cursorPosition,getY()+5, cursorPosition,getY()+getWidth());

        drawing.resetClip();
    }

    // set方法
    public void setArc(int arcWidth, int arcHeight) {
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
    }

    public void setText(String text) {
        this.text = text;

    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    // get方法
    public boolean isSelected() {
        return selected;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getArcHeight() {
        return arcHeight;
    }

    @Override
    public int getArcWidth() {
        return arcWidth;
    }

    // 事件注册
    @FullName(fullName = "registerDefaultEvent")
    private void rDE() {
        addMouseEvent(new MouseEvent() {
            @Override
            public void clickEvent(int key) {
                selected = key == Mouse.LEFT_CLICK && BasicTools.isMouseOver(mousePosition, TextField.this);
            }

            @Override
            public void mouseCoordinates(Location location) {
                mousePosition = location;
            }
        });

        addInputEvent(new KeyboardEvent() {
            @Override
            public void pressKey(int key) {
            }

            @Override
            public void loosenKey(int key) {

            }

            @Override
            public void longPress(int key) {

            }

            @Override
            public void inputKey(char input) {

            }
        });
    }
}