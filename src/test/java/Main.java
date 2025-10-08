import org.useless.gui.template.container.Window;
import org.useless.gui.uir.TintStyle;
import org.useless.gui.uir.UIManager;
import org.useless.gui.exception.UselessCheck;

public class Main {
    public static void main(String[] args) throws UselessCheck {
        UIManager.setUIStyle(new TintStyle());
        var window = new Window("窗口");
        window.setSize(800,600);
        window.setLocation(null);
        window.setVisible(true);
    }
}
