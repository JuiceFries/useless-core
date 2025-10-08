package org.useless.gui.font;

import static org.useless.gui.font.FontRegistry.getFont;

/**
 * 字体数据类
 */
public class Font {
    private final String name;
    private final Style style;
    private final int size;

    public static final String DEFAULT = "思源黑体";
    public static final String SIM_SUN = "霞鹜文楷";

    public Font(String name, Style style, int size) {
        this.name = name;
        this.style = style;
        this.size = size;
    }

    public Font(String name, int size) {
        this(name, Style.PLAIN, size);
    }

    // Getters
    public String getName() { return name; }
    public Style getStyle() { return style; }
    public int getSize() { return size; }

    // 衍生字体
    public Font derive(Style newStyle) {
        return new Font(name, newStyle, size);
    }

    public Font deriveSize(int newSize) {
        return new Font(name, style, newSize);
    }

    /**
     * 验证字体是否可用
     */
    public boolean isValid() {
        return getFont(name) != null;
    }

    /**
     * 获取可用的字体名称（如果请求的字体不存在则返回默认字体）
     */
    public String getAvailableName() {
        return getFont(name) != null ? name : DEFAULT;
    }

}