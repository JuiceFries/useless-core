package org.useless.gui.drawing;

import org.jetbrains.annotations.NotNull;
import org.useless.gui.uir.IllegalComponentException;

/**
 * 绘图实现<br>
 * 用来切换绘图实现
 * @see VGBrush
 * @since 0.0.4
 * @version 0.0.1
 */
public enum DrawImpl {
    /** 使用 {@code OpenGL } 渲染 */
    @SuppressWarnings("removal") @Deprecated(since = "0.0.3",forRemoval = true)
    OpenGL(GLBrush.class),
    /** 使用 {@code NanoVG } 渲染 */
    NanoVG(VGBrush.class);

    /// 绘图实例
    private final Class<? extends Drawing> clazz;

    /**
     * 注册画笔
     * @param clazz 画笔
     */
    DrawImpl(Class<? extends Drawing> clazz) {
        this.clazz = clazz;
    }

    /// 获取画笔
    public @NotNull Drawing brush() {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalComponentException("创建Drawing实例失败", e);
        }
    }

}
