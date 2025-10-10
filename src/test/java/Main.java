import org.useless.gui.data.Color;

import static org.useless.gui.template.container.Garbage.wgc;

public class Main {
    public static void main(String[] args) {
        wgc.setTitle("窗口");
        wgc.setSize(800,600);
        wgc.setLocation(null);
        wgc.setVisible(true);
        wgc.draw(drawing -> {
            drawing.setColor(Color.RED);
            drawing.drawTriangle(
                    150,100,
                    100,300,
                    200,300
            );
        });
    }
}
