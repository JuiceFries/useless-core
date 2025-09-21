package org.useless.uui;

import org.jetbrains.annotations.NotNull;
import org.useless.uui.font.FontStyle;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFont;

public class Font {
    public static final String DEFAULT = "default";

    private static final Map<String, Integer> loadedFonts = new ConcurrentHashMap<>();
    private static long globalNvgContext = 0;

    // 在Font类中添加（如果NanoVG没有预定义）
    private static final int NVG_TEXT_BOLD = 1;
    private static final int NVG_TEXT_ITALIC = 2;

    private final String name;
    private final FontStyle fontStyle;
    private final int size;

    public Font(@NotNull Font font) {
        this(font.getName(), font.getStyle(), font.getSize());
    }

    public Font(String name, FontStyle fontStyle, int size) {
        this.name = name;
        this.fontStyle = fontStyle;
        this.size = size;
    }

    public String getName() { return name; }
    public FontStyle getStyle() { return fontStyle; }
    public int getSize() { return size; }

    // 初始化字体系统
    public static void initializeFontSystem(long nvgContext) {
        globalNvgContext = nvgContext;
        loadDefaultFonts();
    }

    // 修改enrolledFont方法中的判断条件
    public static boolean enrolledFont(String alias, String fontPath) {
        if (globalNvgContext == 0) throw new IllegalStateException("字体系统未初始化！先调用initializeFontSystem()");
        if (loadedFonts.containsKey(alias)) {
            System.err.println("字体别名已存在: " + alias);
            return false;
        }

        try (InputStream is = Font.class.getResourceAsStream(fontPath)) {
            if (is == null) throw new IOException("字体文件不存在: " + fontPath);

            File tempFont = File.createTempFile("font_", ".ttf");
            Files.copy(is, tempFont.toPath(), StandardCopyOption.REPLACE_EXISTING);
            tempFont.deleteOnExit();

            int handle = nvgCreateFont(globalNvgContext, alias, tempFont.getAbsolutePath());
            // 修改这里：nvgCreateFont成功返回0，失败返回-1
            if (handle == -1) {
                System.err.println("NVG字体创建失败: " + alias);
                return false;
            }

            loadedFonts.put(alias, handle);
            //System.out.println("字体注册成功: " + alias + ", 句柄: " + handle);
            return true;

        } catch (IOException e) {
            System.err.println("字体注册失败[" + alias + "]: " + e.getMessage());
            return false;
        }
    }

    // 真正的字体卸载方法
    public static boolean logoutFont(String alias) {
        Integer handle = loadedFonts.remove(alias);
        if (handle != null) {
            System.out.println("字体卸载: " + alias);
            return true;
        }
        System.err.println("字体未找到: " + alias);
        return false;
    }

    // 加载默认字体
    private static void loadDefaultFonts() {
        String[][] fontConfigs = {
                {"default", "/font/SourceHanSansSC-Regular.otf"}
        };

        for (String[] config : fontConfigs) {
            if (!enrolledFont(config[0], config[1])) {
                System.err.println("默认字体加载失败: " + config[0]);
            }
        }
    }

    // 工厂方法创建字体实例
    public static Font create(String fontName, FontStyle fontStyle, int size) {
        if (!loadedFonts.containsKey(fontName)) {
            throw new IllegalStateException("字体未注册: " + fontName);
        }
        return new Font(fontName, fontStyle, size);
    }

    // 获取NVG字体句柄
    public int getNVGFontHandle() {
        Integer handle = loadedFonts.get(name);
        if (handle == null) throw new IllegalStateException("字体未加载: " + name);
        return handle;
    }

    // 获取NanoVG样式标志

    /**
     * 懒得实现
     * @return 看个屁
     */
    public int getNVGFontFlags() {
        int flags = 0;
        // 只设置NanoVG实际支持的标志位
        // if (style == Style.BOLD) flags |= NVG_TEXT_BOLD; // 注释掉不支持的
        // if (style == Style.ITALIC) flags |= NVG_TEXT_ITALIC;
        return flags;
    }

    // 检查字体是否已加载
    public static boolean isFontLoaded(String alias) {
        return loadedFonts.containsKey(alias);
    }

    // 清空所有字体
    public static void clearAllFonts() {
        loadedFonts.clear();
        System.out.println("所有字体已清空");
    }

    // 获取指定别名的字体句柄
    public static int getNVGFontHandle(String alias) {
        Integer handle = loadedFonts.get(alias);
        if (handle == null) throw new IllegalStateException("字体未加载: " + alias);
        return handle;
    }
}