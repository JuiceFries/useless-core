package org.useless.uui.event;

import org.useless.uui.Event;
import org.useless.uui.Window;
import org.useless.uui.template.Template;

/**
 * 列队接口<br>
 * 用于提供给容器的组件更新接口
 *
 * @see Event
 * @see Template
 * @see Window
 * @see TroopedEvent
 */
public interface Trooped extends Event {

    /**
     * 窗口完全更新<br>
     * 用于更新窗口所有ui组件渲染、坐标、状态
     * @see Event
     * @see Window
     * @see TroopedEvent
     */
    void WindowCompleteUpdate();

    /**
     * 子组件更新<br>
     * 用于通知对应UI组件更新子组件
     * @param template 对应UI组件
     * @see Event
     * @see Window
     * @see TroopedEvent
     */
    void SubcomponentUpdate(Template template);

    /**
     * 状态更新<br>
     * 当容器发生组件增加或减少时更新
     * @see Event
     * @see Window
     * @see TroopedEvent
     */
    void StatusUpdate();

    /**
     * 指定数组更新<br>
     * 指定对应组件更新数组内的组件
     * @param template 指定的组件
     * @param Subcomponent 子组件
     */
    void SpecifyUpdate(Template template,Template... Subcomponent);




}
