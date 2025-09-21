package org.useless.uui.image;

import org.useless.uui.Image;
import org.useless.uui.data.Size;
import static org.lwjgl.nanovg.NanoVG.*;

public class NVGImage extends Image {
    private long nvgContext;
    private int nvgImageHandle = -1;

    public NVGImage(long nvgContext) {
        this.nvgContext = nvgContext;
    }

    @Override
    public void loadImpl(String path) {
        nvgImageHandle = nvgCreateImage(nvgContext, path, 0);
        if (nvgImageHandle == -1) throw new RuntimeException("NVG加载图片失败: " + path);

        try (var stack = org.lwjgl.system.MemoryStack.stackPush()) {
            var w = stack.mallocInt(1);
            var h = stack.mallocInt(1);
            nvgImageSize(nvgContext, nvgImageHandle, w, h);
            size = new Size(w.get(), h.get());
            loaded = true;
        }
    }

    @Override
    public void bindTexture() {
        // NanoVG无需显式绑定
    }

    public int getNVGImageHandle() {
        return nvgImageHandle;
    }

    @Override
    public void dispose() {
        if (nvgImageHandle != -1) {
            nvgDeleteImage(nvgContext, nvgImageHandle);
            nvgImageHandle = -1;
        }
        super.dispose();
    }

    @Override
    public boolean loadAsync(String path) {
        new Thread(() -> {
            load(path); // 直接复用同步加载
        }).start();
        return true; // 简单返回true表示已开始加载
    }

    // 在NVGImage里重写load方法，确保同步
    @Override
    public void load(String path) {
        if (isLoaded()) dispose();
        this.path = path;
        loadImpl(path); // 直接同步加载
    }

}