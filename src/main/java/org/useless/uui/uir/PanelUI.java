package org.useless.uui.uir;

import org.jetbrains.annotations.NotNull;
import org.useless.uui.template.container.Panel;

class PanelUI implements PanelDRI {

    @Deprecated
    @Override
    public void drawBackground(@NotNull Panel panel) {
        panel.getDrawing().setColor(panel.getBackground());
        panel.getDrawing().drawRectangle(0, 0, panel.getWidth(), panel.getHeight());
    }

}
