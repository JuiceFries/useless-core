package org.useless.gui.template;

import org.useless.gui.arrange.Arrange;
import org.useless.gui.drawing.Drawing;

import java.util.List;
import java.util.function.Consumer;

/**
 * 容器接口<br>
 * 提供了两种方法 {@code 添加|移除 } <br>
 * 以及两种方法的变体用以批量注册或注销。
 * @author JuiceFries
 * @see org.useless.gui.template.Template
 * @see org.useless.gui.template.container.Window
 * @version 0.0.3
 * @since 0.0.3
 */
public non-sealed interface Container extends Template {

    // set =====

    /**
     * 设置容器的可见性
     * @param visible 可见性
     */
    void setVisible(boolean visible);

    // get =====

    /**
     * @return 获取模板列表
     */
    List<Template> getTemplateList();

    /**
     * 获取模板列表
     * @param index 对应索引
     * @return 对应索引的模板
     */
    Template getTemplateList(int index);


    // stm =====

    /**
     * 渲染
     * @param draw 渲染
     */
    void draw(Consumer<Drawing> draw);

    /**
     * 添加模板
     * @param template 模板
     * @throws NullPointerException 所添加的容器不能为空!
     * @throws RuntimeException 根容器不能被添加到任何容器上!
     */
    void add(Template template);

    /**
     * 添加模板数组
     * @param template 模板数组
     * @throws NullPointerException 所添加的容器不能为空!
     * @throws RuntimeException 根容器不能被添加到任何容器上!
     */
    void add(Template... template);

    /**
     * 移除模板
     * @param template 模板
     */
    void remove(Template template);

    /**
     * 移除模板数组
     * @param templates 模板数组
     */
    void remove(Template... templates);

    /**
     * 设置排布
     * @param arrange
     */
    void setArrange(Arrange arrange);

    /**
     * 获取容器的可见性
     * @return 可见性
     */
    boolean isVisible();


    /**
     * @return 判断容器是否为根容器
     */
    default boolean isRootContainer() {
        return false;
    }
}
