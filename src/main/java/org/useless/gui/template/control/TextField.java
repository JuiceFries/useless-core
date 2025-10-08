package org.useless.gui.template.control;

// 标准库导入
import java.util.LinkedList;
// 注解库导入
import org.jetbrains.annotations.NotNull;
// 核心包导入
import org.useless.gui.data.Color;
import org.useless.gui.data.Location;
import org.useless.gui.drawing.Drawing;
import org.useless.gui.event.KeyboardEvent;
import org.useless.gui.event.Mouse;
import org.useless.gui.event.MouseEvent;
import org.useless.gui.font.Font;
import org.useless.annotation.Fixed;

// 静态方法
import static java.lang.Character.isWhitespace;
import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.valueOf;
import static java.lang.System.currentTimeMillis;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwGetClipboardString;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwSetClipboardString;
import static org.useless.gui.data.BasicTools.isMouseOver;
import static org.useless.gui.data.BasicTools.selectionBoxDraw;
import static org.useless.gui.data.BasicTools.setCursor;
import static org.useless.gui.data.Color.WHITE;
import static org.useless.gui.data.CursorType.ARROW;
import static org.useless.gui.data.CursorType.IBEAM;
import static org.useless.gui.event.Input.KEY_A;
import static org.useless.gui.event.Input.KEY_BACKSPACE;
import static org.useless.gui.event.Input.KEY_C;
import static org.useless.gui.event.Input.KEY_DELETE;
import static org.useless.gui.event.Input.KEY_DOWN;
import static org.useless.gui.event.Input.KEY_END;
import static org.useless.gui.event.Input.KEY_HOME;
import static org.useless.gui.event.Input.KEY_LEFT;
import static org.useless.gui.event.Input.KEY_RIGHT;
import static org.useless.gui.event.Input.KEY_TAB;
import static org.useless.gui.event.Input.KEY_UP;
import static org.useless.gui.event.Input.KEY_V;
import static org.useless.gui.event.Input.KEY_X;
import static org.useless.gui.event.Input.KEY_Y;
import static org.useless.gui.event.Input.KEY_Z;
import static org.useless.gui.font.Font.DEFAULT;

/**
 * 单行文本框<br>
 * 一个简单的单行文本框
 * @since 0.0.3
 * @see org.useless.gui.event.Input
 * @see org.useless.gui.template.Control
 * @see org.useless.gui.template.control.BlankControl
 * @see org.useless.gui.template.Template
 * @see org.useless.gui.uir.Layer
 */
@Fixed(continuous = "~3-Beta")
public class TextField extends BlankControl {
    // 配置项
    private String text;
    private Font font = new Font(DEFAULT, 16);
    private int arcWidth = 9, arcHeight = 9;
    private Color backgroundColor = new Color(60, 60, 60, 1f);
    private Color selectionColor = new Color(0, 120, 215, 0.5f);
    private Color borderColor = new Color(19, 0, 237, 1f);

    // 状态变量
    private int cursor;
    private int selectionStart = -1, selectionEnd = -1;
    private boolean selected = false;
    private boolean cursorVisible = true;
    private long lastBlinkTime = currentTimeMillis();
    private Location mousePosition;

    // 历史记录（简单撤销）
    private final LinkedList<String> history = new LinkedList<>();
    private static final int MAX_HISTORY = 50;

    public TextField() {
        this("");
    }

    public TextField(@NotNull String text) {
        this.text = text;
        this.cursor = text.length();
        history.add(text);
        initEvents();
    }

    // ==================== 配置方法 ====================
    public void setText(@NotNull String text) {
        this.text = text;
        this.cursor = min(cursor, text.length());
        clearSelection();
    }

    public void setFont(Font font) { this.font = font; }
    public void setArc(int width, int height) { this.arcWidth = width; this.arcHeight = height; }
    public void setBackgroundColor(Color color) { this.backgroundColor = color; }
    public void setSelectionColor(Color color) { this.selectionColor = color; }
    public void setBorderColor(Color color) { this.borderColor = color; }

    public String getText() { return text; }
    public Font getFont() { return font; }

    // ==================== 渲染 ====================
    @Override
    public void draw(Drawing drawing) {
        super.draw(drawing);
        drawing.setClip(getX(), getY(), getWidth(), getHeight());

        // 背景
        drawing.setColor(backgroundColor);
        drawing.drawRoundedRectangle(getX() + 3, getY() + 3, getWidth() - 6, getHeight() - 6, arcWidth, arcHeight);

        // 选中边框
        selectionBoxDraw(selected,4f,3f,this,borderColor);

        // 鼠标样式
        setCursor(this, isMouseOver(mousePosition, this) ? IBEAM : ARROW);

        // 文本选中高亮
        if (hasSelection()) {
            int start = min(selectionStart, selectionEnd);
            int end = max(selectionStart, selectionEnd);
            float highlightX = getX() + 15 + drawing.measureText(text.substring(0, start), font);
            float highlightWidth = drawing.measureText(text.substring(start, end), font);
            drawing.setColor(selectionColor);
            drawing.drawRectangle(highlightX, getY() + 8, highlightWidth, getHeight() - 16);
        }

        // 文本
        drawing.setFont(font);
        drawing.setColor(WHITE);
        drawing.drawText(text, getX() + 15, getY() + (float) getHeight() / 2 + 4);

        // 光标
        if (selected && shouldShowCursor()) {
            float cursorX = getX() + 15 + drawing.measureText(text.substring(0, cursor), font);
            drawing.setColor(WHITE);
            drawing.setLineWidth(1.5f);
            drawing.drawLine(cursorX, getY() + 8, cursorX, getY() + getHeight() - 8);
        }

        drawing.resetClip();
    }

    // ==================== 事件处理 ====================
    private void initEvents() {
        // 鼠标事件
        addMouseEvent(new MouseEvent() {
            @Override
            public void clickEvent(int key) {
                boolean wasSelected = selected;
                selected = (key == Mouse.LEFT_CLICK) && isMouseOver(mousePosition, TextField.this);

                if (selected && !wasSelected) {
                    cursor = text.length();
                    cursorVisible = true;
                    lastBlinkTime = currentTimeMillis();
                }
                clearSelection();
            }

            @Override
            public void mouseCoordinates(Location location) {
                mousePosition = location;
            }
        });

        // 键盘事件
        addInputEvent(new KeyboardEvent() {
            @Override
            public void pressKey(int key) {
                keyboardDetection(key);
            }

            @Override
            public void longPress(int key) {
                keyboardDetection(key);
            }

            @Override
            public void inputKey(char input) {
                if (!selected) return;

                if (input >= 32 && input != 127) { // 可打印字符
                    if (hasSelection()) {
                        deleteSelection();
                    }
                    insertText(valueOf(input));
                }
            }
        });
    }

    private void keyboardDetection(int key) {
        if (!selected) return;

        boolean ctrl = isCtrlPressed();
        boolean shift = isShiftPressed();

        // 处理选择
        if (shift && !hasSelection()) {
            selectionStart = cursor;
        }

        switch (key) {
            case KEY_LEFT:
                moveCursor(-1, ctrl);
                break;
            case KEY_RIGHT:
                moveCursor(1, ctrl);
                break;
            case KEY_UP:
            case KEY_HOME:
                cursor = 0;
                break;
            case KEY_DOWN:
            case KEY_END:
                cursor = text.length();
                break;
            case KEY_BACKSPACE:
                deleteText(-1);
                break;
            case KEY_DELETE:
                deleteText(1);
                break;
            case KEY_TAB:
                insertText("    ");
                break;
            case KEY_A: // 全选
                if (ctrl) selectAll();
                break;
            case KEY_C: // 复制
                if (ctrl) copy();
                break;
            case KEY_X: // 剪切
                if (ctrl) { copy(); deleteSelection(); }
                break;
            case KEY_V: // 粘贴
                if (ctrl) paste();
                break;
            case KEY_Z: // 撤销
                if (ctrl) undo();
                break;
            case KEY_Y: // 重做（简单实现）
                if (ctrl) redo();
                break;
        }

        // 更新选择结束位置
        if (shift) {
            selectionEnd = cursor;
        } else if (!ctrl) { // 非组合键清除选择
            clearSelection();
        }

        updateCursor();
    }

    // ==================== 核心功能 ====================
    private void moveCursor(int direction, boolean ctrl) {
        if (ctrl) {
            cursor = (direction > 0) ? findWordEnd(cursor) : findWordStart(cursor);
        } else {
            cursor = max(0, min(text.length(), cursor + direction));
        }
    }

    private void insertText(@NotNull String newText) {
        saveToHistory();
        text = text.substring(0, cursor) + newText + text.substring(cursor);
        cursor += newText.length();
        clearSelection();
    }

    private void deleteText(int direction) {
        if (hasSelection()) {
            deleteSelection();
            return;
        }

        saveToHistory();

        if (direction < 0 && cursor > 0) { // 退格
            text = text.substring(0, cursor - 1) + text.substring(cursor);
            cursor--;
        } else if (direction > 0 && cursor < text.length()) { // 删除
            text = text.substring(0, cursor) + text.substring(cursor + 1);
        }
    }

    private void deleteSelection() {
        if (!hasSelection()) return;

        saveToHistory();

        int start = min(selectionStart, selectionEnd);
        int end = max(selectionStart, selectionEnd);
        text = text.substring(0, start) + text.substring(end);
        cursor = start;
        clearSelection();
    }

    private void selectAll() {
        selectionStart = 0;
        selectionEnd = text.length();
        cursor = text.length();
    }

    private void copy() {
        if (hasSelection()) {
            int start = min(selectionStart, selectionEnd);
            int end = max(selectionStart, selectionEnd);
            String selectedText = text.substring(start, end);
            glfwSetClipboardString(getWindowHandle(), selectedText);
        }
    }

    private void paste() {
        String clipboard = glfwGetClipboardString(getWindowHandle());
        if (clipboard != null && !clipboard.isEmpty()) {
            saveToHistory();
            if (hasSelection()) deleteSelection();
            insertText(clipboard);
        }
    }

    private void undo() {
        if (history.size() > 1) {
            history.removeLast(); // 移除当前状态
            text = history.getLast();
            cursor = min(cursor, text.length());
            clearSelection();
        }
    }

    private void redo() {
        undo(); // 这里简单处理
    }

    // ==================== 工具方法 ====================
    private boolean shouldShowCursor() {
        long currentTime = currentTimeMillis();
        if (currentTime - lastBlinkTime > 500) {
            cursorVisible = !cursorVisible;
            lastBlinkTime = currentTime;
        }
        return cursorVisible;
    }

    private void updateCursor() {
        cursorVisible = true;
        lastBlinkTime = currentTimeMillis();
    }

    private void saveToHistory() {
        history.add(text);
        if (history.size() > MAX_HISTORY) {
            history.removeFirst();
        }
    }

    private boolean hasSelection() {
        return selectionStart != selectionEnd && selectionStart >= 0 && selectionEnd >= 0;
    }

    private void clearSelection() {
        selectionStart = selectionEnd = -1;
    }

    private int findWordStart(int position) {
        if (position <= 0) return 0;

        // 跳过连续的空格
        while (position > 0 && isWhitespace(text.charAt(position - 1))) {
            position--;
        }

        // 找到单词开头
        while (position > 0 && !isWhitespace(text.charAt(position - 1))) {
            position--;
        }

        return position;
    }

    private int findWordEnd(int position) {
        if (position >= text.length()) return text.length();

        // 跳过当前单词
        while (position < text.length() && !isWhitespace(text.charAt(position))) {
            position++;
        }

        // 跳过连续的空格
        while (position < text.length() && isWhitespace(text.charAt(position))) {
            position++;
        }

        return position;
    }

    private boolean isCtrlPressed() {
        long handle = getWindowHandle();
        return glfwGetKey(handle, GLFW_KEY_LEFT_CONTROL) == GLFW_PRESS ||
                glfwGetKey(handle, GLFW_KEY_RIGHT_CONTROL) == GLFW_PRESS;
    }

    private boolean isShiftPressed() {
        long handle = getWindowHandle();
        return glfwGetKey(handle, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS ||
                glfwGetKey(handle, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS;
    }

    private long getWindowHandle() {
        return getDraw().getHandle().glHandle();
    }

}
