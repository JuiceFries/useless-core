package org.useless.gui.template.container;

import java.util.function.Consumer;
import jdk.jfr.Experimental;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.useless.annotation.Fixed;
import org.useless.annotation.FullName;
import org.useless.annotation.Useless;
import org.useless.gui.template.agreement.Futile;

/**
 * 垃圾<br>
 * 我觉得这很好玩并符合这个项目的目的于是我就加入了它。
 * @see Window
 * @see Consumer
 * @since 0.0.4
 * @version 1.0.0
 */
@Useless(isUseless = true)
@Fixed(continuous = "~2-Beta")
@Experimental
public final class Garbage implements Futile {

    @Useless(isUseless = true) public static final Window garbage = new Window();

    @FullName(fullName = "WindowGarbageConfigure") public static final Window wgc = garbage;

    public Garbage(@NotNull Consumer<Window> consumer) {consumer.accept(garbage);}

    public static void garbage(@NotNull Consumer<Window> runnable) {runnable.accept(garbage());}

    @Contract(" -> new") public static @NotNull Window garbage() {return garbage;}

    @FullName(fullName = "WindowGarbageConfigure")
    @Contract(" -> new") public static @NotNull Window wgc() {return garbage();}

}
