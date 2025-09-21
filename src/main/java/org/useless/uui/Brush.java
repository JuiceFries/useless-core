package org.useless.uui;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.opengl.GL11;
import org.useless.uui.data.Location;
import org.useless.uui.data.Size;
import org.useless.uui.font.FontStyle;
import org.useless.uui.image.GLImage;
import org.useless.uui.image.NVGImage;
import org.useless.uui.uir.FullName;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * <h1>画笔</h1>
 * 画笔类，用于在图形界面上绘制各种图形和文本。<br>
 * 用于实现{@link Drawing Drawing} 所提供的绘图API，<br>
 * 同时作为隐藏实现
 * @since 0.0.1
 * @see Drawing
 * @see Window
 */
class Brush implements Drawing {
    private volatile long handle = 0;
    private Size size;
    private Color color = Color.BLUE;
    private java.awt.Color awtColor = java.awt.Color.WHITE;
    private Font font;
    private java.awt.Font awtFont;
    private Location mouseLocation;
    private long nvgContext;

    /**
     * 没啥用处
     */
    private final static long FUTILE = 1145141919L;

    /**
     * 创建一个默认的画笔实例。
     */
    public Brush() {
        this(FUTILE);
    }

    /**
     * 没有任何用处
     * @param BRUSH 没啥用
     */
    Brush(long BRUSH) {
        if (BRUSH != 1145141919L) {
            System.out.println("值 = " + BRUSH);
        }
    }

    // 坐标转换辅助方法
    private float toGlX(float x) {
        return (x * 2.0f / size.width) - 1.0f;
    }

    private float toGlY(float y) {
        return 1.0f - (y * 2.0f / size.height);
    }

    // set ====================

    @Override
    public void setColor(Color color) {
        // 在setColor时预乘
        this.color = new Color(
                color.getR() * color.getA(),
                color.getG() * color.getA(),
                color.getB() * color.getA(),
                color.getA()
        );
    }

    @Deprecated
    @Override
    public void setAwtColor(java.awt.Color awtColor) {
        this.awtColor = awtColor;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    @Deprecated(since = "0.0.2")
    public void setAWTFont(java.awt.Font font) {
        this.awtFont = font;
    }

    @Override
    public void setLineWidth(float width) {
        GL11.glLineWidth(width);
    }

    @Override
    public void setWireFrame(boolean enabled) {
        if (enabled) {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
        } else {
            GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
        }
    }

    // get ====================

    @Override
    public Color getColor() {
        return color;
    }

    @Deprecated
    @Override
    public java.awt.Color getAwtColor() {
        return awtColor;
    }

    @Override
    public Font getFont() {
        return font;
    }

    @Deprecated
    @Override
    public java.awt.Font getAwtFont() {
        return awtFont;
    }

    @Override
    public Location getMouseLocation() {
        return mouseLocation;
    }

    // stm ====================

    @Override
    public void drawRectangle(float x, float y, float width, float height) {
        if (handle == 0) throw new RuntimeException("窗口没初始化玩你妈");

        // 设置颜色
        GL11.glColor4f(color.getR(), color.getG(), color.getB(),color.getA());
        // 直接画矩形
        GL11.glRectf(toGlX(x), toGlY(y), toGlX(x + width), toGlY(y + height));
    }

    @Override
    public void drawRoundedRectangle(float x, float y, float width, float height, float w, float h, float arc) {
        if (nvgContext == 0) throw new RuntimeException("NanoVG not initialized");

        nvgBeginPath(nvgContext);
        nvgRoundedRectVarying(nvgContext, x, y, width, height,
                Math.min(w, width/2), Math.min(h, height/2),
                Math.min(w, width/2), Math.min(h, height/2));

        NVGColor nvgColor = NVGColor.create();
        nvgColor.r(color.getR());
        nvgColor.g(color.getG());
        nvgColor.b(color.getB());
        nvgColor.a(color.getA());
        nvgFillColor(nvgContext, nvgColor);
        nvgFill(nvgContext);
    }

    @FullName(fullName = "drawRoundedRectangleStroke")
    @Override
    public void drawRRS(float x, float y, float w, float h,
                                           float arcW, float arcH, float lineWidth, Color color) {
        nvgBeginPath(nvgContext);
        nvgRoundedRectVarying(nvgContext, x, y, w, h, arcW, arcH, arcW, arcH);

        NVGColor strokeColor = NVGColor.create();
        strokeColor.r(color.getR()); strokeColor.g(color.getG());
        strokeColor.b(color.getB()); strokeColor.a(color.getA());
        nvgStrokeColor(nvgContext, strokeColor);
        nvgStrokeWidth(nvgContext, lineWidth);
        nvgStroke(nvgContext);
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        if (handle == 0) throw new RuntimeException("没初始化画个毛线");

        GL11.glColor4f(color.getR(), color.getG(), color.getB(),color.getA());
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(toGlX(x1), toGlY(y1));
        GL11.glVertex2f(toGlX(x2), toGlY(y2));
        GL11.glEnd();
    }

    @Override
    public void drawCircle(float x, float y, float radius) {
        drawCircle(x, y, radius, 32);
    }

    @Override
    public void drawCircle(float x, float y, float radius, int segments) {
        if (handle == 0) throw new RuntimeException("窗口都没初始化画个锤子圆");

        GL11.glColor4f(color.getR(), color.getG(), color.getB(),color.getA());
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (int i = 0; i < segments; i++) {
            float angle = (float) (2.0 * Math.PI * i / segments);
            float dx = (float) (radius * Math.cos(angle));
            float dy = (float) (radius * Math.sin(angle));
            GL11.glVertex2f(toGlX(x + dx), toGlY(y + dy));
        }
        GL11.glEnd();
    }

    @Override
    public void drawEllipse(float x, float y, float w, float h) {
        if (handle == 0) throw new RuntimeException("没初始化画个蛋椭圆");

        GL11.glColor4f(color.getR(), color.getG(), color.getB(),color.getA());
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (int i = 0; i < 32; i++) {
            float angle = (float) (2.0 * Math.PI * i / 32);
            float dx = (float) (w * Math.cos(angle));
            float dy = (float) (h * Math.sin(angle));
            GL11.glVertex2f(toGlX(x + dx), toGlY(y + dy));
        }
        GL11.glEnd();
    }

    @Override
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        if (handle == 0) throw new RuntimeException("窗口都没初始化画个蛋三角形");

        GL11.glColor4f(color.getR(), color.getG(), color.getB(),color.getA());
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2f(toGlX(x1), toGlY(y1));
        GL11.glVertex2f(toGlX(x2), toGlY(y2));
        GL11.glVertex2f(toGlX(x3), toGlY(y3));
        GL11.glEnd();
    }

    @Override
    public void drawPolygon(float[] xPoints, float[] yPoints) {
        if (handle == 0) throw new RuntimeException("没初始化画个毛多边形");
        if (xPoints.length < 3) throw new IllegalArgumentException("点太少了，画个鸡毛");

        GL11.glColor4f(color.getR(), color.getG(), color.getB(),color.getA());
        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i < xPoints.length; i++) {
            GL11.glVertex2f(toGlX(xPoints[i]), toGlY(yPoints[i]));
        }
        GL11.glEnd();
    }

    @Override
    public void drawPolyLine(float[] xPoints, float[] yPoints) {
        if (handle == 0) throw new RuntimeException("没初始化画个毛折线");
        if (xPoints.length < 2) throw new IllegalArgumentException("点太少了，连毛都画不出来");

        GL11.glColor4f(color.getR(), color.getG(), color.getB(),color.getA());
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (int i = 0; i < xPoints.length; i++) {
            GL11.glVertex2f(toGlX(xPoints[i]), toGlY(yPoints[i]));
        }
        GL11.glEnd();
    }

    @Override
    public void drawText(String text, float x, float y) {
        if (nvgContext == 0) throw new RuntimeException("nanovg未初始化");
        if (font == null) throw new RuntimeException("没设字体画个锤子文字");

        // 根据样式选择对应的字体别名
        String fontAlias = getFontAliasWithStyle(font);

        nvgFontSize(nvgContext, font.getSize());
        nvgFontFaceId(nvgContext, Font.getNVGFontHandle(fontAlias)); // 通过别名获取句柄
        nvgTextAlign(nvgContext, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);

        // 设置颜色
        NVGColor nvgColor = NVGColor.create();
        nvgColor.r(this.color.getR());
        nvgColor.g(this.color.getG());
        nvgColor.b(this.color.getB());
        nvgColor.a(this.color.getA());
        nvgFillColor(nvgContext, nvgColor);

        // 绘制文本
        nvgText(nvgContext, x, y, text);

        // 手动绘制下划线和删除线
        if (font.getStyle() == FontStyle.UNDERLINE || font.getStyle() == FontStyle.STRIKE_THROUGH) {
            drawTextDecorations(text, x, y);
        }
    }

    @Deprecated
    @Override
    public void drawAWTText(String text, int x, int y) {
        if (awtFont == null) throw new RuntimeException("没设字体画个锤子文字");

        // 用awt生成位图
        java.awt.Font aFont = new java.awt.Font(awtFont.getName(), awtFont.getStyle(), awtFont.getSize());
        BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setFont(aFont);
        FontMetrics metrics = g.getFontMetrics();
        int width = metrics.stringWidth(text);
        int height = metrics.getHeight();

        // 创建实际纹理
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g = image.createGraphics();
        g.setColor(new java.awt.Color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue(),awtColor.getAlpha()));
        g.setFont(aFont);
        g.drawString(text, 0, metrics.getAscent());
        g.dispose();

        // 上传纹理到GPU
        int textureId = uploadTexture(image);

        // 画纹理四边形
        drawTexture(textureId, x, y, width, height);
        GL11.glDeleteTextures(textureId); // 记得清临时纹理
    }

    private int uploadTexture(BufferedImage image) {
        int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), null, 0, image.getWidth());
        ByteBuffer buffer = ByteBuffer.allocateDirect(pixels.length * 4);

        for (int pixel : pixels) {
            buffer.put((byte) ((pixel >> 16) & 0xFF)); // R
            buffer.put((byte) ((pixel >> 8) & 0xFF));  // G
            buffer.put((byte) (pixel & 0xFF));         // B
            buffer.put((byte) ((pixel >> 24) & 0xFF)); // A
        }
        buffer.flip();

        int textureId = GL11.glGenTextures();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, image.getWidth(), image.getHeight(),
                0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        return textureId;
    }

    private void drawTexture(int textureId, float x, float y, float width, float height) {
        float glX1 = (x * 2.0f / size.width) - 1.0f;
        float glY1 = 1.0f - (y * 2.0f / size.height);
        float glX2 = ((x + width) * 2.0f / size.width) - 1.0f;
        float glY2 = 1.0f - ((y + height) * 2.0f / size.height);

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0); GL11.glVertex2f(glX1, glY1);
        GL11.glTexCoord2f(1, 0); GL11.glVertex2f(glX2, glY1);
        GL11.glTexCoord2f(1, 1); GL11.glVertex2f(glX2, glY2);
        GL11.glTexCoord2f(0, 1); GL11.glVertex2f(glX1, glY2);
        GL11.glEnd();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public void drawPicture(Picture picture, float x, float y, float width, float height) {
        // 在drawPicture方法中修正NVG图片绘制：
        if (picture instanceof NVGImage) {
            int imageHandle = ((NVGImage) picture).getNVGImageHandle();
            if (imageHandle != -1) {
                nvgBeginPath(nvgContext);
                nvgRect(nvgContext, x, y, width, height);
                // 正确的nvgImagePattern参数：x, y, width, height, angle, imageHandle, alpha
                nvgFillPaint(nvgContext, nvgImagePattern(nvgContext, x, y, width, height,
                        0, imageHandle, 1.0f, NVGPaint.create()));
                nvgFill(nvgContext);
            }
        } else if (picture instanceof GLImage) {
            // OpenGL纹理渲染
            long textureId = picture.getTextureId();
            if (textureId != -1) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int) textureId);
                GL11.glColor4f(1, 1, 1, 1);

                float glX1 = toGlX(x);
                float glY1 = toGlY(y);
                float glX2 = toGlX(x + width);
                float glY2 = toGlY(y + height);

                GL11.glBegin(GL11.GL_QUADS);
                GL11.glTexCoord2f(0, 0); GL11.glVertex2f(glX1, glY1);
                GL11.glTexCoord2f(1, 0); GL11.glVertex2f(glX2, glY1);
                GL11.glTexCoord2f(1, 1); GL11.glVertex2f(glX2, glY2);
                GL11.glTexCoord2f(0, 1); GL11.glVertex2f(glX1, glY2);
                GL11.glEnd();
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
        }
    }

    private static boolean showVariantWarnings = false;

    // 根据样式生成字体别名
    private String getFontAliasWithStyle(Font font) {
        String baseName = font.getName();
        String variantName = baseName + "_" + font.getStyle().name();

        if (!Font.isFontLoaded(variantName)) {
            if (showVariantWarnings) {
                System.err.println("警告: 字体变体 " + variantName + " 未加载，使用基础字体");
            }
            return baseName;
        }
        return variantName;
    }

    // 添加静态方法控制警告显示
    @Override
    public void setShowVariantWarnings(boolean show) {
        showVariantWarnings = show;
    }

    // 绘制文本装饰线（下划线/删除线）
    private void drawTextDecorations(String text, float x, float y) {
        float[] bounds = new float[4];
        nvgTextBounds(nvgContext, x, y, text, bounds);

        float textWidth = bounds[2] - bounds[0];
        float textHeight = bounds[3] - bounds[1];

        if (font.getStyle() == FontStyle.UNDERLINE) {
            drawLine(x, y + textHeight + 2, x + textWidth, y + textHeight + 2);
        }

        if (font.getStyle() == FontStyle.STRIKE_THROUGH) {
            drawLine(x, y + textHeight / 2, x + textWidth, y + textHeight / 2);
        }
    }

    @Override
    public long getNanoVGContext() {
        return nvgContext;
    }

    @Override
    public void pushState() {
        GL11.glPushMatrix();
    }

    @Override
    public void popState() {
        GL11.glPopMatrix();
    }

    @Override
    public void translate(float x, float y) {
        float glX = (x * 2.0f / size.width);
        float glY = (y * 2.0f / size.height);
        GL11.glTranslatef(glX, -glY, 0);
    }

    @Override
    public void rotate(float degrees) {
        GL11.glRotatef(degrees, 0, 0, 1);
    }

    @Override
    public void scale(float sx, float sy) {
        GL11.glScalef(sx, sy, 1);
    }

    @Override
    public void deliverHandle(long handle, Size size, Location mouseLocation, long nvgContext) {
        this.handle = handle;
        this.size = size;
        this.mouseLocation = mouseLocation;
        this.nvgContext = nvgContext;
    }
}