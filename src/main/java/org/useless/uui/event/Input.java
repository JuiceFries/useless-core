package org.useless.uui.event;

import org.useless.uui.Event;

public interface Input extends Event {

    /**
     * 按下的按键
     * @param key 按键
     */
    void pressKey(int key);

    /**
     * 松开的按键
     * @param key 按键
     */
    void loosenKey(int key);

    /**
     * 长按按键
     * @param key 按键
     */
    void longPress(int key);

    /**
     * 字符输入事件
     * @param input 输入的字符
     */
    void inputKey(char input);

}
