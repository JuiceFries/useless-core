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