package org.useless.uui.uir;

import org.useless.uui.template.Template;

@Deprecated(since = "0.0.3")
public class UIManager {

    @SuppressWarnings("unused")
    private final static byte UIM = 0x00;

    private static Style style = new TintStyle();

    @Deprecated
    public UIManager() {  }

    /**
     * 设置UI样式
     * @param style 样式
     * @throws UselessCheck style不能=null !
     */
    @Deprecated
    public static void setUIStyle (Style style) throws UselessCheck {
        if (style == null) throw new UselessCheck("style = null !");
        UIManager.style = style;
    }

    @Deprecated
    public static void applyTo(Template target) {
        if (style != null) style.apply(target);
    }

}
