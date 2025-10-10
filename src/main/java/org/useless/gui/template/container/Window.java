package org.useless.gui.template.container;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;
import org.useless.gui.arrange.Arrange;
import org.useless.gui.data.Color;
import org.useless.gui.data.Location;
import org.useless.gui.data.Size;
import org.useless.gui.drawing.Drawing;
import org.useless.gui.event.EventManager;
import org.useless.gui.event.Input;
import org.useless.gui.event.Mouse;
import org.useless.gui.event.Root;
import org.useless.gui.event.RootEvent;
import org.useless.gui.picture.Picture;
import org.useless.gui.template.Template;
import org.useless.gui.template.agreement.RootAgreement;
import org.useless.gui.template.Container;
import org.useless.gui.uir.DrawLayer;
import org.useless.annotation.Fixed;
import org.useless.gui.data.Handle;
import org.useless.gui.exception.IllegalComponentException;
import org.useless.gui.uir.Layer;
import org.useless.gui.uir.LayerManager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import org.useless.annotation.FullName;

import static java.lang.Math.max;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.err;
import static java.lang.System.out;
import static java.lang.Thread.currentThread;
import static java.util.Collections.singletonList;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static org.lwjgl.glfw.GLFW.GLFW_DECORATED;
import static org.lwjgl.glfw.GLFW.GLFW_FALSE;
import static org.lwjgl.glfw.GLFW.GLFW_FLOATING;
import static org.lwjgl.glfw.GLFW.GLFW_FOCUS_ON_SHOW;
import static org.lwjgl.glfw.GLFW.GLFW_ICONIFIED;
import static org.lwjgl.glfw.GLFW.GLFW_MAXIMIZED;
import static org.lwjgl.glfw.GLFW.GLFW_TRANSPARENT_FRAMEBUFFER;
import static org.lwjgl.glfw.GLFW.GLFW_TRUE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetMonitorWorkarea;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwGetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwGetWindowContentScale;
import static org.lwjgl.glfw.GLFW.glfwHideWindow;
import static org.lwjgl.glfw.GLFW.glfwIconifyWindow;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwMaximizeWindow;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwRestoreWindow;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowAttrib;
import static org.lwjgl.glfw.GLFW.glfwSetWindowFocusCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIcon;
import static org.lwjgl.glfw.GLFW.glfwSetWindowIconifyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowMaximizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSize;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.glfw.GLFWImage.malloc;
import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;
import static org.lwjgl.nanovg.NanoVGGL3.nvgCreate;
import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopAttrib;
import static org.lwjgl.opengl.GL11.glPushAttrib;
import static org.lwjgl.opengl.GL11.glScissor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.memAlloc;
import static org.useless.gui.drawing.DrawImpl.NanoVG;
import static org.useless.gui.drawing.Drawing.init;
import static org.useless.gui.event.EventManager.getInstance;
import static org.useless.gui.uir.InitPhase.triggerAfterInitialization;
import static org.useless.gui.uir.InitPhase.triggerBeforeInitialization;
import static org.useless.gui.uir.UIManager.applyTo;
import static org.useless.gui.uir.UIManager.default32xIcon;
import static org.useless.gui.uir.InitPhase.executeFrameTask;

/**
 * <h2>窗口</h2>
 * 一个由{@code Lwjgl-OpenGL | Lwjgl-NanoVG }实现的一个窗口类,<br>
 * 本身只是实现了一些窗口实现
 * @apiNote 提供了一些基础的api
 * @implNote 使用gl的API实现的窗口管理 <br> 窗口在其它设备我没试过有问题请参阅源代码
 * @since 0.0.1
 * @version 1.1-beta
 */
@Fixed(continuous = "~3-Beta")
public class Window implements Container, RootAgreement {

    // 窗口的GL初始化
    static {
        if (!glfwInit()) {
            try {
                glfwSetErrorCallback(((error, description) -> err.println("GLFW暴毙代码:" + error + ",临终遗言:" + MemoryUtil.memUTF8(description))));
                throw new RuntimeException("GLFW 初始化失败！看看是不是你自己没配置好。");
            } catch (RuntimeException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /// GL窗口管理句柄
    private long glHandle = 0;

    /// VG窗口绘图句柄
    private long vgHandle;

    /// 窗口标题
    private String title;

    /// 标题可见性
    private boolean titleVisibility = true;

    /// 窗口坐标
    private Location location = new Location();

    /// 窗口大小
    private Size size = new Size(50,50);

    /// 窗口可见性
    private boolean visible = false;

    /**
     * 背景色
     */
    private Color background = new Color(28, 28, 28,1f);

    /// 透明模式
    private boolean transparent = true;

    /**
     * ui线程
     */
    @FullName(fullName = "Useless Thread Scheduling")
    protected final ExecutorService UTS = newSingleThreadExecutor();

    /// 事件管理器
    private EventManager eventManager;

    /// 直接关闭整个程序
    public static final int DIRECT_CLOSURE = 1;
    /// 清理后关闭程序
    public static final int CLOSE_AFTER_CLEANING = 2;
    /// 只关闭窗口
    public static final int CLOSES_ONLY = 3;
    /// 不关闭窗口
    public static final int NOT_CLOSED = 4;

    /// 默认关闭行为
    private int closed = CLOSE_AFTER_CLEANING;

    /// 默认关闭事件事件
    private Root root = new RootEvent(this,closed);

    /// 绘图接口
    private Drawing drawing = init(NanoVG);

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

    /// 当前自动刷新率
    private int AutomaticRefreshRate = GENERAL_45_FPS;

    /// 窗口所处层级
    @SuppressWarnings("FieldCanBeLocal")
    private final int zIndex = 0 ;

    /// 层管理器
    private final LayerManager layerManager = new LayerManager(this);

    private int currentDrawingZIndex = 1; // 从绘图层最低开始

    // 窗口状态回调存储
    private Runnable minimizeCallback;
    private Consumer<Boolean> maximizeListener;
    private Consumer<Boolean> focusListener;

    private final List<Input> inputList = new ArrayList<>();
    private final List<Mouse> mouseList = new ArrayList<>();
    private Picture icon = default32xIcon;

    private Arrange arrange;


    /**
     * 无参构造方法
     */
    public Window() {
        this(null);
    }

    /**
     * 带有参数的构造方法<br>
     * 自己理解去
     * @param title 标题
     */
    public Window(String title) {
        this.title = title;
        UTS.submit(this::initWindow);
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
        if (glHandle != 0) {
            glfwSetWindowTitle(glHandle,title);
        } else {
            this.title = title;
        }
    }

    /**
     * 设置窗口标题栏可见性
     * @param titleVisibility 标题可见性
     */
    public void setTitleVisibility(boolean titleVisibility) {
        this.titleVisibility = titleVisibility;
    }

    /**
     * 设置窗口坐标<br>
     * 当传入的参数为 [ null ] 时将窗口剧中
     * @param location 起始点坐标
     * @see Location
     * @see IntBuffer
     */
    @Override
    public void setLocation(Location location) {
        if (location == null) {
            long monitor = glfwGetPrimaryMonitor();
            try (MemoryStack stack = stackPush()) {
                IntBuffer x = stack.mallocInt(1);
                IntBuffer y = stack.mallocInt(1);
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);

                glfwGetMonitorWorkarea(monitor, x, y, w, h);
                this.location = new Location(
                        x.get() + (w.get() - size.width)/2,
                        y.get() + (h.get() - size.height)/2
                );
                if (glHandle != 0) glfwSetWindowPos(glHandle, this.location.x, this.location.y);
            }
        } else {
            this.location = location;
            if (glHandle != 0) glfwSetWindowPos(glHandle, location.x, location.y);
        }
    }

    @Override
    public void setLocation(int x, int y) {
        setLocation(new Location(x, y));
    }

    /**
     * 设置窗口大小<br>
     * 不推荐，因为写的时候要new一个Size对象
     * @param size 窗口大小
     * @see Size
     * @throws RuntimeException 窗口不能小于1!
     */
    @Override
    public void setSize(@NotNull Size size) {
        if (size.width < 1 || size.height < 1) throw new RuntimeException("窗口不能小于1!");
        if (glHandle != 0) {
            glfwSetWindowSize(glHandle,size.width,size.height);
        } else this.size = size;
    }

    @Override
    public void setSize(int width, int height) {
        setSize(new Size(width, height));
    }

    @Override
    public void setBounds(Location location,Size size) {
        setLocation(location);
        setSize(size);
    }

    /**
     * 设置透明模式<br>
     * 允许设置窗口的透明模式默认为 {@code true}
     * @param transparent 透明模式
     */
    public void setTransparent(boolean transparent) {
        this.transparent = transparent;
    }

    @Override
    public void setBounds(int x,int y,int width,int height) {
        setBounds(new Location(x, y),new Size(width, height));
    }

    /**
     * 设置窗口的显示<br>
     * 用来设置窗口的默认显示
     * {@code TRUE } 显示 {@code FALSE } 不显示
     * @param visible 窗口的显示状态
     */
    @Override
    public void setVisible(boolean visible) {
        this.visible = visible;
        if (glHandle != 0) {
            if (visible) glfwShowWindow(glHandle);
            else glfwHideWindow(glHandle);
        }
    }

    @Deprecated
    @Override
    public void setArrange(Arrange arrange) {
        this.arrange = arrange;
        if (arrange != null) {
            // 直接操作图层管理器里的真实对象
            List<Template> templates = layerManager.getSortedLayers().stream()
                    .filter(layer -> layer instanceof Template)
                    .map(layer -> (Template) layer)
                    .toList();

            if (!templates.isEmpty()) {
                arrange.rearrange(this);
            }
        }
    }

    /**
     * 设置单个窗口图标
     * @param icon 图标图片
     */
    public void setIcon(Picture icon) {
        if (icon == null) {
            err.println("图标不能为null，使用默认图标");
            this.icon = default32xIcon;
            return;
        }

        if (!icon.isLoaded()) {
            err.println("图标未加载，尝试加载...");
            try {
                icon.load();
            } catch (Exception e) {
                err.println("图标加载失败: " + e.getMessage());
                this.icon = default32xIcon;
                return;
            }
        }

        if (icon.getImageWidth() <= 0 || icon.getImageHeight() <= 0) {
            err.println("图标尺寸无效: " + icon.getImageWidth() + "x" + icon.getImageHeight());
            this.icon = default32xIcon;
            return;
        }

        this.icon = icon;
        out.println("设置单个图标: " + icon.getPath() + " [" +
                icon.getImageWidth() + "x" + icon.getImageHeight() + "]");

        // 应用到GLFW窗口（包装成数组调用多图标版本）
        applyIconsToWindow(singletonList(icon));
    }


    /**
     * 设置窗口图标（自动选择最合适尺寸）
     * @param icons 图标数组，支持多个尺寸
     */
    public void setIcon(Picture... icons) {
        if (icons == null || icons.length == 0) {
            this.icon = default32xIcon;
            out.println("图标数组为空，使用默认图标");
            return;
        }

        // 过滤有效图标
        List<Picture> validIcons = new ArrayList<>();
        for (Picture icon : icons) {
            if (icon != null && icon.isLoaded() &&
                    icon.getImageWidth() > 0 && icon.getImageHeight() > 0) {
                validIcons.add(icon);
            }
        }

        if (validIcons.isEmpty()) {
            err.println("没有有效的图标，使用默认图标");
            this.icon = default32xIcon;
            return;
        }

        // 选择最佳图标
        this.icon = findBestIcon(validIcons);
        // 应用到GLFW窗口
        applyIconsToWindow(validIcons);
    }

    /**
     * 找到最合适的图标尺寸
     */
    private Picture findBestIcon(List<Picture> icons) {
        // 按优先级选择：32 > 16 > 64 > 其他（按面积最接近1024）
        Picture bestIcon = icons.getFirst();
        int bestScore = -1;

        for (Picture icon : icons) {
            int width = icon.getImageWidth();
            int height = icon.getImageHeight();
            int score = 0;

            // 尺寸优先级评分
            if (width == 32 && height == 32) score = 100;
            else if (width == 16 && height == 16) score = 90;
            else if (width == 64 && height == 64) score = 80;
            else {
                // 其他尺寸按接近32x32的程度评分
                int areaDiff = Math.abs(width * height - 1024); // 1024 = 32x32
                score = max(0, 70 - areaDiff / 100);
            }

            if (score > bestScore) {
                bestScore = score;
                bestIcon = icon;
            }
        }

        return bestIcon;
    }

    /**
     * 将图标应用到GLFW窗口
     */
    private void applyIconsToWindow(List<Picture> icons) {
        if (glHandle == 0) {
            err.println("窗口句柄未初始化，无法设置图标");
            return;
        }

        try (MemoryStack stack = stackPush()) {
            // 创建GLFWImage缓冲区
            Buffer images = malloc(icons.size(), stack);

            for (int i = 0; i < icons.size(); i++) {
                Picture icon = icons.get(i);
                ByteBuffer imageData = icon.getImageData();

                if (imageData != null) {
                    // 确保数据是RGBA格式且正确对齐
                    ByteBuffer rgbaData = convertToRGBA(icon, imageData);
                    if (rgbaData != null) {
                        // 设置GLFW图像数据
                        GLFWImage glfwImage = images.get(i);
                        glfwImage.set(icon.getImageWidth(), icon.getImageHeight(), rgbaData);
                    }
                }
            }

            // 应用图标到窗口
            glfwSetWindowIcon(glHandle, images);

        } catch (Exception e) {
            err.println("图标设置失败: " + e.getMessage());
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }
    }

    /**
     * 转换图片数据为RGBA格式
     */
    private ByteBuffer convertToRGBA(Picture icon, ByteBuffer originalData) {
        try {
            int width = icon.getImageWidth();
            int height = icon.getImageHeight();
            int channels = icon.getChannels();

            // 如果已经是RGBA格式，直接返回
            if (channels == 4) {
                // 创建副本，避免原始数据被修改
                ByteBuffer copy = memAlloc(originalData.remaining());
                int originalPos = originalData.position();
                copy.put(originalData);
                copy.flip();
                originalData.position(originalPos);
                return copy;
            }

            // 转换其他格式到RGBA
            int srcPixelSize = channels;
            int dstPixelSize = 4;
            int srcRowStride = width * srcPixelSize;
            int dstRowStride = width * dstPixelSize;

            ByteBuffer rgbaBuffer = memAlloc(height * dstRowStride);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int srcPos = y * srcRowStride + x * srcPixelSize;
                    int dstPos = y * dstRowStride + x * dstPixelSize;

                    // 读取原始像素
                    byte r = 0, g = 0, b = 0, a = (byte)0xFF;

                    if (channels >= 1) r = originalData.get(srcPos);
                    if (channels >= 2) g = originalData.get(srcPos + 1);
                    if (channels >= 3) b = originalData.get(srcPos + 2);
                    if (channels >= 4) a = originalData.get(srcPos + 3);

                    // 写入RGBA
                    rgbaBuffer.put(dstPos, r);
                    rgbaBuffer.put(dstPos + 1, g);
                    rgbaBuffer.put(dstPos + 2, b);
                    rgbaBuffer.put(dstPos + 3, a);
                }
            }

            return rgbaBuffer;

        } catch (Exception e) {
            err.println("图片格式转换失败: " + e.getMessage());
            return null;
        }
    }

    /**
     * 清除窗口图标
     */
    public void clearIcon() {
        this.icon = default32xIcon;
        if (glHandle != 0) {
            glfwSetWindowIcon(glHandle, null); // 传递null清除图标
            out.println("已清除窗口图标");
        }
    }

    @Override
    public void setBackground(Color background) {
        this.background = background;
    }

    // 窗口层级
    public void setAlwaysOnTop(boolean alwaysOnTop) {
        glfwSetWindowAttrib(glHandle, GLFW_FLOATING, alwaysOnTop ? GLFW_TRUE : GLFW_FALSE);
    }

    // 事件拦截
    public void setBlockEvents(boolean blockEvents) {
        glfwSetWindowAttrib(glHandle, GLFW_FOCUS_ON_SHOW, blockEvents ? GLFW_FALSE : GLFW_TRUE);
    }

    /**
     * 设置窗口的默认关闭行为<br>
     * 用来设置窗口的默认的关闭行为，<br>
     * 若使用了自定义关闭方法默认关闭方法将自动关闭<br>
     * 我也觉得很怪但是不知道怎么解决
     * @param closed 基础关闭行为
     * @throws IllegalArgumentException 不会传参就别传要么传已经写好的要么别用
     * @since 0.0.1
     */
    public final void setGeneralShutdownAction(@MagicConstant(intValues = {
            DIRECT_CLOSURE,CLOSE_AFTER_CLEANING,CLOSES_ONLY,NOT_CLOSED}) int closed
    ) {
        //意思写进错误输出了
        if (closed != DIRECT_CLOSURE && closed != CLOSE_AFTER_CLEANING && closed != CLOSES_ONLY && closed != NOT_CLOSED) {
            throw new IllegalArgumentException("不会传参就别传要么传已经写好的要么别用");
        }
        this.closed = closed;
    }

    /**
     * 设置屏幕的自动刷新频率
     * @param arr 刷新率(毫秒)
     * @throws RuntimeException 刷新率不得小于等于0！
     */
    public void setAutomaticRefreshRate(@MagicConstant(intValues = {
            GENERAL_30_FPS,GENERAL_45_FPS,GENERAL_60_FPS,GENERAL_120_FPS
    }) int arr) {
        if (arr <= 0) {
            throw new IllegalComponentException("AutomaticRefreshRate <= 0!");
        } else if (arr >= 9000000) {
            out.println("不是你真闲的没事啊？");
        } else {
            AutomaticRefreshRate = arr;
        }
    }

    /**
     * 设置窗口层级<br>
     * 这个方法没有任何用
     * @param zIndex 层
     * @throws IllegalStateException 窗口层级只能为零!
     * @deprecated 但是窗口始终为0
     */
    @Deprecated(since = "0.0.3",forRemoval = true)
    @Override public void setZIndex(int zIndex) {
        if (zIndex != 0) throw new IllegalComponentException("窗口层级只能为零!");
    }

    // get ====================

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public int getX() {
        return location.x;
    }

    @Override
    public int getY() {
        return location.y;
    }

    @Override
    public Size getSize() {
        return size;
    }

    @Override
    public int getWidth() {
        return size.width;
    }

    @Override
    public int getHeight() {
        return size.height;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    /**
     * @return 获取透明状态
     */
    public boolean isTransparent() {
        return transparent;
    }

    /**
     * @return 获取窗口的标题可见性
     */
    public boolean isTitleVisibility() {
        return titleVisibility;
    }

    @Override
    public boolean isRootContainer() {
        return true;
    }

    @Override
    public List<Template> getTemplateList() {
        // 从LayerManager中过滤出Template类型的图层
        return layerManager.getSortedLayers().stream()
                .filter(layer -> layer instanceof Template)
                .map(layer -> (Template) layer)
                .collect(Collectors.toList());
    }

    @Override
    public Template getTemplateList(int index) {
        List<Template> templates = getTemplateList();
        if (index < 0 || index >= templates.size()) {
            throw new IndexOutOfBoundsException("索引越界: " + index + ", 模板数量: " + templates.size());
        }
        return templates.get(index);
    }

    @Override
    public Color getBackground() {
        return background;
    }

    // DPI自适应
    public float getContentScale() {
        try (MemoryStack stack = stackPush()) {
            FloatBuffer xScale = stack.mallocFloat(1);
            FloatBuffer yScale = stack.mallocFloat(1);
            glfwGetWindowContentScale(glHandle, xScale, yScale);
            return (xScale.get(0) + yScale.get(0)) / 2.0f;
        }
    }

    @Deprecated(since = "0.0.1",forRemoval = true)
    public Drawing getDrawing() {
        return drawing;
    }

    @Override
    public int getZIndex() {
        return zIndex;
    }

    @Override
    public Drawing getDraw() {
        return drawing;
    }

    public Picture getIcon() {
        return icon;
    }

    // stm ====================

    @Override
    public void draw(Drawing drawing) {
        this.drawing = drawing;
    }

    @Override
    public void draw(Consumer<Drawing> draw) {
        // 自动分配绘图层zIndex，避免冲突
        int zIndex = currentDrawingZIndex++;
        if (currentDrawingZIndex > 299) { // 绘图层上限
            currentDrawingZIndex = 1; // 循环使用
        }
        layerManager.addLayer(new DrawLayer(zIndex, draw));
    }


    @Override
    public void add(Template template) {
        if (template == null) throw new NullPointerException("所添加的容器不能为空!");
        else {
            if (template instanceof Container) {
                if (((Container) template).isRootContainer()) {
                    throw new IllegalComponentException("根容器不能被添加到任何容器上!");
                } else {
                    layerManager.addLayer(template);
                }
            } else {
                layerManager.addLayer(template);
            }
        }
        applyTo(template);
    }

    @Override
    public void add(Template @NotNull ... template) {
        for (Template tmd : template) this.add(tmd);
    }

    @Override
    public void remove(Template template) {
        layerManager.removeLayer(template);
    }

    @Override
    public void remove(Template @NotNull ... templates) {
        for (Template template : templates) this.remove(template);
    }

    @Override
    public void addInputEvent(Input input) {
        inputList.add(input);
    }

    @Override
    public void addMouseEvent(Mouse mouse) {
        mouseList.add(mouse);
    }

    public void addRootEvent(Root root) {
        if (root instanceof RootEvent weh) {
            if (weh.getWindow() == null) {
                weh.setWindow(this); // 确保绑定窗口实例
            }
        }
        this.root = root;
    }

    public void removeInputEvent(Input input) {
        inputList.remove(input);
    }

    public void removeMouseEvent(Mouse mouse) {
        inputList.remove(mouse);
    }

    // 图层管理相关方法
    public void addLayer(Layer layer) {
        layerManager.addLayer(layer);
    }

    public void removeLayer(Layer layer) {
        layerManager.removeLayer(layer);
    }

    public List<Layer> getSortedLayers() {
        return layerManager.getSortedLayers();
    }

    public int getLayerCount() {
        return layerManager.getLayerCount();
    }

    public boolean containsLayer(Layer layer) {
        return layerManager.containsLayer(layer);
    }

    public List<Layer> getLayersByZIndex(int zIndex) {
        return layerManager.getLayersByZIndex(zIndex);
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    @Override
    public List<Input> getInputList() {
        return inputList;
    }

    @Override
    public List<Mouse> getMouseList() {
        return mouseList;
    }

    @Override
    public Root getRoot() {
        return root;
    }

    /** 关闭和清理 */
    // 线程池清理
    public void dispose() {
        if (glHandle != 0) {
            glfwDestroyWindow(glHandle);
            glHandle = 0;
        }
        UTS.shutdown(); // 加上这行，不然线程池变僵尸
    }


    /**
     * 最大化窗口
     */
    public void maximize() {
        if (glHandle != 0) glfwMaximizeWindow(glHandle);
    }

    /**
     * @return 窗口是否最大化
     */
    public boolean isMaximize() {
        return glfwGetWindowAttrib(glHandle,GLFW_MAXIMIZED) == GLFW_TRUE;
    }

    /**
     * 最小化窗口
     */
    public void minimize() {
        if (glHandle != 0) glfwIconifyWindow(glHandle);
    }

    /**
     * @return 窗口是否最小化
     */
    public boolean isMinimize() {
        return glfwGetWindowAttrib(glHandle,GLFW_ICONIFIED) == GLFW_TRUE;
    }

    /**
     * 还原
     */
    public void restore() {
        if (glHandle != 0) glfwRestoreWindow(glHandle);
    }

    /**
     * 选择需要获取的句柄
     * @return 对应句柄
     */
    public Handle selectHandle() {
        return new Handle(glHandle,vgHandle);
    }

    // 窗口状态回调

    // 实现窗口最小化回调
    public void onMinimize(Runnable callback) {
        this.minimizeCallback = callback;
        // 注册GLFW最小化回调
        glfwSetWindowIconifyCallback(glHandle, (window, iconified) -> {
            if (iconified && minimizeCallback != null) {
                minimizeCallback.run();
            }
        });
    }

    // 实现窗口最大化回调
    public void onMaximize(Consumer<Boolean> listener) {
        this.maximizeListener = listener;
        // 注册GLFW最大化回调
        glfwSetWindowMaximizeCallback(glHandle, (window, maximized) -> {
            if (maximizeListener != null) {
                maximizeListener.accept(maximized);
            }
        });
    }

    // 实现窗口焦点变化回调
    public void onFocusChange(Consumer<Boolean> focusListener) {
        this.focusListener = focusListener;
        // 注册GLFW焦点回调
        glfwSetWindowFocusCallback(glHandle, (window, focused) -> {
            if (focusListener != null) {
                focusListener.accept(focused);
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
    @Deprecated(since = "0.0.3")
    public void refresh(int x, int y, int width, int height) {
        if (glHandle == 0) {
            out.println("Window handle is not initialized!");
            return;
        }

        // 保存当前的剪辑状态
        glPushAttrib(GL11.GL_SCISSOR_BIT);

        // 设置剪辑区域
        glEnable(GL11.GL_SCISSOR_TEST);
        glScissor(x, size.height - (y + height), width, height);

        // 渲染所有图层（1-999层）
        layerManager.renderLayers(drawing);
        // 触发重绘
        glClear(GL_COLOR_BUFFER_BIT);

        // 恢复剪辑状态
        glPopAttrib();
    }

    /**
     * 刷新界面绘图
     * @deprecated 没啥用窗口默认自动完全刷新一次界面
     */
    @Deprecated(since = "0.0.3")
    public void refresh() {
        if (glHandle == 0) {
            out.println("窗口句柄未初始化!");
            return;
        }
        // 渲染所有图层（1-999层）
        layerManager.renderLayers(drawing);
        // 触发全屏重绘
        glClear(GL_COLOR_BUFFER_BIT);
    }

    /**
     * 初始化窗口
     */
    protected void initWindow() {
        beforeInitialization();
        glHandle = glfwCreateWindow(size.width, size.height, this.title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (glHandle == 0) throw new RuntimeException("创建 GLFW 窗口失败");
        afterInitialization();
        fieldCallback();
        renderLoop();
    }

    protected void fieldCallback() {
        glfwSetWindowPosCallback(glHandle,(window,x,y) -> this.location = new Location(x,y));

        // 最小化回调
        if (minimizeCallback != null) {
            glfwSetWindowIconifyCallback(glHandle, (window, iconified) -> {
                if (iconified) {
                    minimizeCallback.run();
                }
            });
        }

        // 最大化回调
        if (maximizeListener != null) {
            glfwSetWindowMaximizeCallback(glHandle, (window, maximized) -> {
                maximizeListener.accept(maximized);
            });
        }

        // 焦点回调
        if (focusListener != null) {
            glfwSetWindowFocusCallback(glHandle, (window, focused) -> {
                focusListener.accept(focused);
            });
        }

    }

    /**
     * 渲染循环
     */
    protected void renderLoop() {
        coordinateSetting(); // 初始化坐标系统
        // 获取显示器刷新率上限
        long monitor = glfwGetPrimaryMonitor();
        GLFWVidMode vidMode = glfwGetVideoMode(monitor);
        int maxFPS = 0;
        if (vidMode != null) {
            maxFPS = vidMode.refreshRate();
        }
        int minFrameTime = 1000 / maxFPS; // 转换成最小帧时间
        while (!glfwWindowShouldClose(glHandle)) {
            long frameStart = currentTimeMillis();
            glfwPollEvents();
            if (eventManager != null) {
                eventManager.processEvents();
            }
            backgroundImpl();
            // NanoVG帧开始
            nvgBeginFrame(vgHandle, size.width, size.height, 1f);

            // 先渲染图层管理器中的图层（1-999层）
            layerManager.renderLayers(drawing);

            // NanoVG帧结束
            nvgEndFrame(vgHandle);

            executeFrameTask();

            glfwSwapBuffers(glHandle);
            // 真正的帧率限制在这里
            long frameTime = currentTimeMillis() - frameStart;
            long sleepTime = max(AutomaticRefreshRate, minFrameTime) - frameTime;
            if (sleepTime > 0) {
                try {
                    //noinspection BusyWait
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    currentThread().interrupt();
                    break;
                }
            }
        }
        glfwDestroyWindow(glHandle);
        glHandle = 0;
    }

    /**
     * 初始化 {@code lwjgl }相关设置
     */
    protected void initSet() {
        glfwMakeContextCurrent(glHandle);
        createCapabilities();

        // 在 Window 类的 initSet 方法中添加
        glEnable(GL11.GL_BLEND);
        glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA);

        glfwSwapInterval(1);

        vgHandle = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        if (vgHandle == 0) {
            throw new RuntimeException("NVG上下文创建失败，检查OpenGL状态");
        }

        this.eventManager = getInstance();
        drawing.deliver(new Handle(glHandle,vgHandle),eventManager);
        eventManager.monitorTemplate(this); // 监控窗口自己
    }

    /**
     * 背景渲染
     */
    protected void backgroundImpl() {
        glClearColor(background.getR(), background.getG(), background.getB(), background.getA());
        glClear(GL_COLOR_BUFFER_BIT);
    }

    /**
     * 初始化前操作
     */
    protected void beforeInitialization() {
        // 创建窗口前设置透明支持
        if (transparent) glfwWindowHint(GLFW_TRANSPARENT_FRAMEBUFFER, GLFW_TRUE);
        // titleVisibility为true时显示标题栏，false时隐藏
        glfwWindowHint(GLFW_DECORATED, titleVisibility ? GLFW_TRUE : GLFW_FALSE);
        triggerBeforeInitialization();

    }

    /**
     * 初始化后操作
     */
    protected void afterInitialization() {
        setLocation(location);
        setVisible(visible);
        initSet();
        if (icon != null) {
            applyIconsToWindow(singletonList(icon));
        }
        applyTo(this);
        triggerAfterInitialization(selectHandle());
    }

    /// 坐标转换设置
    protected void coordinateSetting() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, size.width, size.height, 0, -1, 1); // 注意Y参数倒置！
        glMatrixMode(GL_MODELVIEW);

        glfwSetWindowSizeCallback(glHandle, (window, w, h) -> {
            this.size = new Size(w, h);
            glViewport(0, 0, w, h);
            updateProjectionMatrix(w, h);
        });
    }

    private void updateProjectionMatrix(int width, int height) {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, width, height, 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
    }

}
