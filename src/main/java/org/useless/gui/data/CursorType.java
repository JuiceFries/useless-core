package org.useless.gui.data;

/**
 * 提供了几种光标
 * @see BasicTools
 */
public enum CursorType {
    ARROW(0x00036001),
    IBEAM(0x00036002),
    CROSSHAIR(0x00036003),
    HAND(0x00036004),
    HRESIZE(0x00036005),
    VRESIZE(0x00036006),
    NWSERESIZE(0x00036007),
    NESWRESIZE(0x00036008),
    ALLRESIZE(0x00036009),
    NOTALLOWED(0x0003600A);

    private final int cursor;
    CursorType(int cursor) {
        this.cursor = cursor;
    }

    public int getCursor() {
        return cursor;
    }

}
