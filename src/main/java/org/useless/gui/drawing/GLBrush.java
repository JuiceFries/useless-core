package org.useless.gui.drawing;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBTTFontinfo;
import org.lwjgl.stb.STBTruetype;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.useless.gui.data.Color;
import org.useless.gui.template.Template;
import org.useless.gui.event.EventManager;
import org.useless.gui.font.Font;
import org.useless.gui.font.FontRegistry;
import org.useless.gui.font.Style;
import org.useless.gui.picture.Picture;
import org.useless.gui.uir.annotation.FullName;
import org.useless.gui.uir.Handle;

/**
 * 妈的我的代码太烂了这个模块禁用了！
 * @deprecated 代码太烂了
 */
@FullName(fullName = "OpenGLBrush")
@Deprecated(since = "0.0.3",forRemoval = true)
abstract class GLBrush implements Drawing {

    private long glHandle = 0;
    private Color color = Color.WHITE;
    private Font font = new Font(Font.DEFAULT, Style.PLAIN,12);
    private Handle handle;
    private EventManager eventManager;
    private final Map<Picture, Integer> textureCache = new ConcurrentHashMap<>();
    private float charSpacing = 1.0f;

    // STB字体相关
    private final Map<String, STBTTFontinfo> fontInfos = new ConcurrentHashMap<>();
    private final Map<String, ByteBuffer> fontBuffers = new ConcurrentHashMap<>();
    private final Map<String, Integer> fontTextures = new ConcurrentHashMap<>();

    // 移除原有的字体管理，直接使用FontRegistry
    private final Map<CharCacheKey, Integer> charTextureCache = new ConcurrentHashMap<>();

    // 修改缓存键，直接使用字体资源标识
    private record CharCacheKey(char ch, String fontAlias, int size) {}

    // stm =====
    @Override
    public void deliver(@NotNull Handle handle, EventManager eventManager) {
        this.glHandle = handle.glHandle();
        this.handle = handle;
        if (eventManager!= null)this.eventManager = eventManager;
        initDefaultFont();
    }

    @Override
    public void registerEvent(Template template) {
        if (eventManager != null) {
            eventManager.monitorTemplate(template);
        } else {
            System.err.println("警告：事件管理器未初始化，无法注册模板: " + template.getClass().getSimpleName());
        }
    }

    private void initDefaultFont() {
        // 加载默认字体
        loadFont(Font.DEFAULT, "/font/SourceHanSansSC-Regular.otf");
    }

    private void loadFont(String alias, String resourcePath) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // 从资源加载字体
            InputStream is = getClass().getResourceAsStream(resourcePath);
            if (is == null) {
                System.err.println("字体资源不存在: " + resourcePath);
                return;
            }

            byte[] fontData = is.readAllBytes();
            ByteBuffer fontBuffer = MemoryUtil.memAlloc(fontData.length);
            fontBuffer.put(fontData).flip();

            STBTTFontinfo fontInfo = STBTTFontinfo.create();
            if (!STBTruetype.stbtt_InitFont(fontInfo, fontBuffer)) {
                System.err.println("STB字体初始化失败: " + alias);
                MemoryUtil.memFree(fontBuffer);
                return;
            }

            fontInfos.put(alias, fontInfo);
            fontBuffers.put(alias, fontBuffer);
            System.out.println("GL字体加载成功: " + alias);
        } catch (Exception e) {
            System.err.println("字体加载失败: " + alias + " - " + e.getMessage());
        }
    }

    // set =====

    @Override
    public void setColor(@NotNull Color color) {
        this.color = color;
        GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());//TODO 这里偷懒了
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
    }

    @Override
    public void setLineWidth(float width) {
        GL11.glLineWidth(width);
    }

    @Override
    public void setWireFrame(boolean enabled) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, enabled ? GL11.GL_LINE : GL11.GL_FILL);
    }

    @Override
    public void setCharSpacing(float spacing) {
        this.charSpacing = Math.max(0.5f, spacing);
    }

    // get ======


    @Override
    public float getCharSpacing() {
        return charSpacing;
    }

    @Override
    public Color getColor() {
        return color;
    }

    @Override
    public Font getFont() {
        return font;
    }

    // draw =====

    @Override
    public void drawRectangle(float x, float y, float width, float height) {
        if (glHandle == 0) throw new RuntimeException("窗口没初始化!可以去玩文本了~");

        GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2f(x, y);                    // 左上
        GL11.glVertex2f(x + width, y);           // 右上
        GL11.glVertex2f(x + width, y + height);  // 右下
        GL11.glVertex2f(x, y + height);          // 左下
        GL11.glEnd();
    }

    boolean o = true;
    @Override
    public void drawRoundedRectangle(float x, float y, float width, float height, float w, float h) {
        // 直接画普通矩形，暂时放弃圆角
        drawRectangle(x, y, width, height);
        if (o) {
            System.out.println("被这东西搞红温了目前用普通矩形渲染");
            o = false;
        }
    }

    @Override
    public void drawLine(float x1, float y1, float x2, float y2) {
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glEnd();
    }

    @Override
    public void drawCircle(float x, float y, float radius) {
        drawCircle(x, y, radius, 32);
    }

    @Override
    public void drawCircle(float x, float y, float radius, int segments) {
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (int i = 0; i < segments; i++) {
            float angle = (float) (2.0 * Math.PI * i / segments);
            GL11.glVertex2f(x + (float)Math.cos(angle) * radius, y + (float)Math.sin(angle) * radius);
        }
        GL11.glEnd();
    }

    @Override
    public void drawEllipse(float x, float y, float w, float h) {
        int segments = 32;
        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        for (int i = 0; i < segments; i++) {
            float angle = (float) (2.0 * Math.PI * i / segments);
            GL11.glVertex2f(x + w/2 + (float)Math.cos(angle) * w/2,
                    y + h/2 + (float)Math.sin(angle) * h/2);
        }
        GL11.glEnd();
    }

    @Override
    public void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3) {
        GL11.glBegin(GL11.GL_TRIANGLES);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x3, y3);
        GL11.glEnd();
    }

    @Override
    public void drawPolygon(float @NotNull [] xPoints, float[] yPoints) {
        GL11.glBegin(GL11.GL_POLYGON);
        for (int i = 0; i < xPoints.length; i++) {
            GL11.glVertex2f(xPoints[i], yPoints[i]);
        }
        GL11.glEnd();
    }

    @Override
    public void drawPolyLine(float @NotNull [] xPoints, float[] yPoints) {
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (int i = 0; i < xPoints.length; i++) {
            GL11.glVertex2f(xPoints[i], yPoints[i]);
        }
        GL11.glEnd();
    }

    @Deprecated(since = "0.0.3",forRemoval = true)
    @Override
    public void drawText(String text, float x, float y) {
        if (glHandle == 0 || text == null || text.isEmpty()) return;

        // 先测试基础矩形能不能显示
        Color oldColor = this.color;
        setColor(Color.RED);
        drawRectangle(x, y, text.length() * 10, 20);
        setColor(oldColor);

        // 如果矩形能显示，再试STB渲染
        FontRegistry.FontResource fontRes = FontRegistry.getFont(font.getAvailableName());
        if (fontRes == null) return;

        // 关键：保存和恢复OpenGL状态
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        GL11.glPushMatrix();

        try {
            renderTextCached(fontRes, text, x, y);
        } catch (Exception e) {
            System.err.println("文本渲染失败: " + e.getMessage());
            // 降级方案：用绿色矩形标记文本位置
            setColor(Color.GREEN);
            drawRectangle(x, y, text.length() * 8, 16);
            setColor(oldColor);
        } finally {
            GL11.glPopMatrix();
            GL11.glPopAttrib();
        }
    }

    private void renderTextCached(FontRegistry.FontResource fontRes, String text, float x, float y) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            STBTTFontinfo fontInfo = getOrCreateFontInfo(fontRes);
            if (fontInfo == null) return;

            int fontSize = font.getSize();
            float scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, fontSize);

            IntBuffer ascent = stack.mallocInt(1);
            STBTruetype.stbtt_GetFontVMetrics(fontInfo, ascent, null, null);
            float baseline = y + ascent.get(0) * scale;

            // 关键：正确的纹理混合设置
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            // 关键：设置纹理环境模式
            GL11.glTexEnvi(GL11.GL_TEXTURE_ENV, GL11.GL_TEXTURE_ENV_MODE, GL11.GL_MODULATE);

            GL11.glColor4f(color.getR(), color.getG(), color.getB(), color.getA());

            float currentX = x;
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);
                CharCacheKey key = new CharCacheKey(ch, fontRes.getName(), fontSize);

                int textureId = charTextureCache.computeIfAbsent(key, k ->
                        createCharTexture(fontInfo, ch, scale)
                );

                if (textureId != 0) {
                    drawCachedChar(textureId, fontInfo, ch, scale, currentX, baseline);
                }

                float charWidth = getCharAdvance(fontInfo, ch, scale);
                if (i < text.length() - 1) {
                    charWidth += STBTruetype.stbtt_GetCodepointKernAdvance(
                            fontInfo, ch, text.charAt(i + 1)
                    ) * scale;
                }
                currentX += charWidth * charSpacing;
            }
        }
    }

    private int createCharTexture(STBTTFontinfo fontInfo, char ch, float scale) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x0 = stack.mallocInt(1), y0 = stack.mallocInt(1);
            IntBuffer x1 = stack.mallocInt(1), y1 = stack.mallocInt(1);

            STBTruetype.stbtt_GetCodepointBitmapBox(fontInfo, ch, scale, scale, x0, y0, x1, y1);
            int width = x1.get(0) - x0.get(0);
            int height = y1.get(0) - y0.get(0);

            if (width <= 0 || height <= 0) return 0;

            ByteBuffer bitmap = MemoryUtil.memAlloc(width * height);
            STBTruetype.stbtt_MakeCodepointBitmap(fontInfo, bitmap, width, height, width, scale, scale, ch);

            int textureId = uploadTexture(bitmap, width, height);
            MemoryUtil.memFree(bitmap);

            return textureId;
        } catch (Exception e) {
            System.err.println("字符纹理创建失败: " + ch + " - " + e.getMessage());
            return 0;
        }
    }

    private void drawCachedChar(int textureId, STBTTFontinfo fontInfo, char ch,
                                float scale, float x, float baseline) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x0 = stack.mallocInt(1), y0 = stack.mallocInt(1);
            IntBuffer x1 = stack.mallocInt(1), y1 = stack.mallocInt(1);

            STBTruetype.stbtt_GetCodepointBitmapBox(fontInfo, ch, scale, scale, x0, y0, x1, y1);
            int width = x1.get(0) - x0.get(0);
            int height = y1.get(0) - y0.get(0);

            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(0, 0); GL11.glVertex2f(x + x0.get(0), baseline + y0.get(0));
            GL11.glTexCoord2f(1, 0); GL11.glVertex2f(x + x0.get(0) + width, baseline + y0.get(0));
            GL11.glTexCoord2f(1, 1); GL11.glVertex2f(x + x0.get(0) + width, baseline + y0.get(0) + height);
            GL11.glTexCoord2f(0, 1); GL11.glVertex2f(x + x0.get(0), baseline + y0.get(0) + height);
            GL11.glEnd();
        }
    }

    private float getCharAdvance(STBTTFontinfo fontInfo, char ch, float scale) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer advance = stack.mallocInt(1);
            STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, ch, advance, null);
            return advance.get(0) * scale;
        }
    }

    private STBTTFontinfo getOrCreateFontInfo(FontRegistry.FontResource fontRes) {
        return fontInfos.computeIfAbsent(fontRes.getName(), alias -> {
            try {
                // 直接从FontRegistry的字节数据创建STB字体
                ByteBuffer fontBuffer = MemoryUtil.memAlloc(fontRes.getFontData().length);
                fontBuffer.put(fontRes.getFontData()).flip();

                STBTTFontinfo info = STBTTFontinfo.create();
                if (STBTruetype.stbtt_InitFont(info, fontBuffer)) {
                    fontBuffers.put(alias, fontBuffer); // 保存buffer用于后续清理
                    return info;
                }
                MemoryUtil.memFree(fontBuffer);
            } catch (Exception e) {
                System.err.println("STB字体初始化失败: " + alias);
            }
            return null;
        });
    }

    private int uploadTexture(ByteBuffer bitmap, int width, int height) {
        IntBuffer textureId = MemoryUtil.memAllocInt(1);
        GL11.glGenTextures(textureId);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId.get(0));

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

        // 关键修复：STB返回的是8位alpha，用GL_ALPHA格式
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_ALPHA, width, height, 0,
                GL11.GL_ALPHA, GL11.GL_UNSIGNED_BYTE, bitmap);

        int result = textureId.get(0);
        MemoryUtil.memFree(textureId);
        return result;
    }

    // 添加字体注册方法
    @Override
    public void registerFont(String alias, String filePath) {
        // 直接委托给FontRegistry，避免重复实现
        if (!FontRegistry.registerFont(alias, filePath)) {
            System.err.println("GLBrush字体注册失败: " + alias);
            return;
        }

        // 同时也要初始化STB字体信息
        FontRegistry.FontResource fontRes = FontRegistry.getFont(alias);
        if (fontRes != null) {
            getOrCreateFontInfo(fontRes); // 这会初始化STB字体
            System.out.println("GLBrush字体注册成功: " + alias);
        }
    }

    @Override
    public void registerFontFromResource(String alias, String resourcePath) {
        if (!FontRegistry.registerFontFromResource(alias, resourcePath)) {
            System.err.println("GLBrush资源字体注册失败: " + alias);
            return;
        }

        FontRegistry.FontResource fontRes = FontRegistry.getFont(alias);
        if (fontRes != null) {
            getOrCreateFontInfo(fontRes);
            System.out.println("GLBrush资源字体注册成功: " + alias);
        }
    }

    // 在GLBrush中确保所有Picture方法调用正确

    @Override
    public void drawPicture(Picture picture, float x, float y) {
        if (glHandle == 0 || picture == null || !picture.isLoaded()) return;

        int textureId = getOrCreateTexture(picture);
        if (textureId == 0) return;

        // 保存当前状态
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // 设置颜色（支持透明度）
        GL11.glColor4f(color.getR(), color.getG(), color.getB(), picture.getOpacity() * color.getA());

        // 绑定纹理
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // 应用变换
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glRotatef(picture.getRotation(), 0, 0, 1);
        GL11.glScalef(picture.getScaleX(), picture.getScaleY(), 1);

        // 绘制纹理四边形
        GL11.glBegin(GL11.GL_QUADS);
        {
            int width = picture.getImageWidth();
            int height = picture.getImageHeight();

            GL11.glTexCoord2f(0, 0); GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(1, 0); GL11.glVertex2f(width, 0);
            GL11.glTexCoord2f(1, 1); GL11.glVertex2f(width, height);
            GL11.glTexCoord2f(0, 1); GL11.glVertex2f(0, height);
        }
        GL11.glEnd();

        // 恢复状态
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }


    @Override
    public void drawPicture(Picture picture, float x, float y, float width, float height) {
        if (glHandle == 0 || picture == null || !picture.isLoaded()) return;

        int textureId = getOrCreateTexture(picture);
        if (textureId == 0) return;

        // 保存当前状态
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        // 设置颜色（支持透明度）
        GL11.glColor4f(color.getR(), color.getG(), color.getB(), picture.getOpacity());

        // 绑定纹理
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureId);

        // 应用变换
        GL11.glPushMatrix();
        GL11.glTranslatef(x, y, 0);
        GL11.glRotatef(picture.getRotation(), 0, 0, 1);

        // 绘制指定尺寸的纹理四边形
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glTexCoord2f(0, 0); GL11.glVertex2f(0, 0);
            GL11.glTexCoord2f(1, 0); GL11.glVertex2f(width, 0);
            GL11.glTexCoord2f(1, 1); GL11.glVertex2f(width, height);
            GL11.glTexCoord2f(0, 1); GL11.glVertex2f(0, height);
        }
        GL11.glEnd();

        // 恢复状态
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private int getOrCreateTexture(Picture picture) {
        // 检查缓存
        Integer cachedTexture = textureCache.get(picture);
        if (cachedTexture != null && cachedTexture != 0) {
            return cachedTexture;
        }

        // 检查是否已有原生句柄
        Object nativeHandle = picture.getNativeHandle();
        if (nativeHandle instanceof Integer) {
            int textureId = (Integer) nativeHandle;
            textureCache.put(picture, textureId);
            return textureId;
        }

        // 创建新纹理
        try {
            ByteBuffer imageData = picture.getImageData();
            int width = picture.getImageWidth();
            int height = picture.getImageHeight();
            int channels = picture.getChannels();

            if (imageData == null || width <= 0 || height <= 0) {
                System.err.println("无效的图片数据: " + picture.getPath());
                return 0;
            }

            // 生成纹理ID
            IntBuffer textureId = MemoryUtil.memAllocInt(1);
            GL11.glGenTextures(textureId);
            int texId = textureId.get(0);
            MemoryUtil.memFree(textureId);

            // 绑定纹理
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);

            // 设置纹理参数
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);

            // 确定格式
            int format = getImageFormat(channels);

            // 上传纹理数据
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, format, width, height, 0,
                    format, GL11.GL_UNSIGNED_BYTE, imageData);

            // 缓存纹理
            textureCache.put(picture, texId);
            picture.setNativeHandle(texId);

            System.out.println("创建OpenGL纹理: " + picture.getPath() + " [ID: " + texId + ", 尺寸: " + width + "x" + height + "]");

            return texId;

        } catch (Exception e) {
            System.err.println("创建纹理失败: " + picture.getPath() + " - " + e.getMessage());
            return 0;
        }
    }

    private int getImageFormat(int channels) {
        return switch (channels) {
            case 1 -> GL11.GL_RED;
            case 2 -> GL11.GL_RGBA;
            case 3 -> GL11.GL_RGB;
            case 4 -> GL11.GL_RGBA;
            default -> GL11.GL_RGBA; // 默认RGBA
        };
    }

    // 在cleanup方法中添加纹理清理
    @Override
    public void cleanup() {
        // 清理字符纹理
        charTextureCache.values().forEach(GL11::glDeleteTextures);
        charTextureCache.clear();

        // 清理图片纹理
        textureCache.values().forEach(GL11::glDeleteTextures);
        textureCache.clear();

        // 清理字体缓冲区
        fontBuffers.values().forEach(MemoryUtil::memFree);
        fontBuffers.clear();
        fontInfos.clear();
    }

    @Override
    public float measureText(String text, Font font) {
        FontRegistry.FontResource fontRes = FontRegistry.getFont(font.getAvailableName());
        if (fontRes == null) return 0;

        STBTTFontinfo fontInfo = getOrCreateFontInfo(fontRes);
        if (fontInfo == null) return 0;

        float scale = STBTruetype.stbtt_ScaleForPixelHeight(fontInfo, font.getSize());
        float width = 0;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            for (int i = 0; i < text.length(); i++) {
                char ch = text.charAt(i);

                // 获取字符宽度
                IntBuffer advance = stack.mallocInt(1);
                STBTruetype.stbtt_GetCodepointHMetrics(fontInfo, ch, advance, null);
                width += advance.get(0) * scale;

                // 添加字距调整
                if (i < text.length() - 1) {
                    width += STBTruetype.stbtt_GetCodepointKernAdvance(
                            fontInfo, ch, text.charAt(i + 1)
                    ) * scale;
                }
            }
        }
        return width;
    }

    @Override
    public void setClip(float x, float y, float width, float height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int)x, (int)y, (int)width, (int)height);
    }

    @Override
    public void resetClip() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
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
        GL11.glTranslatef(x, y, 0);
    }

    @Override
    public void rotate(float degrees) {
        GL11.glRotatef(degrees, 0, 0, 1);
    }

    @Override
    public void scale(float sx, float sy) {
        GL11.glScalef(sx, sy, 1);
    }

    public Handle getHandle() {
        return handle;
    }

}
