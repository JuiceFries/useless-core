package org.useless.gui.uir;

import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;
import org.useless.gui.data.Handle;
import org.useless.annotation.Fixed;

@Fixed(continuous = "~0.2-Beta")
@Deprecated(since = "0.0.3")
public class InitPhase {

    private static Consumer<Handle> afterInitCallback;
    private static Runnable beforeInit;
    private static Runnable frameTask;

    public static void operation(@NotNull Runnable runnable) {
        frameTask = runnable; // 保存起来给窗口每帧调用
    }

    public static void executeFrameTask() {
        if (frameTask != null) frameTask.run();
    }

    public static void beforeInitialization(Runnable runnable) {
        beforeInit = runnable;
    }

    public static void afterInitialization(Consumer<Handle> callback) {
        afterInitCallback = callback;
    }

    public static void triggerAfterInitialization(Handle handle) {
        if (afterInitCallback != null) afterInitCallback.accept(handle);
    }

    public static void triggerBeforeInitialization() {
        if (beforeInit != null ) beforeInit.run();
    }

}
