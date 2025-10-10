package org.useless.io;

import jdk.jfr.Experimental;
import org.jetbrains.annotations.NotNull;
import org.useless.annotation.Fixed;
import org.useless.gui.picture.Picture;
import org.useless.gui.picture.ImageLoader;
import org.useless.gui.exception.ImageLoadException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.useless.gui.picture.ImageLoader.loadFromMemory;

/**
 * 用于加载文件
 */
@Fixed(continuous = "~3-Beta")
@Experimental
public final class ListLoader {
    private static final String listing_path = "/useless_core/listing/useless_core_resource_listing.json";
    private static final Map<String, Picture> pictureCache = new ConcurrentHashMap<>();
    private static boolean initialized = false;
    private static JsonObject listingData;

    /**
     * 初始化加载器
     */
    public static void initializeLoading() {
        if (initialized) return;

        try {
            InputStream is = ListLoader.class.getResourceAsStream(listing_path);
            if (is == null) {
                throw new RuntimeException("清单文件没找到: " + listing_path);
            }

            if (is.available() == 0) {
                throw new RuntimeException("清单文件是空的,自己看看是不是改文件了!");
            }

            listingData = JsonParser.parseReader(new InputStreamReader(is)).getAsJsonObject();
            is.close();
            initialized = true;

        } catch (Exception e) {
            throw new RuntimeException("ListLoader初始化炸了: " + e.getMessage(), e);
        }
    }

    public static Picture getPicture(String name) {
        if (!initialized) {
            throw new RuntimeException("ListLoader没初始化，先调initializeLoading()");
        }

        if (pictureCache.containsKey(name)) {
            return pictureCache.get(name);
        }

        JsonObject icons = listingData.getAsJsonObject("icons");
        if (!icons.has(name)) {
            throw new RuntimeException("图标没在清单里找到: " + name);
        }

        String resourcePath = icons.get(name).getAsString();

        try {
            InputStream is = ListLoader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                throw new RuntimeException("资源文件不存在: " + resourcePath);
            }

            byte[] imageData = is.readAllBytes();
            is.close();

            Picture picture = loadFromMemory(imageData, name);
            pictureCache.put(name, picture);
            return picture;

        } catch (Exception e) {
            throw new RuntimeException("图标加载失败: " + name + " -> " + resourcePath, e);
        }
    }

    /**
     * 获取字体路径
     */
    public static String getFontPath(String fontName) {
        if (!initialized) {
            throw new RuntimeException("ListLoader没初始化");
        }

        JsonObject fonts = listingData.getAsJsonObject("fonts");
        if (!fonts.has(fontName)) {
            throw new RuntimeException("字体没在清单里找到: " + fontName);
        }

        return fonts.get(fontName).getAsString();
    }

    /**
     * 获取所有字体映射
     */
    public static @NotNull Map<String, String> getAllFonts() {
        if (!initialized) {
            throw new RuntimeException("ListLoader没初始化");
        }
        JsonObject fonts = listingData.getAsJsonObject("fonts");
        Map<String, String> result = new ConcurrentHashMap<>();
        for (String key : fonts.keySet()) {
            result.put(key, fonts.get(key).getAsString());
        }
        return result;
    }

    /**
     * 清空缓存
     */
    public static void clearCache() {
        pictureCache.clear();
        System.out.println("ListLoader缓存已清空");
    }

    /**
     * 从classpath资源加载图片
     */
    public static @NotNull Picture loadFromResource(String resourcePath, String name) throws ImageLoadException {
        try {
            InputStream is = ImageLoader.class.getResourceAsStream(resourcePath);
            if (is == null) {
                throw new ImageLoadException("资源不存在: " + resourcePath);
            }

            byte[] imageData = is.readAllBytes();
            is.close();

            return loadFromMemory(imageData, name);

        } catch (ImageLoadException e) {
            throw e;
        } catch (Exception e) {
            throw new ImageLoadException("资源图片加载失败: " + resourcePath, e);
        }
    }

}
