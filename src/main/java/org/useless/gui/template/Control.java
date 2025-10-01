package org.useless.gui.template;

import java.util.List;
import org.useless.gui.event.Input;
import org.useless.gui.event.Mouse;

/**
 * 控件接口<br>
 * 用以创建控件
 * @see org.useless.gui.template.Template
 */
public non-sealed interface Control extends Template {

    @Override
    default void addInputEvent(Input input) {

    }

    @Override
    default void addMouseEvent(Mouse mouse) {

    }

    @Override
    default void removeInputEvent(Input input) {

    }

    @Override
    default void removeMouseEvent(Mouse mouse) {

    }

    @Override
    default List<Input> getInputList() {
        return List.of();
    }

    @Override
    default List<Mouse> getMouseList() {
        return List.of();
    }

    int getArcWidth();

    int getArcHeight();
}
