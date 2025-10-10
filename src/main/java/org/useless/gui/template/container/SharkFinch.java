package org.useless.gui.template.container;

import java.util.Random;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.useless.annotation.Useless;
import org.useless.gui.data.Handle;
import org.useless.gui.exception.IllegalComponentException;
import org.useless.gui.template.agreement.Futile;

/**
 * 鲨雀<br>
 * 鲨鱼头麻雀身，起飞！
 */
@Useless(isUseless = true)
public final class SharkFinch implements Futile {

    private final static SharkFinch sf = new SharkFinch();

    private static final int index = new Random().nextBoolean() ? Integer.MAX_VALUE : Integer.MIN_VALUE;

    private SharkFinch() {}

    @Contract(pure = true)
    public static @Nullable SharkFinch get(int index) {
        if (index != SharkFinch.index) throw new IllegalComponentException("猜错了!");
        else return sf;
    }

    public @NotNull Window garbage(Handle handle) {
        Window window = new Window();
        if (handle != window.selectHandle()) throw new IllegalComponentException("句柄不正确!");
        return window;
    }

    public static int getIndex(SharkFinch sf) {
        if (sf == null) throw new IllegalComponentException("不能为空!");
        else if (sf != SharkFinch.sf) throw new IllegalComponentException("不能给错误的实例!");
        else return index;
    }


}
