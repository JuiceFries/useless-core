package org.useless.gui.picture;

import java.nio.ByteBuffer;
import org.useless.gui.data.Location;
import org.useless.gui.data.Size;

public interface Picture {
    // 基础属性
    String getPath();
    Size getSize();
    boolean isLoaded();

    // 变换操作
    void setScale(float scaleX, float scaleY);
    void setRotation(float degrees);
    void setOpacity(float opacity); // 0.0-1.0

    Size getOriginalSize();

    // 新增：获取变换属性
    float getScaleX();
    float getScaleY();
    float getRotation();
    float getOpacity();

    // 渲染控制
    void render(int x, int y);
    void render(Location position);
    void render(int x, int y, int width, int height); // 带缩放渲染

    // 资源管理
    void load() throws ImageLoadException;
    void dispose();

    // 状态查询
    default boolean isDisposed() {
        return !isLoaded();
    }

    // 新增：获取内部数据（为后续渲染器准备）
    Object getNativeHandle(); // 返回纹理ID或NVG图像句柄
    int getImageWidth();
    int getImageHeight();

    // 新增：获取图片数据
    ByteBuffer getImageData();
    int getChannels();

    // 新增：设置原生句柄
    void setNativeHandle(Object handle);
}