package org.useless.gui.font;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字体注册表 - 管理字体文件的加载和缓存
 */
public class FontRegistry {
    private static final Map<String, FontResource> fonts = new ConcurrentHashMap<>();
    private static boolean testOut = false;

    static {
        // 注册默认字体
        registerDefaultFont();
    }

    private static void registerDefaultFont() {
        // 批量注册字体
        String[][] fontEntries = {
                {Font.DEFAULT, "/font/SourceHanSansSC-Regular.otf"},
                {Font.SIM_SUN, "/font/LXGWWenKaiScreen.ttf"}
        };

        for (String[] entry : fontEntries) {
            try {
                FontResource fontRes = loadSingleFontResource(entry[1]);
                fonts.put(entry[0], fontRes);
                if (testOut) System.out.println("字体注册成功: " + entry[0]);
            } catch (Exception e) {
                System.err.println("字体加载失败: " + entry[0] + " - " + e.getMessage());
            }
        }
    }

    // 新增单字体加载方法
    private static FontResource loadSingleFontResource(String resourcePath) throws Exception {
        InputStream is = FontRegistry.class.getResourceAsStream(resourcePath);
        if (is == null) throw new Exception("字体资源不存在: " + resourcePath);

        byte[] data = is.readAllBytes();
        is.close();

        File tempFile = File.createTempFile("font_", ".ttf");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), data);

        return new FontResource(resourcePath, data, tempFile);
    }

    public static void setTestOut(boolean testOut) {
        FontRegistry.testOut = testOut;
    }

    /**
     * 注册新字体
     */
    public static boolean registerFont(String alias, String filePath) {
        try {
            FontResource fontRes = loadFontFromFile(filePath);
            fonts.put(alias, fontRes);
            System.out.println("字体注册成功: " + alias + " -> " + filePath);
            return true;
        } catch (Exception e) {
            System.err.println("字体注册失败: " + alias + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * 从classpath注册字体
     */
    public static boolean registerFontFromResource(String alias, String resourcePath) {
        try {
            FontResource fontRes = loadFontFromResource(resourcePath);
            fonts.put(alias, fontRes);
            return true;
        } catch (Exception e) {
            System.err.println("资源字体注册失败: " + alias + " - " + e.getMessage());
            return false;
        }
    }

    /**
     * 获取字体数据
     */
    public static FontResource getFont(String alias) {
        return fonts.getOrDefault(alias, fonts.get(Font.DEFAULT));
    }

    // 字体资源类
    public static class FontResource {
        private final String name;
        private final byte[] fontData;
        private final File fontFile; // 临时文件

        public FontResource(String name, byte[] fontData, File fontFile) {
            this.name = name;
            this.fontData = fontData;
            this.fontFile = fontFile;
        }

        public byte[] getFontData() { return fontData; }
        public File getFontFile() { return fontFile; }
        public String getName() { return name; }
    }

    private static FontResource loadFontFromFile(String filePath) throws Exception {
        File file = new File(filePath);
        byte[] data = Files.readAllBytes(file.toPath());
        return new FontResource(file.getName(), data, file);
    }

    private static FontResource loadFontFromResource(String resourcePath) throws Exception {
        InputStream is = FontRegistry.class.getResourceAsStream(resourcePath);
        if (is == null) throw new Exception("字体资源不存在: " + resourcePath);

        byte[] data = is.readAllBytes();
        is.close();

        // 创建临时文件供渲染后端使用
        File tempFile = File.createTempFile("font_", ".ttf");
        tempFile.deleteOnExit();
        Files.write(tempFile.toPath(), data);

        return new FontResource(resourcePath, data, tempFile);
    }
}