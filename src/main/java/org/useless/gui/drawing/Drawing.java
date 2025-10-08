package org.useless.gui.drawing;

import org.useless.gui.template.Template;
import org.useless.gui.event.EventManager;
import org.useless.gui.font.Font;
import org.useless.gui.template.container.Window;
import org.useless.gui.data.Location;
import org.useless.gui.picture.Picture;
import org.useless.gui.data.Handle;
import org.useless.gui.data.Color;
import org.useless.gui.data.Size;

/**
 * 绘图接口，定义了绘制图形和文本的基本方法。
 * <p>
 * 该接口提供了设置颜色、字体等绘图属性的方法，并支持绘制各种基本图形如矩形、圆形、线条等。
 * 同时支持坐标系变换（平移、旋转、缩放）和绘图状态管理。
 * </p>
 * <p>
 * 使用示例：
 * <pre>
 *     Drawing drawing = ...;
 *     drawing.setColor(new Color(255, 0, 0));
 *     drawing.drawRectangle(10, 10, 100, 50);
 * </pre>
 * </p>
 *
 * <br>
 * 注意：文档是由AI生成具体实现自行理解
 * @see Window
 * @see Color
 * @see Location
 * @see Size
 * @since 0.0.4
 */
public interface Drawing {

    /**
     *
     * @param render 渲染实现
     * @return 对应的渲染器
     */
    static Drawing init(DrawImpl render) {
        return render.brush();
    }

    @Deprecated
    default Drawing initRender(DrawImpl draw) {
        return draw.brush();
    }

    /**
     * 传递数据
     *
     * @param handle       句柄
     * @param eventManager
     */
    void deliver(Handle handle, EventManager eventManager);

    // set =====

    /**
     * 给模板用的<br>
     * 懒得写不能乱用！！！！！！！！！！！！
     * @param template 模板
     */
    void registerEvent(Template template);

    /**
     * 设置当前绘图颜色。
     * <p>
     * 颜色设置会影响后续所有绘图操作，直到再次调用此方法改变颜色。
     * </p>
     *
     * @param color 要设置的颜色对象，使用RGB颜色模型
     * @throws NullPointerException 如果color参数为null
     */
    void setColor(Color color);

    /**
     * 设置当前绘图字体。
     * <p>
     * 字体设置会影响后续所有文本绘制操作。
     * </p>
     *
     * @param font 要设置的字体对象
     * @throws NullPointerException 如果font参数为null
     */
    void setFont(Font font);

    /**
     * 设置绘制线条时的宽度。
     * <p>
     * 线宽会影响所有线条绘制操作，包括矩形边框、线条、多边形边框等。
     * </p>
     *
     * @param width 线条宽度，必须大于0
     * @throws IllegalArgumentException 如果width小于等于0
     */
    void setLineWidth(float width);

    /**
     * 切换线框/填充模式。
     * <p>
     * 当启用线框模式时，所绘制的形状将以轮廓形式显示；否则，将以填充形式显示。
     * </p>
     *
     * @param enabled 如果为true，则启用线框模式；如果为false，则使用填充模式
     */
    void setWireFrame(boolean enabled);

    // get =====

    /**
     * @return 当前颜色对象
     */
    Color getColor();

    /**
     * @return 当前字体
     */
    Font getFont();

    // draw ======

    /**
     * 绘制一个矩形。
     * <p>
     * 矩形的绘制方式（线框或填充）取决于当前绘图模式设置。
     * </p>
     *
     * @param x 矩形左上角的X坐标
     * @param y 矩形左上角的Y坐标
     * @param width 矩形的宽度，必须大于0
     * @param height 矩形的高度，必须大于0
     * @throws IllegalArgumentException 如果width或height小于等于0
     */
    void drawRectangle(float x, float y, float width, float height);

    /**
     * 绘制一个圆角矩形。
     * <p>
     * 圆角的大小由角宽、角高和弧参数控制。
     * </p>
     *
     * @param x 矩形左上角的X坐标
     * @param y 矩形左上角的Y坐标
     * @param width 矩形的宽度，必须大于0
     * @param height 矩形的高度，必须大于0
     * @param w 圆角的水平半径
     * @param h 圆角的垂直半径
     * @throws IllegalArgumentException 如果width或height小于等于0
     */
    void drawRoundedRectangle(
            float x, float y, float width,
            float height, float w, float h
    );

    /**
     * 绘制一条直线。
     *
     * @param x1 线条起点的X坐标
     * @param y1 线条起点的Y坐标
     * @param x2 线条终点的X坐标
     * @param y2 线条终点的Y坐标
     */
    void drawLine(float x1, float y1, float x2, float y2);

    /**
     * 绘制一个圆。
     * <p>
     * 使用默认的段数(通常为32)来近似圆形。
     * </p>
     *
     * @param x 圆心的X坐标
     * @param y 圆心的Y坐标
     * @param radius 圆的半径，必须大于0
     * @throws IllegalArgumentException 如果radius小于等于0
     */
    void drawCircle(float x, float y, float radius);

    /**
     * 绘制一个自定义精度的圆。
     * <p>
     * 通过指定段数可以控制圆的平滑度，段数越多圆越平滑。
     * </p>
     *
     * @param x 圆心的X坐标
     * @param y 圆心的Y坐标
     * @param radius 圆的半径，必须大于0
     * @param segments 用于近似圆的线段数量，必须大于等于3
     * @throws IllegalArgumentException 如果radius小于等于0或segments小于3
     */
    void drawCircle(float x, float y, float radius, int segments);

    /**
     * 绘制一个椭圆。
     *
     * @param x 椭圆中心的X坐标
     * @param y 椭圆中心的Y坐标
     * @param w 椭圆的水平直径
     * @param h 椭圆的垂直直径
     * @throws IllegalArgumentException 如果w或h小于等于0
     */
    void drawEllipse(float x, float y, float w, float h);

    /**
     * 绘制一个三角形。
     *
     * @param x1 第一个顶点的X坐标
     * @param y1 第一个顶点的Y坐标
     * @param x2 第二个顶点的X坐标
     * @param y2 第二个顶点的Y坐标
     * @param x3 第三个顶点的X坐标
     * @param y3 第三个顶点的Y坐标
     */
    void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3);

    /**
     * 绘制一个多边形（自动闭合）。
     * <p>
     * 多边形会自动连接第一个点和最后一个点形成闭合形状。
     * </p>
     *
     * @param xPoints 多边形各顶点的X坐标数组
     * @param yPoints 多边形各顶点的Y坐标数组
     * @throws IllegalArgumentException 如果xPoints或yPoints为null，或长度小于3，或长度不匹配
     */
    void drawPolygon(float[] xPoints, float[] yPoints);

    /**
     * 绘制一条折线（不闭合）。
     * <p>
     * 折线不会自动闭合，仅连接给定的各个点。
     * </p>
     *
     * @param xPoints 折线各点的X坐标数组
     * @param yPoints 折线各点的Y坐标数组
     * @throws IllegalArgumentException 如果xPoints或yPoints为null，或长度小于2，或长度不匹配
     */
    void drawPolyLine(float[] xPoints, float[] yPoints);

    /**
     * 绘制文本。
     * <p>
     * 文本的字体和颜色由当前设置决定。
     * </p>
     *
     * @param text 要绘制的文本字符串
     * @param x 文本基线的起始X坐标
     * @param y 文本基线的最底Y坐标
     * @throws NullPointerException 如果text参数为null
     */
    void drawText(String text, float x, float y);

    /**
     * 设置字符间距
     * @param spacing 字符间距倍数，1.0为默认间距
     */
    void setCharSpacing(float spacing);

    /**
     * 获取当前字符间距
     * @return 字符间距倍数
     */
    float getCharSpacing();


    void drawPicture(Picture picture,float x,float y,float width,float height);

    void drawPicture(Picture picture,float x,float y);

    // 添加字体注册方法
    void registerFont(String alias, String filePath);

    void registerFontFromResource(String alias, String resourcePath);

    // 清理资源
    void cleanup();

    /**
     * 测量文本宽度
     * @param text 要测量的文本
     * @param font 使用的字体
     * @return 文本像素宽度
     */
    float measureText(String text, Font font);

    /**
     * 测量文本宽度（指定字符间距）
     */
    float measureText(String text, Font font, float spacing);

    /**
     * 设置裁剪区域
     * @param x 区域x
     * @param y 区域y
     * @param width 区域宽度
     * @param height 区域高度
     */
    void setClip(float x, float y, float width, float height);

    /**
     * 重置裁剪区域
     */
    void resetClip();

    /**
     * 保存当前绘图状态。
     * <p>
     * 保存当前的颜色、字体、线宽、坐标系变换等状态到状态栈中。
     * 可以通过{@link #popState()}恢复。
     * </p>
     */
    void pushState();

    /**
     * 恢复上次保存的绘图状态。
     * <p>
     * 从状态栈中恢复最近保存的绘图状态。
     * </p>
     *
     * @throws IllegalStateException 如果状态栈为空
     */
    void popState();

    /**
     * 平移坐标系。
     * <p>
     * 后续所有绘图操作都将基于平移后的坐标系。
     * </p>
     *
     * @param x 水平平移量
     * @param y 垂直平移量
     */
    void translate(float x, float y);

    /**
     * 旋转坐标系。
     * <p>
     * 以当前坐标系原点为中心旋转坐标系。
     * </p>
     *
     * @param degrees 旋转角度，单位为度
     */
    void rotate(float degrees);

    /**
     * 缩放坐标系。
     * <p>
     * 后续所有绘图操作都将基于缩放后的坐标系。
     * </p>
     *
     * @param sx 水平缩放因子
     * @param sy 垂直缩放因子
     * @throws IllegalArgumentException 如果sx或sy为0
     */
    void scale(float sx, float sy);

    /**
     * @return 用于给事件管理器的
     */
    Handle getHandle();



}
