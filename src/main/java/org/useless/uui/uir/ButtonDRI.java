package org.useless.uui.uir;

import org.useless.uui.template.control.Button;
import org.useless.uui.Drawing;

/**
 * <h1>按钮渲染器</h1>
 * 用于提供渲染按钮的接口方法
 * @see Button
 * @see Drawing
 * @since 0.0.3
 */
public interface ButtonDRI {

    /**
     * 用于初始化按钮渲染器
     * @return 私有渲染器
     */
    static ButtonDRI init() {
        return new ButtonUI();
    }

    /**
     * 渲染文本<br>
     * 用于渲染文本，样式自己决定去他只是渲染文本位置
     * @param button 按钮
     * @see String
     * @see Button
     */
    void renderText(Button button);

    /**
     * 自动判断<br>
     * 用以判断按钮特定渲染
     * @param button 按钮
     */
    @FullName(fullName = "automaticJudgment")
    void autoJudge(Button button);

    /**
     * 悬停判断
     * @param button 按钮
     * @see Button
     */
    void hoverJudgment(Button button);

    /**
     * 选中框绘制
     * @param button 按钮
     * @see Button
     */
    void selectedFrame(Button button);

    /**
     * 默认事件实现<br>
     * 用以实现默认实现
     * @param button 按钮
     */
    @FullName(fullName = "DefaultEventImplementation")
    void defEventImpl(Button button);

}
