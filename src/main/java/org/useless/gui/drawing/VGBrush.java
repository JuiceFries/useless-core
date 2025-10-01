package org.useless.gui.drawing;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.nanovg.NVGColor;
import org.useless.gui.data.Color;
import org.useless.gui.font.FontRegistry.FontResource;
import org.useless.gui.template.Template;
import org.useless.gui.event.EventManager;
import org.useless.gui.font.Font;
import org.useless.gui.font.FontRegistry;
import org.useless.gui.font.Style;
import org.useless.gui.picture.Picture;
import org.useless.gui.uir.Handle;

import static java.lang.Math.max;
import static java.lang.Math.toRadians;
import static java.lang.System.err;
import static java.lang.System.out;
import static org.lwjgl.nanovg.NVGColor.create;
import static org.lwjgl.nanovg.NanoVG.nvgBeginPath;
import static org.lwjgl.nanovg.NanoVG.nvgCircle;
import static org.lwjgl.nanovg.NanoVG.nvgClosePath;
import static org.lwjgl.nanovg.NanoVG.nvgCreateFont;
import static org.lwjgl.nanovg.NanoVG.nvgCreateImageRGBA;
import static org.lwjgl.nanovg.NanoVG.nvgDeleteImage;
import static org.lwjgl.nanovg.NanoVG.nvgEllipse;
import static org.lwjgl.nanovg.NanoVG.nvgFill;
import static org.lwjgl.nanovg.NanoVG.nvgFillColor;
import static org.lwjgl.nanovg.NanoVG.nvgFillPaint;
import static org.lwjgl.nanovg.NanoVG.nvgFontFaceId;
import static org.lwjgl.nanovg.NanoVG.nvgFontSize;
import static org.lwjgl.nanovg.NanoVG.nvgImagePattern;
import static org.lwjgl.nanovg.NanoVG.nvgIntersectScissor;
import static org.lwjgl.nanovg.NanoVG.nvgLineTo;
import static org.lwjgl.nanovg.NanoVG.nvgMoveTo;
import static org.lwjgl.nanovg.NanoVG.nvgRect;
import static org.lwjgl.nanovg.NanoVG.nvgRestore;
import static org.lwjgl.nanovg.NanoVG.nvgRotate;
import static org.lwjgl.nanovg.NanoVG.nvgRoundedRect;
import static org.lwjgl.nanovg.NanoVG.nvgSave;
import static org.lwjgl.nanovg.NanoVG.nvgScale;
import static org.lwjgl.nanovg.NanoVG.nvgStroke;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeColor;
import static org.lwjgl.nanovg.NanoVG.nvgStrokeWidth;
import static org.lwjgl.nanovg.NanoVG.nvgText;
import static org.lwjgl.nanovg.NanoVG.nvgTextBounds;
import static org.lwjgl.nanovg.NanoVG.nvgTextLetterSpacing;
import static org.lwjgl.nanovg.NanoVG.nvgTranslate;
import static org.useless.gui.data.Color.WHITE;
import static org.useless.gui.font.Font.DEFAULT;
import static org.useless.gui.font.Style.PLAIN;

class VGBrush implements Drawing{

    private long vgHandle = 0;
    private Handle handle;
    private Color color = WHITE;
    private float lineWidth = 1.0f;
    private boolean wireFrame = false;
    private Font font = new Font(DEFAULT, PLAIN,12);
    private final Map<String, Integer> nvgFontHandles = new ConcurrentHashMap<>();
    private EventManager eventManager;
    private final Map<Picture, Integer> imageCache = new ConcurrentHashMap<>();
    private float charSpacing = 1.0f;

    @Override
    public void deliver(@NotNull Handle handle, EventManager eventManager) {
        this.vgHandle = handle.vgHandle();
        this.eventManager = eventManager;
        this.handle = handle;
    }

    @Override
    public void registerEvent(Template template) {
        if (eventManager != null) {
            eventManager.monitorTemplate(template);
        } else {
            err.println("警告：事件管理器未初始化，无法注册模板: " + template.getClass().getSimpleName());
        }
    }

    @Override
    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public void setLineWidth(float width) {
        this.lineWidth = width;
    }

    @Override
    public void setWireFrame(boolean enabled) {
        this.wireFrame = enabled;
    }

    @Override
    public void setCharSpacing(float spacing) {
        this.charSpacing = max(0.5f, spacing);
    }

    @Override
    public float getCharSpacing() {
        return charSpacing;
    }

    @Override
    public Color getColor() {
        return color;
    }

    public Font getFont() {
        return font;
    }

    @Override
    public void drawRectangle(float x, float y, float width, float height) {
        if (vgHandle == 0) throw new RuntimeException("窗口没初始化玩你妈");

        nvgBeginPath(vgHandle);
        nvgRect(vgHandle, x, y, width, height);

        NVGColor fillColor = create();
        fillColor.r(color.getR());
        fillColor.g(color.getG());
        fillColor.b(color.getB());
        fillColor.a(color.getA());
        nvgFillColor(vgHandle, fillColor);
        nvgFill(vgHandle);
    }

    @Override
    public void drawRoundedRectangle(float x, float y, float width, float height, float w, float h) {
        nvgBeginPath(vgHandle);
        nvgRoundedRect(vgHandle, x, y, width, height, Math.min(w, h));

        NVGColor nvgColor = create();
        nvgColor.r(color.getR()); nvgColor.g(color.getG());
        nvgColor.b(color.getB()); nvgColor.a(color.getA());

        if (wireFrame) {
            nvgStrokeColor(vgHandle, nvgColor);
            nvgStrokeWidth(vgHandle, lineWidth);
            nvgStroke(vgHandle);
        } else {
            nvgFillColor(vgHandle, nvgColor);
            nvgFill(vgHandle);
        }
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        nvgBeginPath(vgHandle);
        nvgMoveTo(vgHandle, x1, y1);
        nvgLineTo(vgHandle, x2, y2);

        NVGColor nvgColor = create();
        nvgColor.r(color.getR()); nvgColor.g(color.getG());
        nvgColor.b(color.getB()); nvgColor.a(color.getA());
        nvgStrokeColor(vgHandle, nvgColor);
        nvgStrokeWidth(vgHandle, lineWidth);
        nvgStroke(vgHandle);
    }

    @Override
    public void drawCircle(float x, float y, float radius) {
        drawCircle(x, y, radius, 32);
    }

    @Override
    public void drawCircle(float x, float y, float radius, int segments) {
        nvgBeginPath(vgHandle);
        nvgCircle(vgHandle, x, y, radius);

        NVGColor nvgColor = create();
        nvgColor.r(color.getR()); nvgColor.g(color.getG());
        nvgColor.b(color.getB()); nvgColor.a(color.getA());

        if (wireFrame) {
            nvgStrokeColor(vgHandle, nvgColor);
            nvgStrokeWidth(vgHandle, lineWidth);
            nvgStroke(vgHandle);
        } else {
            nvgFillColor(vgHandle, nvgColor);
            nvgFill(vgHandle);
        }
    }

    @Override
    public void drawEllipse(float x, float y, float w, float h) {
        nvgBeginPath(vgHandle);
        nvgEllipse(vgHandle, x + w/2, y + h/2, w/2, h/2);

        NVGColor nvgColor = create();
        nvgColor.r(color.getR()); nvgColor.g(color.getG());
        nvgColor.b(color.getB()); nvgColor.a(color.getA());

        if (wireFrame) {
            nvgStrokeColor(vgHandle, nvgColor);
            nvgStrokeWidth(vgHandle, lineWidth);
            nvgStroke(vgHandle);
        } else {
            nvgFillColor(vgHandle, nvgColor);
            nvgFill(vgHandle);
        }
    }

    @Override
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        nvgBeginPath(vgHandle);
        nvgMoveTo(vgHandle, x1, y1);
        nvgLineTo(vgHandle, x2, y2);
        nvgLineTo(vgHandle, x3, y3);
        nvgClosePath(vgHandle);

        NVGColor nvgColor = create();
        nvgColor.r(color.getR()); nvgColor.g(color.getG());
        nvgColor.b(color.getB()); nvgColor.a(color.getA());

        if (wireFrame) {
            nvgStrokeColor(vgHandle, nvgColor);
            nvgStrokeWidth(vgHandle, lineWidth);
            nvgStroke(vgHandle);
        } else {
            nvgFillColor(vgHandle, nvgColor);
            nvgFill(vgHandle);
        }
    }

    @Override
    public void drawPolygon(float @NotNull [] xPoints, float @NotNull [] yPoints) {
        nvgBeginPath(vgHandle);
        nvgMoveTo(vgHandle, xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            nvgLineTo(vgHandle, xPoints[i], yPoints[i]);
        }
        nvgClosePath(vgHandle);

        NVGColor nvgColor = create();
        nvgColor.r(color.getR()); nvgColor.g(color.getG());
        nvgColor.b(color.getB()); nvgColor.a(color.getA());

        if (wireFrame) {
            nvgStrokeColor(vgHandle, nvgColor);
            nvgStrokeWidth(vgHandle, lineWidth);
            nvgStroke(vgHandle);
        } else {
            nvgFillColor(vgHandle, nvgColor);
            nvgFill(vgHandle);
        }
    }

    @Override
    public void drawPolyLine(float @NotNull [] xPoints, float @NotNull [] yPoints) {
        nvgBeginPath(vgHandle);
        nvgMoveTo(vgHandle, xPoints[0], yPoints[0]);
        for (int i = 1; i < xPoints.length; i++) {
            nvgLineTo(vgHandle, xPoints[i], yPoints[i]);
        }

        NVGColor nvgColor = create();
        nvgColor.r(color.getR()); nvgColor.g(color.getG());
        nvgColor.b(color.getB()); nvgColor.a(color.getA());
        nvgStrokeColor(vgHandle, nvgColor);
        nvgStrokeWidth(vgHandle, lineWidth);
        nvgStroke(vgHandle);
    }

    @Override
    public void drawText(String text, float x, float y) {
        if (vgHandle == 0) throw new RuntimeException("NVG上下文未初始化");

        FontResource fontRes = FontRegistry.getFont(font.getAvailableName());
        if (fontRes == null) return;

        int fontHandle = getOrCreateNVGFont(fontRes);
        if (fontHandle == 0) {
            fontRes = FontRegistry.getFont(DEFAULT);
            if (fontRes == null) return;
            fontHandle = getOrCreateNVGFont(fontRes);
        }

        nvgFontFaceId(vgHandle, fontHandle);
        nvgFontSize(vgHandle, font.getSize());

        nvgTextLetterSpacing(vgHandle, (charSpacing - 1.0f) * font.getSize() * 0.1f);

        NVGColor textColor = create();
        textColor.r(color.getR());
        textColor.g(color.getG());
        textColor.b(color.getB());
        textColor.a(color.getA());
        nvgFillColor(vgHandle, textColor);

        nvgText(vgHandle, x, y, text);

        nvgTextLetterSpacing(vgHandle, 0);
    }

    @Override
    public void registerFont(String alias, String filePath) {
        if (!FontRegistry.registerFont(alias, filePath)) {
            err.println("VGBrush字体注册失败: " + alias);
            return;
        }

        FontResource fontRes = FontRegistry.getFont(alias);
        if (fontRes != null) {
            getOrCreateNVGFont(fontRes);
            out.println("VGBrush字体注册成功: " + alias);
        }
    }

    @Override
    public void registerFontFromResource(String alias, String resourcePath) {
        if (!FontRegistry.registerFontFromResource(alias, resourcePath)) {
            err.println("VGBrush资源字体注册失败: " + alias);
            return;
        }

        FontResource fontRes = FontRegistry.getFont(alias);
        if (fontRes != null) {
            getOrCreateNVGFont(fontRes);
            out.println("VGBrush资源字体注册成功: " + alias);
        }
    }

    private int getOrCreateNVGFont(FontResource fontRes) {
        return nvgFontHandles.computeIfAbsent(fontRes.getName(), alias -> {
            // 直接使用FontRegistry提供的字体文件路径
            if (fontRes.getFontFile() == null || !fontRes.getFontFile().exists()) {
                err.println("字体文件不存在: " + alias);
                return -1;
            }

            int fontHandle = nvgCreateFont(vgHandle, fontRes.getName(), fontRes.getFontFile().getAbsolutePath());
            if (fontHandle == -1) {
                err.println("NanoVG字体创建失败: " + alias);
                return -1;
            }
            return fontHandle;
        });
    }

    @Override
    public void drawPicture(Picture picture, float x, float y) {
        if (vgHandle == 0 || picture == null || !picture.isLoaded()) return;

        int imageHandle = getOrCreateNVGImage(picture);
        if (imageHandle == 0) return;

        nvgSave(vgHandle);
        nvgTranslate(vgHandle, x, y);
        nvgRotate(vgHandle, (float) toRadians(picture.getRotation()));
        nvgScale(vgHandle, picture.getScaleX(), picture.getScaleY());

        nvgBeginPath(vgHandle);
        nvgRect(vgHandle, 0, 0, picture.getImageWidth(), picture.getImageHeight());

        org.lwjgl.nanovg.NVGPaint paint = org.lwjgl.nanovg.NVGPaint.create();
        nvgImagePattern(vgHandle, 0, 0, picture.getImageWidth(), picture.getImageHeight(), 0, imageHandle, picture.getOpacity(), paint);

        nvgFillPaint(vgHandle, paint);
        nvgFill(vgHandle);
        nvgRestore(vgHandle);
    }

    @Override
    public void drawPicture(Picture picture, float x, float y, float width, float height) {
        if (vgHandle == 0 || picture == null || !picture.isLoaded()) return;

        int imageHandle = getOrCreateNVGImage(picture);
        if (imageHandle == 0) return;

        nvgSave(vgHandle);
        nvgTranslate(vgHandle, x, y);
        nvgRotate(vgHandle, (float) toRadians(picture.getRotation()));

        nvgBeginPath(vgHandle);
        nvgRect(vgHandle, 0, 0, width, height);

        org.lwjgl.nanovg.NVGPaint paint = org.lwjgl.nanovg.NVGPaint.create();
        nvgImagePattern(vgHandle, 0, 0, width, height, 0, imageHandle, picture.getOpacity(), paint);

        nvgFillPaint(vgHandle, paint);
        nvgFill(vgHandle);
        nvgRestore(vgHandle);
    }

    private int getOrCreateNVGImage(Picture picture) {
        Integer cachedImage = imageCache.get(picture);
        if (cachedImage != null && cachedImage != 0) {
            return cachedImage;
        }

        Object nativeHandle = picture.getNativeHandle();
        if (nativeHandle instanceof Integer) {
            int imageId = (Integer) nativeHandle;
            imageCache.put(picture, imageId);
            return imageId;
        }

        try {
            ByteBuffer imageData = picture.getImageData();
            int width = picture.getImageWidth();
            int height = picture.getImageHeight();

            if (imageData == null || width <= 0 || height <= 0) {
                err.println("无效的图片数据: " + picture.getPath());
                return 0;
            }

            int imageId = nvgCreateImageRGBA(
                    vgHandle, width, height, 0, imageData
            );

            if (imageId == 0) {
                err.println("NanoVG图片创建失败: " + picture.getPath());
                return 0;
            }

            imageCache.put(picture, imageId);
            picture.setNativeHandle(imageId);
            return imageId;

        } catch (Exception e) {
            err.println("创建NanoVG图片失败: " + picture.getPath() + " - " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void cleanup() {
        for (Integer imageId : imageCache.values()) {
            if (imageId != 0) {
                nvgDeleteImage(vgHandle, imageId);
            }
        }
        imageCache.clear();
        nvgFontHandles.clear();
    }

    @Override
    public float measureText(String text, Font font) {
        if (vgHandle == 0 || text == null || text.isEmpty()) return 0;

        try {
            FontResource fontRes = FontRegistry.getFont(font.getAvailableName());
            if (fontRes == null) return 0;

            int fontHandle = getOrCreateNVGFont(fontRes);
            if (fontHandle <= 0) return 0;

            nvgFontFaceId(vgHandle, fontHandle);
            nvgFontSize(vgHandle, font.getSize());

            nvgTextLetterSpacing(vgHandle, (charSpacing - 1.0f) * font.getSize() * 0.1f);

            FloatBuffer bounds = FloatBuffer.allocate(4);
            nvgTextBounds(vgHandle, 0, 0, text, bounds);

            nvgTextLetterSpacing(vgHandle, 0);

            return max(0, bounds.get(2) - bounds.get(0));
        } catch (Exception e) {
            err.println("测量文本宽度失败: " + e.getMessage());
            return 0;
        }
    }

    @Override
    public void setClip(float x, float y, float width, float height) {
        nvgSave(vgHandle);
        nvgIntersectScissor(vgHandle, x, y, width, height);
    }

    @Override
    public void resetClip() {
        nvgRestore(vgHandle);
    }

    @Override
    public void pushState() {
        nvgSave(vgHandle);
    }

    @Override
    public void popState() {
        nvgRestore(vgHandle);
    }

    @Override
    public void translate(float x, float y) {
        nvgTranslate(vgHandle, x, y);
    }

    @Override
    public void rotate(float degrees) {
        nvgRotate(vgHandle, (float) toRadians(degrees));
    }

    @Override
    public void scale(float sx, float sy) {
        nvgScale(vgHandle, sx, sy);
    }

    @Override
    public Handle getHandle() {
        return handle;
    }
}