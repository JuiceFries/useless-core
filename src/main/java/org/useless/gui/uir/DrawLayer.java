package org.useless.gui.uir;

import java.util.function.Consumer;
import org.useless.gui.drawing.Drawing;
import org.useless.annotation.FullName;

/**
 * 绘图层
 */
@FullName(fullName = "DrawingLayer")
public class DrawLayer implements Layer {

    private int zIndex;
    private Consumer<Drawing> drawing;

    public DrawLayer(int zIndex, Consumer<Drawing> drawing) {
        this.zIndex = zIndex;
        this.drawing = drawing;
    }

    @Override public int getZIndex() { return zIndex; }
    @Override public void setZIndex(int zIndex) { this.zIndex = zIndex; }

    @Override
    public void draw(Drawing drawing) {
        this.drawing.accept(drawing);
    }

}
