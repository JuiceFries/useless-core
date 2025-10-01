package org.useless.gui.picture;

import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.useless.gui.data.Size;

public class ImageLoader {

    /**
     * 从文件加载图片
     */
    public static Picture loadFromFile(String filePath) throws ImageLoadException {
        Image image = new Image(filePath);
        image.load();
        return image;
    }

    /**
     * 从内存数据创建图片
     */
    public static Picture loadFromMemory(byte[] imageData, String name) throws ImageLoadException {
        MemoryImage image = new MemoryImage(imageData, name);
        image.load();
        return image;
    }

    /**
     * 从ByteBuffer加载图片
     */
    public static Picture loadFromBuffer(ByteBuffer buffer, String name) throws ImageLoadException {
        BufferImage image = new BufferImage(buffer, name);
        image.load();
        return image;
    }

    /**
     * 创建空图片（用于占位）
     */
    public static Picture createEmpty(int width, int height) {
        EmptyImage image = new EmptyImage(width, height);
        try {
            image.load();
        } catch (ImageLoadException e) {
            // 空图片不会抛出异常
        }
        return image;
    }

    /**
     * 创建纯色图片
     */
    public static Picture createColorImage(int width, int height, int rgbaColor) throws ImageLoadException {
        ColorImage image = new ColorImage(width, height, rgbaColor);
        image.load();
        return image;
    }

    /**
     * 创建棋盘格图片（用于调试）
     */
    public static Picture createCheckerboard(int width, int height, int color1, int color2, int tileSize) throws ImageLoadException {
        CheckerboardImage image = new CheckerboardImage(width, height, color1, color2, tileSize);
        image.load();
        return image;
    }

    /**
     * 预加载多个图片
     */
    public static Picture[] loadMultiple(String... filePaths) throws ImageLoadException {
        Picture[] result = new Picture[filePaths.length];
        for (int i = 0; i < filePaths.length; i++) {
            result[i] = loadFromFile(filePaths[i]);
        }
        return result;
    }

    /**
     * 检查图片文件是否支持
     */
    public static boolean isFormatSupported(String filePath) {
        if (filePath == null) return false;
        String lower = filePath.toLowerCase();
        return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".bmp") || lower.endsWith(".tga") || lower.endsWith(".psd")
                || lower.endsWith(".gif") || lower.endsWith(".hdr");
    }

    // 内存图片实现
    private static class MemoryImage extends Image {
        private final byte[] sourceData;

        public MemoryImage(byte[] imageData, String name) {
            super("memory://" + name);
            this.sourceData = imageData;
        }

        @Override
        public void load() throws ImageLoadException {
            if (disposed) {
                throw new ImageLoadException("图片已释放，无法重新加载: " + path);
            }

            if (loaded) {
                return;
            }

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer comp = stack.mallocInt(1);

                // 将byte[]转换为ByteBuffer
                ByteBuffer buffer = MemoryUtil.memAlloc(sourceData.length);
                buffer.put(sourceData);
                buffer.flip();

                // 修复：使用正确的STBImage方法
                imageData = STBImage.stbi_load_from_memory(buffer, width, height, comp, 4);
                MemoryUtil.memFree(buffer);

                if (imageData == null) {
                    throw new ImageLoadException("STB_image从内存加载失败: " + path + " - " + STBImage.stbi_failure_reason());
                }

                this.size = new Size(width.get(0), height.get(0));
                this.channels = comp.get(0);
                this.loaded = true;

            } catch (Exception e) {
                if (e instanceof ImageLoadException) throw (ImageLoadException) e;
                throw new ImageLoadException("内存图片加载失败: " + path, e);
            }
        }
    }

    // Buffer图片实现
    private static class BufferImage extends Image {
        private final ByteBuffer sourceBuffer;

        public BufferImage(ByteBuffer buffer, String name) {
            super("buffer://" + name);
            this.sourceBuffer = buffer;
        }

        @Override
        public void load() throws ImageLoadException {
            if (disposed) {
                throw new ImageLoadException("图片已释放，无法重新加载: " + path);
            }

            if (loaded) {
                return;
            }

            try (MemoryStack stack = MemoryStack.stackPush()) {
                IntBuffer width = stack.mallocInt(1);
                IntBuffer height = stack.mallocInt(1);
                IntBuffer comp = stack.mallocInt(1);

                // 创建缓冲区的副本，避免修改原始数据
                ByteBuffer bufferCopy = MemoryUtil.memAlloc(sourceBuffer.remaining());
                int originalPosition = sourceBuffer.position();
                bufferCopy.put(sourceBuffer);
                bufferCopy.flip();
                sourceBuffer.position(originalPosition); // 恢复原始位置

                // 修复：使用正确的STBImage方法
                imageData = STBImage.stbi_load_from_memory(bufferCopy, width, height, comp, 4);
                MemoryUtil.memFree(bufferCopy);

                if (imageData == null) {
                    throw new ImageLoadException("STB_image从缓冲区加载失败: " + path + " - " + STBImage.stbi_failure_reason());
                }

                this.size = new Size(width.get(0), height.get(0));
                this.channels = comp.get(0);
                this.loaded = true;

            } catch (Exception e) {
                if (e instanceof ImageLoadException) throw (ImageLoadException) e;
                throw new ImageLoadException("缓冲区图片加载失败: " + path, e);
            }
        }
    }

    // 空图片实现
    private static class EmptyImage extends Image {
        public EmptyImage(int width, int height) {
            super("empty://" + width + "x" + height);
            this.size = new Size(width, height);
        }

        @Override
        public void load() throws ImageLoadException {
            if (disposed) {
                throw new ImageLoadException("图片已释放，无法重新加载: " + path);
            }

            if (loaded) {
                return;
            }

            // 创建空的RGBA缓冲区（全透明）
            int bufferSize = size.width * size.height * 4;
            imageData = MemoryUtil.memAlloc(bufferSize);

            // 填充为全透明
            for (int i = 0; i < bufferSize; i++) {
                imageData.put((byte) 0);
            }
            imageData.flip();

            this.channels = 4;
            this.loaded = true;
        }

        @Override
        public void dispose() {
            if (disposed) return;

            if (imageData != null) {
                MemoryUtil.memFree(imageData);
                imageData = null;
            }

            nativeHandle = null;
            disposed = true;
            loaded = false;
        }
    }

    // 纯色图片实现
    private static class ColorImage extends Image {
        private final int rgbaColor;

        public ColorImage(int width, int height, int rgbaColor) {
            super("color://" + width + "x" + height + "_" + Integer.toHexString(rgbaColor));
            this.size = new Size(width, height);
            this.rgbaColor = rgbaColor;
        }

        @Override
        public void load() throws ImageLoadException {
            if (disposed) {
                throw new ImageLoadException("图片已释放，无法重新加载: " + path);
            }

            if (loaded) {
                return;
            }

            try {
                // 提取RGBA分量（注意字节顺序）
                byte r = (byte) ((rgbaColor >> 16) & 0xFF);  // Red
                byte g = (byte) ((rgbaColor >> 8) & 0xFF);   // Green
                byte b = (byte) (rgbaColor & 0xFF);          // Blue
                byte a = (byte) ((rgbaColor >> 24) & 0xFF);  // Alpha

                // 创建纯色图片数据
                int bufferSize = size.width * size.height * 4;
                imageData = MemoryUtil.memAlloc(bufferSize);

                for (int i = 0; i < size.width * size.height; i++) {
                    imageData.put(r).put(g).put(b).put(a);
                }
                imageData.flip();

                this.channels = 4;
                this.loaded = true;

            } catch (Exception e) {
                throw new ImageLoadException("纯色图片创建失败: " + path, e);
            }
        }

        @Override
        public void dispose() {
            if (disposed) return;

            if (imageData != null) {
                MemoryUtil.memFree(imageData);
                imageData = null;
            }

            nativeHandle = null;
            disposed = true;
            loaded = false;
        }
    }

    // 棋盘格图片实现
    private static class CheckerboardImage extends Image {
        private final int color1;
        private final int color2;
        private final int tileSize;

        public CheckerboardImage(int width, int height, int color1, int color2, int tileSize) {
            super("checkerboard://" + width + "x" + height);
            this.size = new Size(width, height);
            this.color1 = color1;
            this.color2 = color2;
            this.tileSize = Math.max(1, tileSize);
        }

        @Override
        public void load() throws ImageLoadException {
            if (disposed) {
                throw new ImageLoadException("图片已释放，无法重新加载: " + path);
            }

            if (loaded) {
                return;
            }

            try {
                // 提取颜色分量
                byte[] color1Bytes = extractColorBytes(color1);
                byte[] color2Bytes = extractColorBytes(color2);

                // 创建棋盘格数据
                int bufferSize = size.width * size.height * 4;
                imageData = MemoryUtil.memAlloc(bufferSize);

                for (int y = 0; y < size.height; y++) {
                    for (int x = 0; x < size.width; x++) {
                        boolean isColor1 = ((x / tileSize) + (y / tileSize)) % 2 == 0;
                        byte[] color = isColor1 ? color1Bytes : color2Bytes;
                        imageData.put(color[0]).put(color[1]).put(color[2]).put(color[3]);
                    }
                }
                imageData.flip();

                this.channels = 4;
                this.loaded = true;

            } catch (Exception e) {
                throw new ImageLoadException("棋盘格图片创建失败: " + path, e);
            }
        }

        private byte[] extractColorBytes(int color) {
            return new byte[] {
                    (byte) ((color >> 16) & 0xFF),  // R
                    (byte) ((color >> 8) & 0xFF),   // G
                    (byte) (color & 0xFF),          // B
                    (byte) ((color >> 24) & 0xFF)   // A
            };
        }

        @Override
        public void dispose() {
            if (disposed) return;

            if (imageData != null) {
                MemoryUtil.memFree(imageData);
                imageData = null;
            }

            nativeHandle = null;
            disposed = true;
            loaded = false;
        }
    }
}