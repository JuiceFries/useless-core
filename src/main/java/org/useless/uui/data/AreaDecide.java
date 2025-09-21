package org.useless.uui.data;

import org.useless.uui.Drawing;
import org.useless.uui.event.MouseEvent;
import org.useless.uui.template.Template;

public class AreaDecide {

    private static long AREA_DECIDE = 0xFFFFFFFFL;

    private AreaDecide(long AREA_DECIDE) {
        AreaDecide.AREA_DECIDE = AREA_DECIDE;
        if (AreaDecide.AREA_DECIDE != 0xFFFFFFFFL) System.out.println("AreaDecide!");
    }

    /**
     * 检测鼠标是否在范围内<br>
     * 用于给所有面板组件进行判断鼠标光标知否在区域内<br>
     * 当前仅支持简单的矩形检测
     *
     * @param drawing 画笔
     * @param template 组件
     * @return 在范围内返回 {@code true }未在范围内返回{@code false }
     * @see Drawing
     * @see Template
     * @see MouseEvent
     */
    public static boolean isMouseOver(Drawing drawing, Template template) {
        if (drawing == null || template == null) return false;

        Location mouseLoc = drawing.getMouseLocation();
        return mouseLoc.x >= template.getX() &&
                mouseLoc.y >= template.getY() &&
                mouseLoc.x <= template.getX() + template.getWidth() &&
                mouseLoc.y <= template.getY() + template.getHeight();
    }

}
