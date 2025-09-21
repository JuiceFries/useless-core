package org.useless.uui.template.control;

import org.useless.uui.Color;
import org.useless.uui.Drawing;
import org.useless.uui.Font;
import org.useless.uui.data.AreaDecide;
import org.useless.uui.data.Location;
import org.useless.uui.data.Size;
import org.useless.uui.event.MouseEvent;
import org.useless.uui.font.FontStyle;
import org.useless.uui.template.Control;
import org.useless.uui.uir.ButtonDRI;
import org.useless.uui.uir.FullName;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>按钮<h2>
 * 一个目前还无法显示图标的按钮
 * @see Color
 * @see Drawing
 * @see AreaDecide
 * @see Location
 * @see Size
 * @see MouseEvent
 */
public class Button implements Control {
    private int zIndex = 300; // 默认值
    private Location location = new Location();
    private Size size = new Size();
    private Color Background = new Color(69, 69, 69);
    private volatile Drawing drawing;
    private volatile int arcWidth,arcHeight,arc;
    private String text;
    private Font font = new Font(Font.DEFAULT, FontStyle.PLAIN,12);
    private float lineWidth = 5.0f;
    private List<MouseEvent> mouseEventList = new ArrayList<>();
    private boolean AutomaticJudgment = false;

    ButtonDRI dri = ButtonDRI.init();
    private boolean select = false;

    public Button() {
        this(null);
    }

    public Button(String text) {
        this.text = text != null ? text : "";
        dri.defEventImpl(this);
    }


    // stm =====

    @Override
    public void draw(Drawing drawing) {
        this.drawing = drawing;
        if (Font.getNVGFontHandle(font.getName()) < 0) {
            System.err.println("字体未加载，跳过文本渲染");
            return;
        }

        dri.hoverJudgment(this);

        dri.autoJudge(this);

        dri.selectedFrame(this);

        dri.renderText(this);
    }

    @Override
    public void enrolledInput(org.useless.uui.event.Input input) {
        // 按钮不需要键盘输入，空实现
    }

    @Override
    public void logoutEvent(org.useless.uui.Event event) {
        if (event instanceof MouseEvent) {
            mouseEventList.remove(event);
        }
    }


    @Override
    public void enrolledMouse(MouseEvent mouse) {
        mouseEventList.add(mouse);
    }

    // set =====

    @Override
    public void setBounds(int x, int y, int width, int height) {
        setLocation(x, y);
        setSize(width, height);
    }

    @Override
    public void setBounds(Location location, Size size) {
        setLocation(location);
        setSize(size);
    }

    /**
     * 自动特殊绘制判断<br>
     * 设置按钮的特定绘制自动判断
     * @param aj 自动判断状态
     * @see ButtonDRI
     * @since 0.0.2
     */
    @FullName(fullName = "setAutomaticJudgment")
    public void setAJ(boolean aj) {
        this.AutomaticJudgment = aj;
    }

    @Override
    public void setLocation(int x,int y) {
        setLocation(new Location(x, y));
    }

    @Override
    public void setSize(int width,int height) {
        setSize(new Size(width,height));
    }

    public void setLineWidth(float lineWidth) {
        this.lineWidth = lineWidth;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public void setSize(Size size) {
        this.size = size;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

    /**
     * 设置按钮的弧<br>
     *
     * @param arcWidth 圆角的水平半径
     * @param arcHeight 圆角的垂直半径
     * @param arc 圆角的弧度
     * @throws IllegalArgumentException 弧必须是 > 0
     * @since 0.0.2
     */
    public void setArc(int arcWidth,int arcHeight,int arc) {
        if (arcWidth <= -1 || arcHeight <= -1 || arc <= -1) throw new IllegalArgumentException("弧必须是 > 0");
        this.arcWidth = arcWidth;
        this.arcHeight = arcHeight;
        this.arc = arc;
    }


    @Override
    public void setBackground(Color background) {
        Background = background;
    }

    public void setDri(ButtonDRI dri) {
        this.dri = dri;
    }

    // get =====


    public ButtonDRI getDri() {
        return dri;
    }

    @Override
    public int getX() {
        return location.x;
    }

    @Override
    public List<org.useless.uui.event.Input> getInputList() {
        return List.of(); // 返回空列表
    }

    @Override
    public int getY() {
        return location.y;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getWidth() {
        return size.width;
    }

    @Override
    public int getHeight() {
        return size.height;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @FullName(fullName = "isAutomaticJudgment")
    public boolean isAJ() {
        return AutomaticJudgment;
    }

    @Override
    public Color getBackground() {
        return Background;
    }

    public Font getFont() {
        return font;
    }

    public String getText() {
        return text;
    }

    @Override
    public List<MouseEvent> getMouseEventList() {
        return mouseEventList != null ? mouseEventList : List.of();
    }

    public Drawing getDrawing() {
        return drawing;
    }

    public boolean isSelect() {
        return select;
    }

    public int getArcWidth() {
        return arcWidth;
    }

    public int getArcHeight() {
        return arcHeight;
    }

    public int getArc() {
        return arc;
    }

    public float getLineWidth() {
        return lineWidth;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public void setZIndex(int zIndex) {
        this.zIndex = zIndex;
    }

}
