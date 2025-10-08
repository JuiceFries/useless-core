package org.useless.gui.uir;

import org.jetbrains.annotations.NotNull;
import org.useless.gui.data.Color;
import org.useless.gui.template.Template;

/**
 * 深色样式
 */
public class BrunetStyle implements Style {

    @Override
    public void apply(@NotNull Template target) {
        target.setBackground(new Color(30, 30, 30));
    }

    @Override
    public String getKey() {
        return "BrunetStyle";
    }

    @Override
    public void reset(Template target) {
    }

}
