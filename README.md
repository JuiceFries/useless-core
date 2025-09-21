# Useless

# 1.介绍

懒得写，就是一个可以简单运行的窗口DEMO

依赖项用不了，我没上传中央仓库

````java

import org.useless.uui.Color;
import org.useless.uui.Window;

public class Main {
    public static void main(String[] args) {
        Window window = new Window("窗口"); // 创建窗口并命名
        window.setSize(800, 600); // 设置窗口大小
        window.setLocation(null); // 设置窗口居中
        window.setVisible(true); // 设置窗口可见
        window.setBackground(Color.NULL); // 设置窗口透明
        window.drawing(drawing -> { // 绘图命令
            drawing.setColor(Color.RED); // 设置颜色属性
            drawing.drawTriangle(0.0f,0.0f,400.0f,600.0f,800.0f,0.0f); //画个图形
        });
    }
}

````

# 2.绘图

`Draawing` 已经提供了部分会使用到的简单绘图方法，注意在绘图操作之外执行时可能会产生线程问题

# 3.懒得写了