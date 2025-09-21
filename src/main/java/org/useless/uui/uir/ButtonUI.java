package org.useless.uui.uir;

import org.useless.uui.Color;
import org.useless.uui.Font;
import org.useless.uui.data.AreaDecide;
import org.useless.uui.event.Mouse;
import org.useless.uui.event.MouseEvent;
import org.useless.uui.template.control.Button;

class ButtonUI implements ButtonDRI {

    @Override
    public void renderText(Button button) {
        float r = button.getBackground().getR();
        float g = button.getBackground().getG();
        float b = button.getBackground().getB();
        float brightness = r * 0.299f + g * 0.587f + b * 0.114f;

        Color textColor = (brightness > 0.5f) ? Color.BLACK : Color.WHITE;
        button.getDrawing().setColor(textColor);
        button.getDrawing().setFont(button.getFont());
        // 简单居中计算（避免复杂的文本测量）
        float tx = button.getX() + ( ((float) button.getWidth() /2) - ((float) button.getFont().getSize()) );
        float ty = button.getY() + ( ((float) button.getHeight() /2) - ( (float) button.getFont().getSize() / 2 ) );

        button.getDrawing().drawText(button.getText(), tx, ty);
    }

    @Override
    public void autoJudge(Button button) {
        if (button.isAJ()) {
            int buttonSize = Math.min(button.getWidth(),button.getHeight());
            int size = buttonSize/3;
            button.setFont(new Font(new Font(button.getFont().getName(),button.getFont().getStyle(),size)));
            if (button.getWidth() <= 60 && button.getHeight() <= 40) {
                button.setLineWidth(3.0f);
                button.setArc(5, 5, 0);
            } else if (button.getWidth() <= 120 && button.getHeight() <= 80) {
                button.setLineWidth(4.0f);
                button.setArc(10, 10, 0);
            } else {
                button.setLineWidth(5.0f);
                button.setArc(20, 20, 0);
            }
        }
    }

    @Override
    public void hoverJudgment(Button button) {
        for (MouseEvent event : button.getMouseEventList()) {
            if (AreaDecide.isMouseOver(button.getDrawing(), button)) event.alreadyHovering();
            else event.notHovering();
        }
    }

    @Override
    public void selectedFrame(Button button) {
        if (button.isSelect()) {
            button.getDrawing().drawRRS(
                    button.getX(),button.getY(),
                    button.getWidth(),button.getHeight(),
                    button.getArcWidth(),button.getArcHeight(),
                    button.getLineWidth(),Color.blue
            );
        }
    }

    @FullName(fullName = "DefaultEventImplementation")
    @Override
    public void defEventImpl(Button button) {
        button.enrolledMouse(new MouseEvent() {
            @Override
            public void clickEvent(int key) {
                // 只传递左键点击
                if (key == Mouse.LEFT_CLICK) {
                    button.setSelect(true);
                }
            }

            @Override
            public void alreadyHovering() {
                button.getDrawing().setColor(button.getBackground());
                button.getDrawing().drawRoundedRectangle(
                        button.getX(),button.getY(),
                        button.getWidth(), button.getHeight(),
                        button.getArcWidth(),button.getArcHeight(),
                        button.getArc()
                );
                button.getDrawing().setColor(new Color(200,200,200, 0.1f));
                button.getDrawing().drawRoundedRectangle(
                        button.getX(),button.getY(),
                        button.getWidth(),button.getHeight(),
                        button.getArcWidth(),button.getArcHeight(),
                        button.getArc()
                );
            }

            @Override
            public void notHovering() {
                button.getDrawing().setColor(button.getBackground());
                button.getDrawing().drawRoundedRectangle(
                        button.getX(),button.getY(),
                        button.getWidth(),button.getHeight(),
                        button.getArcWidth(),button.getArcHeight(),
                        button.getArc()
                );
            }
        });
    }

}
