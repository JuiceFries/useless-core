package org.useless.gui.uir;

import java.io.InputStream;
import org.useless.gui.template.Template;
import org.useless.gui.picture.ImageLoadException;
import org.useless.gui.picture.ImageLoader;
import org.useless.gui.picture.Picture;
import org.useless.gui.uir.annotation.Useless;

public class UIManager {

    public  static Picture default16xIcon;
    public  static Picture default32xIcon;
    public  static Picture default64xIcon;
    static {
        try {
            // 读取资源为字节数组，然后用loadFromMemory
            default16xIcon = loadResourceAsPicture("picture/di_16x16.png");
            default32xIcon = loadResourceAsPicture("picture/di_32x32.png");
            default64xIcon = loadResourceAsPicture("picture/di_64x64.png");

        } catch (Exception e) {
            throw new RuntimeException("默认图标加载失败:" + e);
        }
    }

    private static Picture loadResourceAsPicture(String resourcePath) throws ImageLoadException {
        try {
            InputStream is = UIManager.class.getResourceAsStream("/" + resourcePath);
            if (is == null) {
                throw new ImageLoadException("资源不存在: /" + resourcePath);
            }

            byte[] imageData = is.readAllBytes();
            is.close();

            return ImageLoader.loadFromMemory(imageData, resourcePath);

        } catch (ImageLoadException e) {
            throw e;
        } catch (Exception e) {
            throw new ImageLoadException("资源加载失败: " + resourcePath, e);
        }
    }

    @SuppressWarnings("unused")
    private final static byte UIM = 0x00;

    private static Style style = new TintStyle();

    public UIManager() {  }

    /**
     * 设置UI样式
     * @param style 样式
     * @throws UselessCheck style不能=null !
     */

    public static void setUIStyle (Style style) throws UselessCheck {
        if (style == null) throw new UselessCheck("style = null !");
        UIManager.style = style;
    }

    @Useless
    public static void applyTo(Template target) {
        if (style != null) style.apply(target);
    }

}
