package org.useless.uui.uir;

import org.useless.uui.Color;
import org.useless.uui.Drawing;

/**
 * 绘图层
 * @see Color
 * @see Drawing
 * @since 0.0.3
 */
public class RectLayer implements Layer {
    private int zIndex;
    private float x, y, width, height;
    private Color color;

    public RectLayer(int zIndex, float x, float y, float width, float height, Color color) {
        this.zIndex = zIndex;
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.color = color;
    }

    @Override public int getZIndex() { return zIndex; }
    @Override public void setZIndex(int zIndex) { this.zIndex = zIndex; }

    @Override
    public void draw(Drawing drawing) {
        drawing.setColor(color);
        drawing.drawRectangle(x, y, width, height);
    }
}
