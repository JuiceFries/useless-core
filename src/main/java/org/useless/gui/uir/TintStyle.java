package org.useless.gui.uir;

import org.useless.gui.data.Color;
import org.useless.gui.template.Template;

/**
 * 浅色样式
 */
public class TintStyle implements Style {

    @Override
    public void apply(Template target) {
        target.setBackground(Color.WHITE);
    }

    @Override
    public String getKey() {
        return "TintStyle";
    }

    @Override
    public void reset(Template target) {

    }

}
