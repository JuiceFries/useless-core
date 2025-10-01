package org.useless.gui.data;

import java.util.HashMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.useless.gui.template.Control;
import org.useless.gui.template.Template;
import org.useless.gui.uir.annotation.FullName;

import static org.lwjgl.glfw.GLFW.glfwCreateStandardCursor;
import static org.lwjgl.glfw.GLFW.glfwSetCursor;

/**
 * 基础工具提供了简单的基础静态工具
 */
public class BasicTools {

    private static long AREA_DECIDE = 0xFFFFFFFFL;

    private BasicTools(long AREA_DECIDE) {
        BasicTools.AREA_DECIDE = AREA_DECIDE;
        if (BasicTools.AREA_DECIDE != 0xFFFFFFFFL) System.out.println("AreaDecide!");
    }

    /**
     * 检测鼠标是否在范围内<br>
     * 用于给所有控件组件进行判断鼠标光标知否在区域内<br>
     * 当前仅支持简单的矩形检测
     *
     * @return 在范围内返回 {@code true }未在范围内返回{@code false }
     * @see Location
     * @see Control
     */
    public static boolean isMouseOver(Location mousePosition,Control control) {
        if (control == null || mousePosition == null) return false;
        return mousePosition.x >= control.getX() && mousePosition.y >= control.getY() &&
                mousePosition.x <= control.getX() + control.getWidth() &&
                mousePosition.y <= control.getY() + control.getHeight();
    }

    /// 光标缓存表
    private static final Map<CursorType, Long> cursorCache = new HashMap<>();

    /**
     * 用于设置鼠标样式
     * @param template 模板
     * @param cursorType 样式
     * @see Template
     * @see CursorType
     */
    public static void setCursor(@NotNull Template template, CursorType cursorType) {
        Long cursor = cursorCache.computeIfAbsent(cursorType,
                k -> glfwCreateStandardCursor(cursorType.getCursor()));
        glfwSetCursor(template.getDraw().getHandle().glHandle(), cursor);
    }

    /**
     * 清理光标缓存
     */
    @FullName(fullName = "clearCursorCache")
    public static void clearCursor() {
        cursorCache.clear();
    }

    /**
     * 绘制选中框<br>
     * 闲得没事写的方法
     * @param selected 选中状态
     * @param lineWidth 线宽
     * @param internalOffset 内偏移
     * @param control 控件
     * @param color 边框颜色
     * @see Control
     * @see Color
     */
    @FullName(fullName = "selectionBorderDrawing")
    public static void selectionBoxDraw(boolean selected,float lineWidth,float internalOffset,Control control,Color color) {
        if (selected) {
            control.getDraw().setWireFrame(true);
            control.getDraw().setLineWidth(lineWidth);
            control.getDraw().setColor(color);
            control.getDraw().drawRoundedRectangle(
                    control.getX()+internalOffset, control.getY()+internalOffset,
                    control.getWidth()-(internalOffset*2),control.getHeight()-(internalOffset*2),
                    control.getArcWidth(), control.getArcHeight()
            );
            control.getDraw().setWireFrame(false);
        }
    }



}
