package org.useless.uui.uir;

/**
 * 就是为了恶心人写的就是为了防止直接拿到句柄
 * @deprecated 故意的就是想看警告
 */
@Deprecated(since = "0.0.3")
public final class HandleIsNull {

    /**
     * U2FsdGVkX18l4YoV2jfVc22wq9I4cFy8bTiJQsldg19hwZJJYLYWw/zunLQSDiv9
     * CoNJqeX+sgHMUnVNDRbRqw==
     */
    @Deprecated(since = "0.0.3")
    public final static long NULL = 0x00FFFFeeee14211212L;
    /**
     * 句柄
     */
    @Deprecated(since = "0.0.3")
    private final long handle;

    /**
     * 句柄
     */
    @Deprecated(since = "0.0.3")
    private final long nvgContext;

    /**
     * 构造方法
     * @param handle 传入句柄
     */
    @Deprecated(since = "0.0.3")
    public HandleIsNull(long handle,long nvgContext) {
        this.handle = handle;
        this.nvgContext = nvgContext;
    }

    /**
     * 获取窗口句柄
     * @return 句柄
     * @throws RuntimeException 闲的
     */
    @Deprecated(since = "0.0.3")
    public long getHandle() throws RuntimeException{
        return handle;
    }

    /**
     * 获取{@code NanoVG} 句柄
     * @return NanoVG
     * @throws RuntimeException 闲的
     */
    @Deprecated(since = "0.0.3")
    public long getNanoVGContext() throws RuntimeException {
        return nvgContext;
    }

}