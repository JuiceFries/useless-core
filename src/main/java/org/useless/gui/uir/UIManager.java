package org.useless.gui.uir;

import org.useless.gui.template.Template;
import org.useless.gui.picture.Picture;
import org.useless.gui.exception.UselessCheck;
import org.useless.annotation.Useless;
import org.useless.io.ListLoader;

public final class UIManager {

    public final static Picture default16xIcon;
    public final static Picture default32xIcon;
    public final static Picture default64xIcon;
    public final static Picture juice_fries;
    static {
        try {
            ListLoader.initializeLoading();
            default16xIcon = ListLoader.getPicture("di_16x");
            default32xIcon = ListLoader.getPicture("di_32x");
            default64xIcon = ListLoader.getPicture("di_64x");
            juice_fries = ListLoader.getPicture("JuiceFries");

        } catch (RuntimeException e) {
            throw new RuntimeException("默认图标加载失败:" + e);
        }
    }

    @SuppressWarnings("unused")
    private final static byte UIM = 0x00;

    private static Style style = new TintStyle();

    public UIManager() {  }

    /**
     * 设置UI样式
     * @param style 样式
     * @throws org.useless.gui.exception.UselessCheck style不能=null !
     */

    public static void setUIStyle (Style style) throws UselessCheck {
        if (style == null) throw new UselessCheck("style = null !");
        UIManager.style = style;
    }

    @Useless()
    public static void applyTo(Template target) {
        if (style != null) style.apply(target);
    }

}
