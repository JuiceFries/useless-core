// TernaryList.java - 完整生产级别版本
package org.useless.gui.data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.useless.annotation.Useless;

@Useless(isUseless = true)
public class TernaryList<T1, T2, T3> implements Ternary<T1, T2, T3>, Serializable {
    @Serial
    private static final long serialVersionUID = 0xCAFEBABEL;
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] data;
    private int size;

    public TernaryList() {
        data = new Object[DEFAULT_CAPACITY * 3];
        size = 0;
    }

    public TernaryList(int initialCapacity) {
        if (initialCapacity < 0)
            throw new IllegalArgumentException("初始容量不能为负数: " + initialCapacity);
        data = new Object[initialCapacity * 3];
        size = 0;
    }

    public TernaryList(Ternary<T1, T2, T3> other) {
        this(other.size());
        addAll(other);
    }

    @Override
    public void add(T1 a, T2 b, T3 c) {
        ensureCapacity(size + 1);
        data[size * 3] = a;
        data[size * 3 + 1] = b;
        data[size * 3 + 2] = c;
        size++;
    }

    @Override
    public void insert(int index, T1 a, T2 b, T3 c) {
        checkIndexForAdd(index);
        ensureCapacity(size + 1);

        System.arraycopy(data, index * 3, data, (index + 1) * 3, (size - index) * 3);

        data[index * 3] = a;
        data[index * 3 + 1] = b;
        data[index * 3 + 2] = c;
        size++;
    }

    @Override
    public void remove(int index) {
        checkIndex(index);

        if (index < size - 1) {
            System.arraycopy(data, (index + 1) * 3, data, index * 3, (size - index - 1) * 3);
        }

        // 清理GC引用
        int lastPos = (size - 1) * 3;
        Arrays.fill(data, lastPos, lastPos + 3, null);
        size--;
    }

    @Override
    public void update(int index, T1 a, T2 b, T3 c) {
        checkIndex(index);
        data[index * 3] = a;
        data[index * 3 + 1] = b;
        data[index * 3 + 2] = c;
    }

    @Override
    public Ternary<T1, T2, T3> get(int index) {
        checkIndex(index);
        // 返回包含单个三元组的子列表
        return this.subList(index, index + 1);
    }

    @Override
    public T1 getFirst(int index) {
        checkIndex(index);
        return (T1) data[index * 3];
    }

    @Override
    public T2 getSecond(int index) {
        checkIndex(index);
        return (T2) data[index * 3 + 1];
    }

    @Override
    public T3 getThird(int index) {
        checkIndex(index);
        return (T3) data[index * 3 + 2];
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Arrays.fill(data, 0, size * 3, null);
        size = 0;
    }

    @Override
    public void addAll(Ternary<T1, T2, T3> other) {
        Objects.requireNonNull(other, "其他三元组不能为null");

        if (other instanceof TernaryList<T1, T2, T3> otherList) {
            ensureCapacity(size + otherList.size);
            System.arraycopy(otherList.data, 0, data, size * 3, otherList.size * 3);
            size += otherList.size;
        } else {
            for (int i = 0; i < other.size(); i++) {
                add(other.getFirst(i), other.getSecond(i), other.getThird(i));
            }
        }
    }

    @Override
    public Ternary<T1, T2, T3> subList(int fromIndex, int toIndex) {
        checkIndex(fromIndex);
        checkIndex(toIndex - 1);
        if (fromIndex > toIndex)
            throw new IllegalArgumentException("fromIndex > toIndex: " + fromIndex + " > " + toIndex);

        TernaryList<T1, T2, T3> subList = new TernaryList<>(toIndex - fromIndex);
        for (int i = fromIndex; i < toIndex; i++) {
            subList.add(getFirst(i), getSecond(i), getThird(i));
        }
        return subList;
    }

    // 性能优化方法
    public void trimToSize() {
        if (data.length > size * 3) {
            data = Arrays.copyOf(data, size * 3);
        }
    }

    public int capacity() {
        return data.length / 3;
    }

    // 批量更新
    public void setAll(int fromIndex, Ternary<T1, T2, T3> values) {
        checkIndex(fromIndex);
        Objects.requireNonNull(values, "值不能为null");

        int requiredSize = fromIndex + values.size();
        ensureCapacity(requiredSize);

        for (int i = 0; i < values.size(); i++) {
            int targetIndex = fromIndex + i;
            data[targetIndex * 3] = values.getFirst(i);
            data[targetIndex * 3 + 1] = values.getSecond(i);
            data[targetIndex * 3 + 2] = values.getThird(i);
        }

        size = Math.max(size, requiredSize);
    }

    // 查找方法
    public int indexOfFirst(T1 value) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i * 3], value)) return i;
        }
        return -1;
    }

    public int indexOfSecond(T2 value) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i * 3 + 1], value)) return i;
        }
        return -1;
    }

    public int indexOfThird(T3 value) {
        for (int i = 0; i < size; i++) {
            if (Objects.equals(data[i * 3 + 2], value)) return i;
        }
        return -1;
    }

    // 实用静态工厂
    public static <T1, T2, T3> TernaryList<T1, T2, T3> of(T1 first, T2 second, T3 third) {
        TernaryList<T1, T2, T3> list = new TernaryList<>(1);
        list.add(first, second, third);
        return list;
    }

    @SafeVarargs
    public static <T1, T2, T3> TernaryList<T1, T2, T3> ofTriplets(Triplet<T1, T2, T3>... triplets) {
        TernaryList<T1, T2, T3> list = new TernaryList<>(triplets.length);
        for (Triplet<T1, T2, T3> triplet : triplets) {
            list.add(triplet.first(), triplet.second(), triplet.third());
        }
        return list;
    }

    // 内部工具方法
    private void ensureCapacity(int minCapacity) {
        if (minCapacity * 3 > data.length) {
            int newCapacity = Math.max(data.length * 2, minCapacity * 3);
            data = Arrays.copyOf(data, newCapacity);
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("索引: " + index + ", 大小: " + size);
        }
    }

    private void checkIndexForAdd(int index) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("索引: " + index + ", 大小: " + size);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Ternary<?, ?, ?> other)) return false;

        if (size != other.size()) return false;

        for (int i = 0; i < size; i++) {
            if (!Objects.equals(getFirst(i), other.getFirst(i)) ||
                    !Objects.equals(getSecond(i), other.getSecond(i)) ||
                    !Objects.equals(getThird(i), other.getThird(i))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + Objects.hashCode(getFirst(i));
            result = 31 * result + Objects.hashCode(getSecond(i));
            result = 31 * result + Objects.hashCode(getThird(i));
        }
        return result;
    }

    @Override
    public String toString() {
        if (size == 0) return "TernaryList[]";

        StringBuilder sb = new StringBuilder("TernaryList[");
        for (int i = 0; i < size; i++) {
            if (i > 0) sb.append(", ");
            sb.append("(").append(data[i * 3])
                    .append(", ").append(data[i * 3 + 1])
                    .append(", ").append(data[i * 3 + 2]).append(")");
        }
        return sb.append("]").toString();
    }

    // 值对象记录类
    @Useless(isUseless = true)
    public record Triplet<T1, T2, T3>(T1 first, T2 second, T3 third) {
        @Override
        public @NotNull String toString() {
            return "(" + first + ", " + second + ", " + third + ")";
        }
    }
}