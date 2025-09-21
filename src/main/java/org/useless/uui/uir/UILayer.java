package org.useless.uui.uir;

import org.useless.uui.Drawing;

import java.util.function.Consumer;

/**
 * UI渲染层
 * @see Drawing
 * @see Consumer
 */
public class UILayer implements Layer {
    private int zIndex;
    private final Consumer<Drawing> renderCallback;

    public UILayer(int zIndex, Consumer<Drawing> renderCallback) {
        this.zIndex = zIndex;
        this.renderCallback = renderCallback;
    }

    @Override public int getZIndex() { return zIndex; }
    @Override public void setZIndex(int zIndex) { this.zIndex = zIndex; }

    @Override
    public void draw(Drawing drawing) {
        if (renderCallback != null) renderCallback.accept(drawing);
    }
}
