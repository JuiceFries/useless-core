package org.useless.uui.image;

import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.useless.uui.Image;
import org.useless.uui.data.Size;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class GLImage extends Image {
    @Override
    public void loadImpl(String path) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer comp = stack.mallocInt(1);

            // 在STBImage.stbi_load前添加
            System.out.println("尝试加载: " + path);
            ByteBuffer imageData = STBImage.stbi_load(path, w, h, comp, 4);
            if (imageData == null) {
                System.err.println("STB加载失败: " + STBImage.stbi_failure_reason());
                throw new RuntimeException("加载图片失败: " + path);
            }

            textureId = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)textureId); // 强转int
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, w.get(), h.get(),
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageData);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

            size = new Size(w.get(), h.get());
            loaded = true;

            STBImage.stbi_image_free(imageData);
        }
    }

    @Override
    public void bindTexture() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, (int)textureId); // 强转int
    }

    @Override
    public boolean loadAsync(String path) {
        new Thread(() -> load(path)).start();
        return true;
    }

}