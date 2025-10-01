package org.useless.gui.data;

/**
 * 自己看去
 * @see java.awt.Dimension
 */
public class Location {
    public int x, y;

    /**
     * 无参数构造方法<br>
     * 容易提供无参数构造方法，默认为 0
     * @see Location
     */
    public Location(){this(0,0);}

    /**
     * 坐标构造方法<br>
     * 用于传入 [ Location ] 类型坐标
     * @param l 坐标
     */
    public Location(Location l){this(l.getX(),l.getY());}

    /**
     * 默认构造方法<br>
     * 提供了起始点的直接赋值
     * @param x 横向坐标
     * @param y 竖向纵坐标
     */
    public Location(int x, int y) {this.x = x;this.y = y;}
    // set ==========

    /**
     * 用以设置坐标
     * @param x 横向坐标
     * @param y 竖向坐标
     */
    public final void setLocation(int x, int y) {this.x = x;this.y = y;}

    /**
     * 用以设置坐标
     * @param l 坐标
     */
    public final void setLocation(Location l) {
        setLocation(l.x, l.y);
    }

    /**
     * 设置横向坐标
     * @param x 横向坐标
     */
    public final void setX(int x) {this.x = x;}

    /**
     * 设置竖向坐标
     * @param y 竖向坐标
     */
    public final void setY(int y) {this.y = y;}
    // get ==========
    public final Location getValue() {return new Location(x, y);}
    public final int getX() {return x;}
    public final int getY() {return y;}
    // stm ==========
    public boolean isValid() {
        return this != null && x >= 0 && y >= 0;
    }
    public String toString() {return getClass().getName() + "[x=" + x + ",y=" + y + "]";}
}
