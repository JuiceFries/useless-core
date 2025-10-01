package org.useless.gui.data;

import java.util.Iterator;
import org.jetbrains.annotations.NotNull;

/**
 * 知道我为什么要脑子<br>
 * 被驴踢了一样搞出这么一个接口<br>
 * 不过算了管他呢用就用呢
 * @param <T1> 第一
 * @param <T2> 第二
 * @param <T3> 第三
 */
public interface Ternary<T1, T2, T3> extends Iterable<Object[]> {
    void add(T1 a, T2 b, T3 c);
    void insert(int index, T1 a, T2 b, T3 c);
    void remove(int index);
    void update(int index, T1 a, T2 b, T3 c);

    Ternary<T1,T2,T3> get(int index);
    T1 getFirst(int index);
    T2 getSecond(int index);
    T3 getThird(int index);
    int size();
    boolean isEmpty();
    void clear();

    void addAll(Ternary<T1, T2, T3> other);
    Ternary<T1, T2, T3> subList(int fromIndex, int toIndex);

    // 默认迭代器实现
    default @NotNull Iterator<Object[]> iterator() {
        return new Iterator<>() {
            private int cursor = 0;

            @Override
            public boolean hasNext() {
                return cursor < size();
            }

            @Override
            public Object[] next() {
                Object[] triple = new Object[]{
                        getFirst(cursor),
                        getSecond(cursor),
                        getThird(cursor)
                };
                cursor++;
                return triple;
            }
        };
    }
}