package org.useless.uui.data;

/**
 * 自己看用途
 */
public class Size {

    /**
     * 宽
     */
    public int width;

    /**
     * 高
     */
    public int height;

    // 在Size类中添加常量
    public static final Size ZERO = new Size(0, 0);
    public static final Size ONE = new Size(1, 1);

    /**
     * 无参构造方法
     */
    public Size() {
        this.width = 0;
        this.height = 0;
    }

    /**
     * 构造方法
     * @param size 大小
     */
    public Size(Size size) {
        this(size.width, size.height);
    }

    /**
     * 构造方法
     * @param width 宽
     * @param height 高
     */
    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 设置宽度
     * @param width 宽
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 设置高
     * @param height 高
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 设置大小
     * @param width 宽
     * @param height 高
     */
    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * 设置大小
     * @param size 大小
     */
    public void setSize(Size size) {
        setSize(size.width, size.height);
    }

    /**
     * 返回参数
     * @return 宽
     */
    public int getWidth() {
        return width;
    }

    /**
     * 返回参数
     * @return 高
     */
    public int getHeight() {
        return height;
    }

    /**
     * 返回参数
     * @return 大小
     */
    public Size getSize() {
        return new Size(width, height);
    }

    // stm ==========

    /**
     * 判断是否为负数
     * @return true / false
     */
    public boolean isValid() {
        return this != null && width >= 0 && height >= 0;
    }

    /**
     * 打印大小
     * @return 大小
     */
    public String toString() {return getClass().getName() + "[width=" + width + ",height=" + height + "]";}

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Size other)) return false;
        return width == other.width && height == other.height;
    }

    @Override
    public int hashCode() {
        return 31 * width + height;
    }

    // 顺便加个实用方法
    public boolean equals(int width, int height) {
        return this.width == width && this.height == height;
    }

}
