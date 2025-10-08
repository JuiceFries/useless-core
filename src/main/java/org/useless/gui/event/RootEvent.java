package org.useless.gui.event;

import org.useless.gui.template.container.Window;

import static java.lang.System.exit;
import static org.useless.gui.template.container.Window.CLOSES_ONLY;
import static org.useless.gui.template.container.Window.CLOSE_AFTER_CLEANING;
import static org.useless.gui.template.container.Window.DIRECT_CLOSURE;
import static org.useless.gui.template.container.Window.NOT_CLOSED;

public class RootEvent implements Root {
    private Window window;
    private int closeOperation = CLOSE_AFTER_CLEANING;

    public RootEvent() {
    }

    public RootEvent(int closeOperation) {
        this.closeOperation = closeOperation;
    }

    public RootEvent(Window window, int closeOperation) {
        this.window = window;
        this.closeOperation = closeOperation;
    }

    @Override
    public void DragEvent() {
        // 窗口拖动事件，暂时不需要实现
    }

    @Override
    public void SizeEvent() {
        if (window != null) {
            window.refresh(); // 触发重绘
        }
    }

    @Override
    public void CloseEvent() {
        if (window == null) return;

        switch (closeOperation) {
            case DIRECT_CLOSURE -> {
                window.dispose();
                window.getEventManager().cleanup();
                exit(0);
            }
            case CLOSE_AFTER_CLEANING -> {
                // 这里可以添加资源清理逻辑
                window.dispose();
                window.getEventManager().cleanup();
                exit(0);
            }
            case CLOSES_ONLY -> {
                window.dispose();
                window.getEventManager().cleanup();
            }
            case NOT_CLOSED -> {
                // 啥也不干，就是玩
            }
            default ->  {
                window.dispose();
                window.getEventManager().cleanup();
            } // 兜底处理
        }
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public Window getWindow() {
        return window;
    }
}