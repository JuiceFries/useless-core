package org.useless.gui.event;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import org.lwjgl.glfw.GLFW;
import org.useless.gui.template.Container;
import org.useless.gui.template.Template;
import org.useless.gui.data.Ternary;
import org.useless.gui.data.TernaryList;
import org.useless.gui.template.agreement.RootAgreement;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 主动收集型事件管理器 - 正确区分根容器和普通容器
 */
public class EventManager {
    private static EventManager instance;
    private boolean out = false;

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    // 监听的模板列表
    private final Set<Template> monitoredTemplates = new HashSet<>(); // 改用Set避免重复

    // 三级事件存储
    private final Ternary<Input, EventType, EventRate> inputEvents = new TernaryList<>();
    private final Ternary<Mouse, EventType, EventRate> mouseEvents = new TernaryList<>();
    private final Ternary<Root, EventType, EventRate> rootEvents = new TernaryList<>();

    // 事件队列
    private final Queue<Runnable> eventQueue = new ConcurrentLinkedQueue<>();

    private long handle;
    private boolean initialized = false;

    private EventManager() {
        // 空构造器，通过monitorTemplate主动添加
    }

    public void setOut(boolean out) {
        this.out = out;
    }

    /**
     * 监控模板并收集其事件
     */
    public void monitorTemplate(Template template) {
        if (template == null || monitoredTemplates.contains(template)) {
            return;
        }

        if (out) System.out.println(this);

        monitoredTemplates.add(template);
        collectEventsFromTemplate(template);
        if (out) System.out.println("监控模板: " + template.getClass().getSimpleName());

        if (!initialized) {
            initializeHandle(template);
        }
    }

    /**
     * 停止监控模板
     */
    public void stopMonitoredTemplates(Template template) {
        if (monitoredTemplates.remove(template)) {
            removeTemplateEvents(template);
            if (out) System.out.println("停止监控模板: " + template.getClass().getSimpleName());
        }
    }

    /**
     * 初始化GLFW句柄
     */
    private void initializeHandle(Template template) {
        if (template.getDraw().getHandle().glHandle() != 0) {
            handle = template.getDraw().getHandle().glHandle();
            setupGlfwCallbacks();
            initialized = true;
        }
    }

    /**
     * 设置GLFW回调
     */
    private void setupGlfwCallbacks() {
        if (handle == 0) return;

        // 字符输入回调
        GLFW.glfwSetCharCallback(handle, (window, codepoint) -> eventQueue.offer(() -> {
            for (int i = 0; i < inputEvents.size(); i++) {
                inputEvents.getFirst(i).inputKey((char) codepoint);
            }
        }));

        // 键盘按键回调
        GLFW.glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> eventQueue.offer(() -> {
            for (int i = 0; i < inputEvents.size(); i++) {
                Input input = inputEvents.getFirst(i);
                switch (action) {
                    case GLFW.GLFW_PRESS -> input.pressKey(key);
                    case GLFW.GLFW_RELEASE -> input.loosenKey(key);
                    case GLFW.GLFW_REPEAT -> input.longPress(key);
                }
            }
        }));

        // 鼠标点击回调
        GLFW.glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            double[] x = new double[1], y = new double[1];
            GLFW.glfwGetCursorPos(window, x, y);
            eventQueue.offer(() -> {
                for (int i = 0; i < mouseEvents.size(); i++) {
                    if (action == GLFW.GLFW_PRESS) {
                        Mouse mouse = mouseEvents.getFirst(i);
                        int convertedButton = convertMouseButton(button);
                        // 调用两个重载方法
                        //noinspection MagicConstant
                        mouse.clickEvent(convertedButton);                    // 无坐标版本
                        //noinspection MagicConstant
                        mouse.clickEvent(convertedButton, (int)x[0], (int)y[0]); // 有坐标版本
                    }
                }
            });
        });

        // 鼠标滚动回调
        GLFW.glfwSetScrollCallback(handle, (window, xOffset, yOffset) -> {
            eventQueue.offer(() -> {
                for (int i = 0; i < mouseEvents.size(); i++) {
                    Mouse mouse = mouseEvents.getFirst(i);
                    mouse.scrollEvent((int)(yOffset * 10)); // 乘个系数让滚动更明显
                }
            });
        });

        GLFW.glfwSetWindowPosCallback(handle, (window, x, y) -> eventQueue.offer(() -> {
            for (int i = 0; i < rootEvents.size(); i++) {
                rootEvents.getFirst(i).DragEvent();
            }
        }));

        // 鼠标移动回调
        GLFW.glfwSetCursorPosCallback(handle, (window, x, y) -> eventQueue.offer(() -> {
            for (int i = 0; i < mouseEvents.size(); i++) {
                Mouse mouse = mouseEvents.getFirst(i);
                mouse.mouseCoordinates(new org.useless.gui.data.Location((int)x, (int)y));
            }
        }));

        // 窗口事件回调 - 只触发根事件
        GLFW.glfwSetWindowSizeCallback(handle, (window, w, h) -> eventQueue.offer(() -> {
            for (int i = 0; i < rootEvents.size(); i++) {
                rootEvents.getFirst(i).SizeEvent();
            }
        }));

        GLFW.glfwSetWindowCloseCallback(handle, (window) -> {
            GLFW.glfwSetWindowShouldClose(window, false); // 阻止默认关闭
            eventQueue.offer(() -> {
                for (int i = 0; i < rootEvents.size(); i++) {
                    rootEvents.getFirst(i).CloseEvent();
                }
            });
        });
    }

    /**
     * 核心：从模板收集事件处理器（正确区分容器类型）
     */
    private void collectEventsFromTemplate(Template template) {
        // 收集输入事件
        for (Input input : template.getInputList()) {
            if (!containsInput(input)) {
                EventType type = determineEventType(template);
                inputEvents.add(input, type, EventRate.LOW_FREQUENCY);
            }
        }

        // 收集鼠标事件
        for (Mouse mouse : template.getMouseList()) {
            if (!containsMouse(mouse)) {
                EventType type = determineEventType(template);
                mouseEvents.add(mouse, type, EventRate.LOW_FREQUENCY);
            }
        }

        // 收集根事件 - 只有根容器才有
        if (isRootContainer(template)) {
            Root root = ((RootAgreement) template).getRoot();
            if (root != null && !containsRoot(root)) {
                rootEvents.add(root, EventType.ROOT, EventRate.LOW_FREQUENCY);
            }
        }
    }

    /**
     * 判断是否为根容器（正确实现）
     */
    private boolean isRootContainer(Template template) {
        return template instanceof Container &&
                template instanceof RootAgreement &&
                ((Container) template).isRootContainer();
    }

    /**
     * 确定事件类型
     */
    private EventType determineEventType(Template template) {
        if (template instanceof Container) {
            return ((Container) template).isRootContainer() ? EventType.ROOT : EventType.NON_ROOT;
        }
        return EventType.GENERAL;
    }

    /**
     * 移除模板相关的事件处理器
     */
    private void removeTemplateEvents(Template template) {
        // 移除输入事件
        for (Input input : template.getInputList()) {
            removeInput(input);
        }

        // 移除鼠标事件
        for (Mouse mouse : template.getMouseList()) {
            removeMouse(mouse);
        }

        // 移除根事件
        if (isRootContainer(template)) {
            Root root = ((RootAgreement) template).getRoot();
            if (root != null) {
                removeRoot(root);
            }
        }
    }

    // 查重和移除辅助方法
    private boolean containsInput(Input target) {
        for (int i = 0; i < inputEvents.size(); i++) {
            if (inputEvents.getFirst(i).equals(target)) return true;
        }
        return false;
    }

    private boolean containsMouse(Mouse target) {
        for (int i = 0; i < mouseEvents.size(); i++) {
            if (mouseEvents.getFirst(i).equals(target)) return true;
        }
        return false;
    }

    private boolean containsRoot(Root target) {
        for (int i = 0; i < rootEvents.size(); i++) {
            if (rootEvents.getFirst(i).equals(target)) return true;
        }
        return false;
    }

    private void removeInput(Input target) {
        for (int i = 0; i < inputEvents.size(); i++) {
            if (inputEvents.getFirst(i).equals(target)) {
                inputEvents.remove(i);
                break;
            }
        }
    }

    private void removeMouse(Mouse target) {
        for (int i = 0; i < mouseEvents.size(); i++) {
            if (mouseEvents.getFirst(i).equals(target)) {
                mouseEvents.remove(i);
                break;
            }
        }
    }

    private void removeRoot(Root target) {
        for (int i = 0; i < rootEvents.size(); i++) {
            if (rootEvents.getFirst(i).equals(target)) {
                rootEvents.remove(i);
                break;
            }
        }
    }

    /**
     * 处理事件队列（每帧调用）
     */
    public void processEvents() {
        int processed = 0;
        while (!eventQueue.isEmpty() && processed < 100) { // 每帧最多处理100个
            Runnable task = eventQueue.poll();
            if (task != null) {
                try {
                    task.run();
                    processed++;
                } catch (Exception e) {
                    System.err.println("事件处理失败: " + e.getMessage());
                }
            }
        }
    }

    /**
     * 转换鼠标按钮
     */
    private int convertMouseButton(int glfwButton) {
        return switch (glfwButton) {
            case GLFW.GLFW_MOUSE_BUTTON_LEFT -> Mouse.LEFT_CLICK;
            case GLFW.GLFW_MOUSE_BUTTON_RIGHT -> Mouse.RIGHT_CLICK;
            case GLFW.GLFW_MOUSE_BUTTON_MIDDLE -> Mouse.MIDDLE_CLICK;
            default -> glfwButton;
        };
    }

    /**
     * 刷新事件收集
     */
    public void refreshEvents() {
        monitoredTemplates.forEach(this::collectEventsFromTemplate);
    }

    /**
     * 获取监控状态
     */
    public String getMonitorStatus() {
        return String.format("监控状态 - 模板:%d 输入事件:%d 鼠标事件:%d 根事件:%d 待处理队列:%d",
                monitoredTemplates.size(), inputEvents.size(),
                mouseEvents.size(), rootEvents.size(), eventQueue.size());
    }

    /**
     * 清理资源
     */
    public void cleanup() {
        monitoredTemplates.clear();
        inputEvents.clear();
        mouseEvents.clear();
        rootEvents.clear();
        eventQueue.clear();
        initialized = false;
        handle = 0;
    }

    public boolean isInitialized() {
        return initialized;
    }

}