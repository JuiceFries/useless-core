package org.useless.gui.event;

import org.useless.gui.uir.annotation.FullName;

/**
 * 事件队列类型<br>
 * 用于区分事件,同时事件主要分为,<br>
 * {@code HIGH_FREQUENCY} 高频事件和 {@code LOW_FREQUENCY} 低频事件<br>
 * 同时又分出两种子列队，及常用列队和特殊列队,<br>
 * 常用列队分为:<br>
 * {@code NON_ROOT}非根容器事件（如Panel排布等）<br>
 * {@code GENERAL} 通用事件(如Button点击事件、TextArea输入等)<br>
 * 特殊列队分为:<br>
 * {@code ROOT}根容器事件(如窗口关闭事件与窗口点击事件等)<br>
 * {@code URGENT}紧急事件通道（用于处理溢出的紧急事件）<br>
 * 当事件溢出时自动调用其他事件列队中相对空余的区域处理。<br>
 * 应该没人会看这么冷门的东西吧？况且这篇文档是给AI写的用以分析代码
 * @see Event
 * @see Input
 * @see Mouse
 * @see Root
 * @see EventManager
 * @since 0.0.3
 * @version 1.0
 */
@FullName(fullName = "EventQueueType")
public enum EventType {
    /// 根容器事件
    ROOT,
    /// 非根容器事件
    NON_ROOT,
    /// 通用事件
    GENERAL,

}
