package org.useless.gui.event;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import org.lwjgl.glfw.GLFW;
import org.useless.gui.template.Template;
import org.useless.gui.template.agreement.RootAgreement;

import static java.lang.String.format;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.useless.gui.event.Mouse.LEFT_CLICK;
import static org.useless.gui.event.Mouse.MIDDLE_CLICK;
import static org.useless.gui.event.Mouse.RIGHT_CLICK;

/**
 * 高性能事件管理器 - 保持收集模式但优化处理逻辑
 */
public class EventManager {
    private static EventManager instance;

    private final Set<Template> monitoredTemplates = new CopyOnWriteArraySet<>();
    private final Map<Class<?>, Set<Object>> eventHandlers = new ConcurrentHashMap<>();

    private final Queue<Runnable> keyboardQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Runnable> urgentQueue = new ConcurrentLinkedQueue<>();
    private final Queue<Runnable> normalQueue = new ConcurrentLinkedQueue<>();

    private long handle;
    private boolean initialized = false;

    // 性能统计
    private int framesProcessed = 0;
    private long totalEventsProcessed = 0;

    public static EventManager getInstance() {
        if (instance == null) {
            instance = new EventManager();
        }
        return instance;
    }

    private EventManager() {
        // 初始化事件处理器分类
        eventHandlers.put(Input.class, new HashSet<>());
        eventHandlers.put(Mouse.class, new HashSet<>());
        eventHandlers.put(Root.class, new HashSet<>());
    }

    /**
     * 监控模板并收集事件
     */
    public void monitorTemplate(Template template) {
        if (template == null || monitoredTemplates.contains(template)) {
            return;
        }

        monitoredTemplates.add(template);
        collectTemplateEvents(template);

        if (!initialized) {
            initializeHandle(template);
        }
    }

    /**
     * 高性能事件收集 - 去重、过滤、分类
     */
    private void collectTemplateEvents(Template template) {
        // 收集输入事件
        template.getInputList().stream()
                .filter(Objects::nonNull)
                .forEach(input -> eventHandlers.get(Input.class).add(input));

        // 收集鼠标事件
        template.getMouseList().stream()
                .filter(Objects::nonNull)
                .forEach(mouse -> eventHandlers.get(Mouse.class).add(mouse));

        // 收集根事件
        if (template instanceof RootAgreement) {
            Root root = ((RootAgreement) template).getRoot();
            if (root != null) {
                eventHandlers.get(Root.class).add(root);
            }
        }
    }

    /**
     * 停止监控模板
     */
    public void stopMonitoring(Template template) {
        if (monitoredTemplates.remove(template)) {
            removeTemplateEvents(template);
        }
    }

    /**
     * 移除模板事件
     */
    private void removeTemplateEvents(Template template) {
        // 从所有集合中移除该模板的事件
        template.getInputList().forEach(input -> eventHandlers.get(Input.class).remove(input));
        template.getMouseList().forEach(mouse -> eventHandlers.get(Mouse.class).remove(mouse));

        if (template instanceof RootAgreement) {
            Root root = ((RootAgreement) template).getRoot();
            if (root != null) {
                eventHandlers.get(Root.class).remove(root);
            }
        }
    }

    /**
     * 初始化GLFW句柄和回调
     */
    private void initializeHandle(Template template) {
        if (template.getDraw().getHandle().glHandle() != 0) {
            handle = template.getDraw().getHandle().glHandle();
            setupGlfwCallbacks();
            initialized = true;
        }
    }

    /**
     * 设置GLFW回调 - 优化事件分发
     */
    private void setupGlfwCallbacks() {
        if (handle == 0) return;

        // 字符输入 - 最高优先级，实时处理
        glfwSetCharCallback(handle, (window, codepoint) -> handleCharInput((char) codepoint));

        // 键盘按键 - 高优先级
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            switch (action) {
                case GLFW.GLFW_PRESS -> keyboardQueue.offer(() -> handleKeyPress(key));
                case GLFW.GLFW_RELEASE -> keyboardQueue.offer(() -> handleKeyRelease(key));
                case GLFW.GLFW_REPEAT -> keyboardQueue.offer(() -> handleKeyRepeat(key));
            }
        });

        // 鼠标点击 - 中优先级
        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            double[] x = new double[1], y = new double[1];
            glfwGetCursorPos(window, x, y);

            if (action == GLFW.GLFW_PRESS) {
                urgentQueue.offer(() -> handleMouseClick(button, (int) x[0], (int) y[0], false));
            } else if (action == GLFW.GLFW_RELEASE) {
                urgentQueue.offer(() -> handleMouseClick(button, (int) x[0], (int) y[0], true));
            }
        });

        // 鼠标滚动 - 中优先级
        glfwSetScrollCallback(handle, (window, xOffset, yOffset) -> urgentQueue.offer(() -> handleMouseScroll((int)(yOffset * 10))));

        // 鼠标移动 - 低优先级
        glfwSetCursorPosCallback(handle, (window, x, y) -> normalQueue.offer(() -> handleMouseMove((int)x, (int)y)));

        // 窗口事件 - 中优先级
        glfwSetWindowSizeCallback(handle, (window, w, h) -> urgentQueue.offer(this::handleWindowResize));

        glfwSetWindowCloseCallback(handle, (window) -> {
            glfwSetWindowShouldClose(window, false);
            urgentQueue.offer(this::handleWindowClose);
        });
    }

    /**
     * 事件处理器方法 - 避免在回调中直接循环
     */
    private void handleCharInput(char codepoint) {
        // 实时处理，不进队列
        Set<Object> inputs = eventHandlers.get(Input.class);
        for (Object handler : inputs) {
            ((Input) handler).inputKey(codepoint);
        }
    }

    private void handleKeyPress(int key) {
        Set<Object> inputs = eventHandlers.get(Input.class);
        for (Object handler : inputs) {
            ((Input) handler).pressKey(key);
        }
    }

    private void handleKeyRelease(int key) {
        Set<Object> inputs = eventHandlers.get(Input.class);
        for (Object handler : inputs) {
            ((Input) handler).loosenKey(key);
        }
    }

    private void handleKeyRepeat(int key) {
        Set<Object> inputs = eventHandlers.get(Input.class);
        for (Object handler : inputs) {
            ((Input) handler).longPress(key);
        }
    }

    private void handleMouseClick(int button, int x, int y,boolean release) {
        Set<Object> mice = eventHandlers.get(Mouse.class);
        int convertedButton = convertMouseButton(button);
        for (Object handler : mice) {
            Mouse mouse = (Mouse) handler;
            mouse.clickEvent(convertedButton);
            mouse.clickEvent(convertedButton, x, y);
            mouse.isRelease(release);
        }
    }

    private void handleMouseScroll(int delta) {
        Set<Object> mice = eventHandlers.get(Mouse.class);
        for (Object handler : mice) {
            ((Mouse) handler).scrollEvent(delta);
        }
    }

    private void handleMouseMove(int x, int y) {
        Set<Object> mice = eventHandlers.get(Mouse.class);
        for (Object handler : mice) {
            ((Mouse) handler).mouseCoordinates(new org.useless.gui.data.Location(x, y));
        }
    }

    private void handleWindowResize() {
        Set<Object> roots = eventHandlers.get(Root.class);
        for (Object handler : roots) {
            ((Root) handler).SizeEvent();
        }
    }

    private void handleWindowClose() {
        Set<Object> roots = eventHandlers.get(Root.class);
        for (Object handler : roots) {
            ((Root) handler).CloseEvent();
        }
    }

    /**
     * 处理事件队列 - 优化后的版本
     */
    public void processEvents() {
        framesProcessed++;

        // 1. 处理所有键盘事件（无限制）
        processAllInQueue(keyboardQueue);

        // 2. 处理紧急事件（最多50个）
        processQueue(urgentQueue, 50);

        // 3. 处理普通事件（最多30个）
        processQueue(normalQueue, 30);

        // 每100帧清理一次空队列
        if (framesProcessed % 100 == 0) {
            cleanupEmptyQueues();
        }
    }

    private void processAllInQueue(Queue<Runnable> queue) {
        int processed = 0;
        while (!queue.isEmpty()) {
            Runnable task = queue.poll();
            if (task != null) {
                task.run();
                processed++;
                totalEventsProcessed++;
            }
        }
    }

    private void processQueue(Queue<Runnable> queue, int maxCount) {
        int processed = 0;
        while (!queue.isEmpty() && processed < maxCount) {
            Runnable task = queue.poll();
            if (task != null) {
                task.run();
                processed++;
                totalEventsProcessed++;
            }
        }
    }

    private void cleanupEmptyQueues() {
    }

    /**
     * 性能统计
     */
    public String getPerformanceStats() {
        double avgEventsPerFrame = framesProcessed > 0 ?
                (double) totalEventsProcessed / framesProcessed : 0;

        return format("事件统计 - 帧数:%d 总事件:%d 平均事件/帧:%.1f 队列:[K:%d U:%d N:%d]",
                framesProcessed, totalEventsProcessed, avgEventsPerFrame,
                keyboardQueue.size(), urgentQueue.size(), normalQueue.size());
    }

    /**
     * 获取监控状态
     */
    public String getMonitorStatus() {
        return format("监控状态 - 模板:%d 输入处理器:%d 鼠标处理器:%d 根处理器:%d",
                monitoredTemplates.size(),
                eventHandlers.get(Input.class).size(),
                eventHandlers.get(Mouse.class).size(),
                eventHandlers.get(Root.class).size());
    }

    private int convertMouseButton(int glfwButton) {
        return switch (glfwButton) {
            case GLFW_MOUSE_BUTTON_LEFT -> LEFT_CLICK;
            case GLFW_MOUSE_BUTTON_RIGHT -> RIGHT_CLICK;
            case GLFW_MOUSE_BUTTON_MIDDLE -> MIDDLE_CLICK;
            default -> glfwButton;
        };
    }

    public void cleanup() {
        monitoredTemplates.clear();
        eventHandlers.values().forEach(Set::clear);
        keyboardQueue.clear();
        urgentQueue.clear();
        normalQueue.clear();
        initialized = false;
        handle = 0;
    }
}
