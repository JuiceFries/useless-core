package org.useless.uui;

import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.useless.uui.data.AreaDecide;
import org.useless.uui.data.Location;
import org.useless.uui.data.Size;
import org.useless.uui.event.KeyboardEvent;
import org.useless.uui.event.Mouse;
import org.useless.uui.event.MouseEvent;
import org.useless.uui.event.WindowEvent;
import org.useless.uui.template.Control;
import org.useless.uui.template.Template;
import org.useless.uui.uir.FullName;
import org.useless.uui.uir.HandleIsNull;
import org.useless.uui.uir.LayerManager;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_ICONIFIED;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_REPEAT;
import static org.lwjgl.glfw.GLFW.GLFW_SAMPLES;
import static org.lwjgl.glfw.GLFW.GLFW_TRANSPARENT_FRAMEBUFFER;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorWorkarea;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwIconifyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwPostEmptyEvent;
import static org.lwjgl.glfw.GLFW.glfwRestoreWindow;
import static org.lwjgl.glfw.GLFW.glfwSetCharCallback;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwSetWindowCloseCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreate;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.useless.uui.Font.initializeFontSystem;

/**
 * <h2>窗口类</h2>
 * 一个由{@code Lwjgl-OpenGL}实现的一个窗口类,<br>
 * 本身只是实现了一些窗口实现
 * @see GL
 * @see GL11
 * @see Template
 * @apiNote 提供了一些基础的api
 * @implNote 使用gl的API实现 <br> 窗口在其它设备我没试过有问题请参阅源代码
 * @since 0.0.1
 * @version 1.0
 */
public class Window {

    private static final long WINDOW = 2347039197L;


    // 窗口的GL初始化
    static {
        if (!glfwInit()) {
            try {
                glfwSetErrorCallback(((error, description) -> System.err.println("GLFW暴毙代码:" + error + ",临终遗言:" + MemoryUtil.memUTF8(description))));
                throw new RuntimeException("GLFW 初始化失败！看看是不是你自己没配置好。");
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 窗口句柄
     */
    private volatile long handle = 0;

    /**
     * 窗口 [ X , Y ] 坐标
     */
    private volatile Location location = new Location(50,50);

    /**
     * 鼠标位置
     */
    private final Location mouse = new Location();

    /**
     * 窗口大小
     */
    private volatile Size size = new Size(100,100);

    /**
     * 窗口名称
     */
    private volatile String title;

    /**
     * 窗口可见性
     */
    private volatile boolean visible = false;

    /**
     * 绘制命令
     */
    private final List<Consumer<Drawing>> drawingCommands = new ArrayList<>();

    /**
     * 绘图
     */
    private Drawing drawing = Drawing.init();

    /**
     * 自动刷新率
     */
    private int AutomaticRefreshRate = GENERAL_45_FPS;

    /**
     * ui线程
     */
    private final ExecutorService renderThread = Executors.newSingleThreadExecutor();

    /**
     * 窗口监听器
     */
    private final List<WindowEvent> windowEvents = new ArrayList<>();

    /**
     * 窗口关闭标记
     */
    private volatile boolean shouldForceClose = false;

    /**
     * 背景色
     */
    private Color Background = new Color(28, 28, 28);

    /**
     * 是否开启透明背景
     */
    private boolean Transparent = false;

    /**
     * 窗口的排布器
     */
    private Arrange arrange;

    /**
     * 窗口的鼠标事件器
     */
    private MouseEvent mouseEvent;

    /**
     * 键盘输入
     */
    private KeyboardEvent input;

    /**
     * 文本句柄
     */
    private long nvgContext;

    /**
     * 窗口的标题栏显示状态
     */
    private volatile boolean untitled = false;

    private final LayerManager layerManager = new LayerManager();

    /**
     * 无参数构造方法
     * @see Window
     */
    public Window() {
        this(null);
    }

    /**
     * 默认构造方法<br>
     * 带有一个可设置窗口的名称的 [ String ] 参数
     * @param title 窗口名称
     */
    public Window(String title) {
        this.title = title;
        windowEvents.add(new WindowEvent() {});
        renderThread.submit(this::initializeWindow);
    }

    // set ====================

    /**
     * 窗口标题栏名称<br>
     * 设置窗口的名称
     * @param title 窗口标题栏名称
     * @see String
     * @see Window
     */
    public void setTitle(String title) {
        if (handle != 0) {
            glfwSetWindowTitle(handle,title);
        } else {
            this.title = title;
        }
    }

    /**
     * 窗口坐标大小设置<br>
     * 用于设置窗口的坐标和大小
     * @param location 窗口坐标
     * @param size 窗口大小
     * @throws RuntimeException 窗口大小不能小于1!
     */
    public void setBounds(Location location,Size size) {
        if (handle != 0) {
            setLocation(location);
            setSize(size);
        } else {
            this.location = location;
            this.size = size;
        }
    }

    /**
     * 窗口坐标大小设置<br>
     * 用于设置窗口的坐标和大小
     * @param x 横向坐标
     * @param y 竖向坐标
     * @param width 窗口宽度
     * @param height 窗口高度
     * @throws RuntimeException 窗口大小不能小于1!
     */
    public void setBounds(int x,int y,int width,int height) {
        setBounds(new Location(x, y), new Size(width, height));
    }

    /**
     * 设置背景颜色<br>
     * 当传入的参数为 [ null ] 时会将整个画布设为透明，<br>
     * 其他正常
     * @param background 背景色
     * @see Color
     */
    public void setBackground(Color background) {
        if (background == null) {
            Transparent = true;
            Background = null;  // 明确设置为null
        } else {
            Transparent = false;  // 关闭透明模式
            this.Background = new Color(
                    background.getR() * background.getA(),
                    background.getG() * background.getA(),
                    background.getB() * background.getA(),
                    background.getA()
            );
        }
        // 触发重绘
        if (handle != 0) {
            glfwPostEmptyEvent();
        }
    }

    /**
     * 用于设置模板排布<br>
     * 参数为null时为绝对布局其他根据功能自动布置
     * @param arrange 布置
     * @see Arrange
     */
    public void setArrange(Arrange arrange) {
        this.arrange = arrange;
        if (arrange != null) arrange.rearrange(this); // 直接传窗口自己！
    }

    /**
     * 设置窗口坐标<br>
     * 当传入的参数为 [ null ] 时将窗口剧中
     * @param location 起始点坐标
     * @see Location
     * @see IntBuffer
     */
    public void setLocation(Location location) {
        if (location == null) {
            long monitor = glfwGetPrimaryMonitor();
            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer x = stack.mallocInt(1);
                IntBuffer y = stack.mallocInt(1);
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);

                glfwGetMonitorWorkarea(monitor, x, y, w, h);
                this.location = new Location(
                        x.get() + (w.get() - size.width)/2,
                        y.get() + (h.get() - size.height)/2
                );
                if (handle != 0) glfwSetWindowPos(handle, this.location.x, this.location.y);
            }
        } else {
            this.location = location;
            if (handle != 0) glfwSetWindowPos(handle, location.x, location.y);
        }
    }

    /**
     * 设置窗口图标
     * @param icon 图标
     * @see Picture
     */
    public void setIcon(Picture icon) {
        if (handle == 0 || icon == null || !icon.isLoaded()) return;

        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            ByteBuffer imageData = STBImage.stbi_load(icon.getPath(), w, h, comp, 4);
            if (imageData != null) {
                GLFWImage.Buffer images = GLFWImage.malloc(1);
                GLFWImage iconImage = images.get(0);
                iconImage.set(w.get(), h.get(), imageData);

                glfwSetWindowIcon(handle, images);
                STBImage.stbi_image_free(imageData);
            }
        }
    }

    /**
     * 设置标题隐藏状态
     * @param untitled 标题状态
     */
    public void setUntitled(boolean untitled) {
        this.untitled = untitled;  // 移出handle判断！
        if (handle != 0) {
            // 如果窗口已创建，实时更新
            glfwSetWindowAttrib(handle, GLFW_DECORATED, untitled ? GLFW_FALSE : GLFW_TRUE);
        }
    }

    /**
     * 33毫秒约为30刷新率
     */
    public final static int GENERAL_30_FPS = 33;

    /**
     * 22毫秒约为45刷新率
     */
    public final static int GENERAL_45_FPS = 22;

    /**
     * 16毫秒约为60刷新率
     */
    public final static int GENERAL_60_FPS = 16;

    /**
     * 8毫秒约为120刷新率
     */
    public final static int GENERAL_120_FPS = 8;

    /**
     * 设置屏幕的自动刷新频率
     * @param arr 刷新率(毫秒)
     * @throws RuntimeException 刷新率不得小于等于0！
     */
    @FullName(fullName = "setAutomaticRefreshRate")
    public void setARR(@MagicConstant(intValues = {
            GENERAL_30_FPS,GENERAL_45_FPS,GENERAL_60_FPS,GENERAL_120_FPS
    }) int arr) {
        if (arr <= 0) {
            throw new RuntimeException("AutomaticRefreshRate <= 0!");
        } else if (arr >= 9000000) {
            System.out.println("不是你真闲的没事啊？");
        } else {
            AutomaticRefreshRate = arr;
        }
    }

    /**
     * 设置窗口坐标
     * @param x 横向坐标
     * @param y 竖向坐标
     * @see Location
     */
    public void setLocation(int x,int y) {
        setLocation(new Location(x, y));
    }

    /**
     * 设置窗口的显示<br>
     * 用来设置窗口的默认显示
     * [ TRUE ] 显示 [ FALSE ] 不显示
     * @param visible 窗口的显示状态
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (handle != 0) {
            if (visible) glfwShowWindow(handle);
            else glfwHideWindow(handle);
        }
    }



    /**
     * 设置窗口大小<br>
     * 不推荐，因为写的时候要new一个Size对象
     * @param size 窗口大小
     * @see Size
     * @throws RuntimeException 窗口不能小于1!
     */
    public void setSize(@NotNull Size size) {
        if (size.width < 1 || size.height < 1) throw new RuntimeException("窗口不能小于1!");
        if (handle != 0) {
            glfwSetWindowSize(handle,size.width,size.height);
        } else this.size = size;
    }

    /**
     * 设置窗口大小<br>
     * 提供了两个参数用来设置窗口大小
     * @param width 窗口宽度
     * @param height 窗口高度
     * @see Size
     * @throws RuntimeException 窗口不能小于1!
     */
    public void setSize(int width,int height) {
        setSize(new Size(width, height));
    }

    // get ====================

    /**
     * 获取窗口名称
     * @return 名称
     */
    public String getTitle() {
        return title;
    }

    /**
     * 获取窗口位置
     */
    public Location getLocation() {
        return location;
    }

    /**
     * 获取窗口大小
     * @return size
     */
    public Size getSize() {
        return size;
    }

    /**
     * 获取窗口自动刷新率
     * @return 窗口刷新率
     */
    @FullName(fullName = "AutomaticRefreshRate")
    public int getARR() {
        return AutomaticRefreshRate;
    }

    /**
     * 获取窗口显示状态
     * @return 可见性
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * 获取模板<br>
     * 获取窗口的对应的模板
     * @param index 模板索引
     * @return 模板
     */
    public Template getTemplate(int index) {
        return layerManager.getAllTemplates().get(index);
    }

    /**
     * 获取模板<br>
     * 获取模板列表
     * @return 模板
     */
    public List<Template> getAllTemplates() {
        return layerManager.getAllTemplates(); // 委托给LayerManager
    }

    // stm ====================

    /**
     * 进行绘图
     * @param command 绘图命令
     * @see Drawing
     */
    public void drawing(Consumer<Drawing> command) {
        synchronized (drawingCommands) {
            drawingCommands.add(command);
        }
        if (handle != 0) glfwPostEmptyEvent(); // 最好加上这个唤醒事件循环
    }

    /**
     * 最大化窗口
     */
    public void maximize() {
        if (handle != 0) glfwMaximizeWindow(handle);
    }

    /**
     * @return 窗口是否最大化
     */
    public boolean isMaximize() {
        return glfwGetWindowAttrib(handle,GLFW_MAXIMIZED) == GLFW_TRUE;
    }

    /**
     * 最小化窗口
     */
    public void minimize() {
        if (handle != 0) glfwIconifyWindow(handle);
    }

    /**
     * @return 窗口是否最小化
     */
    public boolean isMinimize() {
        return glfwGetWindowAttrib(handle,GLFW_ICONIFIED) == GLFW_TRUE;
    }

    /**
     * 还原
     */
    public void restore() {
        if (handle != 0) glfwRestoreWindow(handle);
    }

    /**
     * 初始化窗口
     */
    private void initializeWindow() {
        glfwWindowHint(GLFW_SAMPLES, 4); // 4倍多重采样
        // 在glfwCreateWindow之前设置窗口提示
        glfwWindowHint(GLFW_DECORATED, untitled ? GLFW_FALSE : GLFW_TRUE);

        // 动态设置透明帧缓冲提示（修复点）
        if (Transparent || (Background != null && Background.getA() == 0.0f)) glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
        else glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_FALSE);// 必须显式禁用，否则之前设置的提示会残留

        handle = glfwCreateWindow(size.width, size.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (handle == 0) throw new RuntimeException("创建 GLFW 窗口失败");

        setLocation(location);
        setVisible(visible);
        initGL();       //初始化GL等设置
        CallbackArea(); // 绑定GLFW回调

        while (!glfwWindowShouldClose(handle)) {
            long frameStart = System.currentTimeMillis();
            glfwPollEvents();
            BackgroundImplementation();
            // 在glClear之后添加：
            nvgBeginFrame(nvgContext, size.width, size.height, 1);
            templateDraw();
            if (shouldForceClose) {glfwDestroyWindow(handle);System.exit(0);}
            nvgEndFrame(nvgContext);
            glfwSwapBuffers(handle);
            RefreshControl(frameStart);
        }

        glfwDestroyWindow(handle);
        handle = 0;
    }

    /**
     * 背景实现
     */
    private void BackgroundImplementation() {
        if (Transparent || (Background != null && Background.getA() == 0.0f)) {
            glClearColor(0, 0, 0, 0);  // 全透明
        } else if (Background != null) {
            glClearColor(Background.getR(), Background.getG(), Background.getB(), Background.getA());
        } else {
            glClearColor(0.11f, 0.11f, 0.11f, 1.0f); // 默认深灰
        }
        glClear(GL_COLOR_BUFFER_BIT);
    }

    /**
     * 初始化区域
     */
    private void initGL() {
        glfwMakeContextCurrent(handle);
        GL.createCapabilities();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        glfwSwapInterval(1);

        nvgContext = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        if (nvgContext == 0) {
            throw new RuntimeException("NVG上下文创建失败，检查OpenGL状态");
        }

        drawing.deliverHandle(handle, size,mouse,nvgContext);

        initializeFontSystem(nvgContext);
    }

    /**
     * 窗口回调区
     */
    private void CallbackArea() {
        // 绑定键盘回调
        glfwSetKeyCallback(handle, (window, key, scancode, action, mods) -> {
            if (input != null) {
                switch (action) {
                    case GLFW_PRESS -> input.pressKey(key);
                    case GLFW_RELEASE -> input.loosenKey(key);
                    case GLFW_REPEAT -> input.longPress(key);
                }
            }
        });

        // 绑定字符输入回调
        glfwSetCharCallback(handle, (window, codepoint) -> {
            if (input != null) {
                input.inputKey((char) codepoint);
            }
        });

        // 窗口拖动回调
        glfwSetWindowPosCallback(handle, (window, x, y) -> windowEvents.forEach(WindowEvent::DragEvent));

        // 窗口尺寸变化回调
        glfwSetWindowSizeCallback(handle, (window, w, h) -> windowEvents.forEach(WindowEvent::SizeEvent));

        // 窗口关闭事件回调
        CloseImplementation();

        initMouseCallback();
    }

    /**
     * 窗口关闭事件处理
     */
    private void CloseImplementation() {
        glfwSetWindowCloseCallback(handle, (window) -> {
            glfwSetWindowShouldClose(window,false);
            if (windowEvents.size() > 1 && custom_close_operation_status) {
                windowEvents.forEach(WindowEvent::CloseEvent);
            }
            else {
                switch (closed) {
                    case DIRECT_CLOSURE -> {
                        glfwDestroyWindow(window);
                        shouldForceClose = true;
                        handle = 0;
                        System.exit(0);
                    }
                    case CLOSE_AFTER_CLEANING ->  {
                        glfwDestroyWindow(window);
                        handle = 0;
                        drawingCommands.clear();
                        drawing = null;
                        windowEvents.clear();
                        glfwSetWindowShouldClose(window, true);
                        shouldForceClose = true;
                        System.exit(0);
                    }
                    case CLOSES_ONLY ->  {
                        glfwSetWindowShouldClose(window,true);
                        glfwDestroyWindow(window);
                        handle = 0;
                    }
                    case NOT_CLOSED -> {

                    }
                }
            }
        });
    }

    /**
     * 刷新控制
     * @param frameStart 刷新
     */
    private void RefreshControl(long frameStart) {
        long frameTime = System.currentTimeMillis() - frameStart;
        long sleepTime = AutomaticRefreshRate - frameTime;
        if (sleepTime > 0) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 渲染UI
     */
    private void templateDraw() {
        if (handle != 0) {
            // 只需要渲染层级管理器
            layerManager.logoutLayer(drawing);
            for (Consumer<Drawing> cmd : drawingCommands) {
                cmd.accept(drawing);
            }
        }
    }

    /**
     * 用来获取鼠标坐标
     */
    private void initMouseCallback() {
        glfwSetCursorPosCallback(handle, (window, x, y) -> {
            mouse.x = (int) x;
            mouse.y = (int) y;
        });
        // 回调处理鼠标点击
        glfwSetMouseButtonCallback(handle, (window, button, action, mods) -> {
            int mouseButton = convertGlfwButton(button);
            if (action == GLFW_PRESS) {
                layerManager.
                        getAllTemplates().
                        stream()
                        .filter(template -> template instanceof Control)
                        .filter(template -> AreaDecide.isMouseOver(drawing, template)).max(Comparator.comparingInt(Template::getZIndex))
                        .ifPresent(control -> {
                            List<MouseEvent> events = control.getMouseEventList();
                            if (events != null) { // 防止NPE
                                int convertedButton = convertGlfwButton(button); // 用你下面写好的转换方法
                                events.forEach(event -> event.clickEvent(convertedButton));
                            }
                        });
            }

            if (mouseEvent != null) {
                if (action == GLFW_PRESS) {
                    mouseEvent.clickEvent(mouseButton);
                }
            }
        });
    }

    /**
     * 对选定的区域进行重绘
     * @param x 起始点
     * @param y 起始点
     * @param width 宽
     * @param height 高
     * @deprecated 没啥用窗口默认自动完全刷新一次界面
     */
    @Deprecated
    public void refresh(int x, int y, int width, int height) {
        if (handle == 0) {
            System.out.println("Window handle is not initialized!");
            return;
        }

        // 保存当前的剪辑状态
        GL11.glPushAttrib(GL11.GL_SCISSOR_BIT);

        // 设置剪辑区域
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, size.height - (y + height), width, height);

        // 触发重绘
        glClear(GL_COLOR_BUFFER_BIT);
        for (Consumer<Drawing> cmd : drawingCommands) cmd.accept(drawing);

        // 恢复剪辑状态
        GL11.glPopAttrib();
    }

    /**
     * UI渲染线程
     * @param task 运行
     * @deprecated 我忘了这个东西干啥用的了
     */
    @Deprecated(since = "0.0.3")
    private void runOnRenderThread(Runnable task) {
        renderThread.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                System.err.println("setUntitled操作失败: " + e.getMessage());
            }
        });
    }

    /**
     * 刷新界面绘图
     * @deprecated 没啥用窗口默认自动完全刷新一次界面
     */
    @Deprecated
    public void refresh() {
        if (handle == 0) {
            System.out.println("窗口句柄未初始化!");
            return;
        }

        // 触发全屏重绘
        glClear(GL_COLOR_BUFFER_BIT);
    }

    /**
     * 注册模板<br>
     * 用来将模板注册到窗口渲染中
     * @see Template
     */
    public void enrolled(Template template) {
        layerManager.enrolledLayer(template);
        // 新增：注册后自动布局
        if (arrange != null) arrange.rearrange(this);
    }

    /**
     * 注销模板<br>
     * 用来将模板从窗口的渲染中注销
     */
    public void logout(Template template) {
        layerManager.removeLayer(template); // 调用按对象删除的方法
    }

    /// 判断自定义关闭操作启用状态
    private volatile boolean custom_close_operation_status = false;

    /// 直接关闭整个程序
    public static final int DIRECT_CLOSURE = 1;
    /// 清理后关闭程序
    public static final int CLOSE_AFTER_CLEANING = 2;
    /// 只关闭窗口
    public static final int CLOSES_ONLY = 3;
    /// 不关闭窗口
    public static final int NOT_CLOSED = 4;

    private int closed = CLOSE_AFTER_CLEANING;
    /**
     * 设置窗口的默认关闭行为<br>
     * 用来设置窗口的默认的关闭行为，<br>
     * 若使用了自定义关闭方法默认关闭方法将自动关闭<br>
     * 我也觉得很怪但是不知道怎么解决
     * @param closed 基础关闭行为
     * @see WindowEvent
     * @throws IllegalArgumentException 不会传参就别传要么传已经写好的要么别用
     * @since 0.0.1
     */
    @FullName(fullName = "setGeneralShutdownAction")
    public final void setGSA(@MagicConstant(intValues = {
            DIRECT_CLOSURE,CLOSE_AFTER_CLEANING,CLOSES_ONLY,NOT_CLOSED}) int closed
    ) {
        //意思写进错误输出了
        if (closed != DIRECT_CLOSURE && closed != CLOSE_AFTER_CLEANING && closed != CLOSES_ONLY && closed != NOT_CLOSED) {
            throw new IllegalArgumentException("不会传参就别传要么传已经写好的要么别用");
        }
        this.closed = closed;
    }

    /**
     * 注册窗口事件器<br>
     * 用来设置窗口的拖动、尺寸、关闭事件
     * @param we 事件
     * @see WindowEvent
     */
    public void enrolledWindowEvent(WindowEvent we) {
        if (we != null) {
            windowEvents.add(we);
            this.custom_close_operation_status = true;
        }
    }

    /**
     * 用以注册窗口键盘处理
     * @param input 输入事件
     * @see KeyboardEvent
     */
    public void enrolledInputEvent(KeyboardEvent input) {
        if (input != null) {
            this.input = input;
        }
    }

    /**
     * 注册鼠标事件
     * @param mouse 事件
     * @see MouseEvent
     */
    public void enrolledMouse(MouseEvent mouse) {
        if (mouse != null) {
            this.mouseEvent = mouse;
        }
    }

    /**
     * 添加GLFW按钮转换方法
     */
    private int convertGlfwButton(int glfwButton) {
        return switch (glfwButton) {
            case GLFW_MOUSE_BUTTON_LEFT -> Mouse.LEFT_CLICK;
            case GLFW_MOUSE_BUTTON_RIGHT -> Mouse.RIGHT_CLICK;
            case GLFW_MOUSE_BUTTON_MIDDLE -> Mouse.MIDDLE_CLICK;
            default -> glfwButton; // 其他按钮原样返回
        };
    }


    /**
     * 判断窗口是否为空，用于获取窗口句柄
     * @return 窗口是否为空
     * @throws Exception 闲的
     */
    @Deprecated(since = "0.0.3")
    public final @NotNull HandleIsNull isNull
    (@MagicConstant(intValues = HandleIsNull.NULL) long IS) throws Exception
    {
        if (IS == HandleIsNull.NULL) return new HandleIsNull(handle,nvgContext);
        else throw new Exception("命令错误");
    }

}
