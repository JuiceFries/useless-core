package org.useless.gui.font;

import org.useless.io.ListLoader;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 字体注册表 - 管理字体文件的加载和缓存
 */
public class FontRegistry {
    private static final Map<String, FontResource> fonts = new ConcurrentHashMap<>();
    private static boolean testOut = false;

    static {
        // 从listing.json注册字体
        registerFontsFromListing();
    }

    private static void registerFontsFromListing() {
        try {
            ListLoader.initializeLoading();
            Map<String, String> fontMap = ListLoader.getAllFonts();
            for (Map.Entry<String, String> entry : fontMap.entrySet()) {
                try {
                    FontResource fontRes = loadSingleFontResource(entry.getValue());
                    fonts.put(entry.getKey(), fontRes);
                    if (testOut) System.out.println("字体注册成功: " + entry.getKey());
                } catch (Exception e) {
                    System.err.println("字体加载失败: " + entry.getKey() + " - " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("字体注册表初始化失败: " + e.getMessage());
        }
    }

    // 直接从资源加载字体数据
    private static FontResource loadSingleFontResource(String resourcePath) throws Exception {
        InputStream is = FontRegistry.class.getResourceAsStream(resourcePath);
        if (is == null) throw new Exception("字体资源不存在: " + resourcePath);

        byte[] data = is.readAllBytes();
        is.close();
        return new FontResource(resourcePath, data);
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

    // 字体资源类 - 只存内存数据
    public static class FontResource {
        private final String name;
        private final byte[] fontData;

        public FontResource(String name, byte[] fontData) {
            this.name = name;
            this.fontData = fontData;
        }

        public byte[] getFontData() { return fontData; }
        public String getName() { return name; }
    }

    private static FontResource loadFontFromFile(String filePath) throws Exception {
        java.io.File file = new java.io.File(filePath);
        byte[] data = java.nio.file.Files.readAllBytes(file.toPath());
        return new FontResource(file.getName(), data);
    }

    private static FontResource loadFontFromResource(String resourcePath) throws Exception {
        InputStream is = FontRegistry.class.getResourceAsStream(resourcePath);
        if (is == null) throw new Exception("字体资源不存在: " + resourcePath);

        byte[] data = is.readAllBytes();
        is.close();
        return new FontResource(resourcePath, data);
    }
}
