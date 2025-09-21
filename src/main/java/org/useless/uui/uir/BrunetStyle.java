package org.useless.uui.uir;

import org.jetbrains.annotations.NotNull;
import org.useless.uui.Color;
import org.useless.uui.template.Template;

/**
 * 深色样式
 */
@Deprecated
public class BrunetStyle implements Style {

    @Deprecated
    @Override
    public void apply(@NotNull Template target) {
        target.setBackground(new Color(69, 69, 69));
    }

    @Deprecated
    @Override
    public String getKey() {
        return "BrunetStyle";
    }

    @Deprecated
    @Override
    public void reset(Template target) {
    }

}
