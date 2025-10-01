package org.useless.gui.picture;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.useless.gui.data.Location;
import org.useless.gui.data.Size;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Image implements Picture {
    protected String path;
    protected Size size = new Size(0, 0);
    protected boolean loaded = false;
    protected boolean disposed = false;

    protected float scaleX = 1.0f;
    protected float scaleY = 1.0f;
    protected float rotation = 0.0f;
    protected float opacity = 1.0f;

    // STB_image加载的数据
    protected ByteBuffer imageData;
    protected int channels;
    protected Object nativeHandle;

    public Image(String path) {
        this.path = path;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Size getSize() {
        return new Size(
                (int)(size.width * scaleX),
                (int)(size.height * scaleY)
        );
    }

    @Override
    public boolean isLoaded() {
        return loaded && !disposed;
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public void setRotation(float degrees) {
        this.rotation = degrees;
    }

    @Override
    public void setOpacity(float opacity) {
        this.opacity = Math.max(0, Math.min(1, opacity));
    }

    @Override
    public void render(int x, int y) {
        if (!isLoaded()) {
            System.err.println("图片未加载: " + path);
            return;
        }
        System.out.println("渲染图片到: " + x + ", " + y + " [尺寸: " + getSize() + "]");
    }

    @Override
    public void render(Location position) {
        render(position.x, position.y);
    }

    @Override
    public void render(int x, int y, int width, int height) {
        if (!isLoaded()) {
            System.err.println("图片未加载: " + path);
            return;
        }
        System.out.println("渲染图片到: " + x + ", " + y + " [缩放尺寸: " + width + "x" + height + "]");
    }

    @Override
    public void load() throws ImageLoadException {
        if (disposed) {
            throw new ImageLoadException("图片已释放，无法重新加载: " + path);
        }

        if (loaded) {
            return;
        }

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.mallocInt(1);
            IntBuffer height = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            imageData = STBImage.stbi_load(path, width, height, comp, 4);
            if (imageData == null) {
                throw new ImageLoadException("STB_image加载失败: " + path + " - " + STBImage.stbi_failure_reason());
            }

            this.size = new Size(width.get(0), height.get(0));
            this.channels = comp.get(0);
            this.loaded = true;

        } catch (Exception e) {
            if (e instanceof ImageLoadException) throw (ImageLoadException) e;
            throw new ImageLoadException("图片加载失败: " + path, e);
        }
    }

    @Override
    public void dispose() {
        if (disposed) return;

        if (imageData != null) {
            STBImage.stbi_image_free(imageData);
            imageData = null;
        }

        nativeHandle = null;
        disposed = true;
        loaded = false;
    }

    @Override
    public Object getNativeHandle() {
        return nativeHandle;
    }

    @Override
    public int getImageWidth() {
        return size.width;
    }

    @Override
    public int getImageHeight() {
        return size.height;
    }

    @Override
    public ByteBuffer getImageData() {
        if (!isLoaded()) {
            throw new IllegalStateException("图片未加载: " + path);
        }
        return imageData;
    }

    @Override
    public int getChannels() {
        return channels;
    }

    @Override
    public void setNativeHandle(Object handle) {
        this.nativeHandle = handle;
    }

    @Override
    public Size getOriginalSize() {
        return size;
    }

    public float getScaleX() { return scaleX; }
    public float getScaleY() { return scaleY; }
    public float getRotation() { return rotation; }
    public float getOpacity() { return opacity; }

    @Override
    public String toString() {
        return String.format("Image{path='%s', size=%dx%d, loaded=%s, disposed=%s}",
                path, size.width, size.height, loaded, disposed);
    }
}