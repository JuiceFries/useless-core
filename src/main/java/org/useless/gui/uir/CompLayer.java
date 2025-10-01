package org.useless.gui.uir;

import org.useless.gui.drawing.Drawing;
import org.useless.gui.uir.annotation.FullName;

/**
 * 组件渲染层
 */
@FullName(fullName = "ComponentLayer")
public class CompLayer implements Layer {
    private int zIndex = 500; // 默认模板层
    private Runnable renderTask; // 实际渲染逻辑

    public CompLayer(int zIndex, Runnable renderTask) {
        setZIndex(zIndex);
        this.renderTask = renderTask;
    }

    @Override
    public void draw(Drawing drawing) {
        if (renderTask != null) {
            renderTask.run();
        }
    }

    @Override
    public void setZIndex(int zIndex) {
        if (zIndex < 1 || zIndex > 999) {
            System.out.println("图层zIndex应在1-999范围内");
            return;
        }
        this.zIndex = zIndex;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }
}
