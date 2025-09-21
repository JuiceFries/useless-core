package org.useless.uui;

import org.useless.uui.data.Size;

/**
 * 用以处理图片
 */
public interface Picture {

    void load(String path); // 同步加载
    boolean loadAsync(String path); // 异步加载
    Size getSize();
    void draw(Drawing drawing, float x, float y); // 注入Drawing上下文
    void draw(Drawing drawing, float x, float y, float width, float height); // 带缩放
    long getTextureId(); // OpenGL纹理ID
    void dispose(); // 释放资源
    boolean isLoaded(); // 加载状态检查
    String getPath();

}
