package org.useless.uui;

import org.useless.uui.data.Size;

public abstract class Image implements Picture {
    protected long textureId = -1;
    protected Size size = Size.ZERO;
    protected volatile boolean loaded = false;
    protected String path; // 记录加载路径

    @Override
    public void draw(Drawing drawing, float x, float y) {
        if (!isLoaded()) return;
        drawing.drawPicture(this, x, y, size.width, size.height);
    }

    @Override
    public void draw(Drawing drawing, float x, float y, float width, float height) {
        if (!isLoaded()) return;
        drawing.drawPicture(this, x, y, width, height);
    }

    @Override
    public Size getSize() {
        return new Size(size); // 返回副本避免外部修改
    }

    @Override
    public long getTextureId() {
        return textureId;
    }

    @Override
    public boolean isLoaded() {
        return loaded && textureId != -1;
    }

    @Override
    public void dispose() {
        if (textureId != -1) {
            // 具体纹理释放由子类实现
            textureId = -1;
        }
        loaded = false;
    }

    public abstract void bindTexture();
    public abstract void loadImpl(String path); // 实际加载实现

    @Override
    public void load(String path) {
        if (isLoaded()) dispose();
        this.path = path;
        loadImpl(path);
    }

    @Override
    public String getPath() {
        return path;
    }

}