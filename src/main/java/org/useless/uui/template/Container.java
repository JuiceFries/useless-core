package org.useless.uui.template;

import org.useless.uui.Arrange;
import org.useless.uui.Window;

import java.util.List;

/**
 * 容器接口<br>
 * 提供了两种方法 [ 注册 | 注销 ] <br>
 * 以及两种方法的变体用以批量注册或注销。
 * @author JuiceFries
 * @see Template
 * @see Window
 * @version 0.0.3
 * @since 0.0.3
 */
public non-sealed interface Container extends Template {

    /**
     * 注册模板ui<br>
     * 将对应的模板注册到渲染流程列表中
     * @param template 模板
     */
    void enrolled(Template template);

    /**
     * 注册模板ui
     * @param template 模板
     */
    void enrolled(Template... template);

    /**
     * 注销模板<br>
     * 注销UI组件使面板不在渲染该UI
     * @param template 模板
     */
    void logout(Template template);

    /**
     * 注销模板<br>
     * 注销UI组件使面板不在渲染该UI
     * @param template 模板
     */
    void logout(Template... template);

    /**
     * 用于设置模板排布<br>
     * 参数为null时为绝对布局其他根据功能自动布置
     * @param arrange 布置
     * @see Arrange
     */
    void setArrange(Arrange arrange);

    /**
     * 获取模板数组
     * @return 模板
     */
    List<Template> getTemplates();

}
