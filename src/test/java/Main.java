import org.useless.gui.template.container.Window;
import org.useless.gui.template.control.TextField;
import org.useless.gui.uir.TintStyle;
import org.useless.gui.uir.UIManager;
import org.useless.gui.uir.UselessCheck;

public class Main {
    public static void main(String[] args) throws UselessCheck {
        UIManager.setUIStyle(new TintStyle());
        var window = new Window("窗口");
        UIManager.applyTo(window);
        window.setSize(800,600);
        window.setLocation(null);
        window.setVisible(true);
        TextField textField = new TextField();
        textField.setText("hhh");
        textField.setBounds(0,0, window.getWidth(), 60);
        window.add(textField);
    }
}
