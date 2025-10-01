package org.useless.gui.event;

public interface Input extends Event {

    // 提供的几个常用的常量

    // 功能键
    int KEY_ESC = 256;
    int KEY_ENTER = 257;
    int KEY_TAB = 258;
    int KEY_BACKSPACE = 259;
    int KEY_INSERT = 260;
    int KEY_DELETE = 261;
    int KEY_RIGHT = 262;
    int KEY_LEFT = 263;
    int KEY_DOWN = 264;
    int KEY_UP = 265;
    int KEY_PAGE_UP = 266;
    int KEY_PAGE_DOWN = 267;
    int KEY_HOME = 268;
    int KEY_END = 269;
    int KEY_CAPS_LOCK = 280;
    int KEY_SCROLL_LOCK = 281;
    int KEY_NUM_LOCK = 282;
    int KEY_PRINT_SCREEN = 283;
    int KEY_PAUSE = 284;

    // 修饰键
    int KEY_LEFT_SHIFT = 340;
    int KEY_LEFT_CONTROL = 341;
    int KEY_LEFT_ALT = 342;
    int KEY_LEFT_SUPER = 343;
    int KEY_RIGHT_SHIFT = 344;
    int KEY_RIGHT_CONTROL = 345;
    int KEY_RIGHT_ALT = 346;
    int KEY_RIGHT_SUPER = 347;
    int KEY_MENU = 348;

    // F键
    int KEY_F1 = 290;
    int KEY_F2 = 291;
    int KEY_F3 = 292;
    int KEY_F4 = 293;
    int KEY_F5 = 294;
    int KEY_F6 = 295;
    int KEY_F7 = 296;
    int KEY_F8 = 297;
    int KEY_F9 = 298;
    int KEY_F10 = 299;
    int KEY_F11 = 300;
    int KEY_F12 = 301;
    int KEY_F13 = 302;
    int KEY_F14 = 303;
    int KEY_F15 = 304;
    int KEY_F16 = 305;
    int KEY_F17 = 306;
    int KEY_F18 = 307;
    int KEY_F19 = 308;
    int KEY_F20 = 309;
    int KEY_F21 = 310;
    int KEY_F22 = 311;
    int KEY_F23 = 312;
    int KEY_F24 = 313;
    int KEY_F25 = 314;

    // 数字键
    int KEY_0 = 48;
    int KEY_1 = 49;
    int KEY_2 = 50;
    int KEY_3 = 51;
    int KEY_4 = 52;
    int KEY_5 = 53;
    int KEY_6 = 54;
    int KEY_7 = 55;
    int KEY_8 = 56;
    int KEY_9 = 57;

    // 字母键
    int KEY_A = 65;
    int KEY_B = 66;
    int KEY_C = 67;
    int KEY_D = 68;
    int KEY_E = 69;
    int KEY_F = 70;
    int KEY_G = 71;
    int KEY_H = 72;
    int KEY_I = 73;
    int KEY_J = 74;
    int KEY_K = 75;
    int KEY_L = 76;
    int KEY_M = 77;
    int KEY_N = 78;
    int KEY_O = 79;
    int KEY_P = 80;
    int KEY_Q = 81;
    int KEY_R = 82;
    int KEY_S = 83;
    int KEY_T = 84;
    int KEY_U = 85;
    int KEY_V = 86;
    int KEY_W = 87;
    int KEY_X = 88;
    int KEY_Y = 89;
    int KEY_Z = 90;

    // 符号键
    int KEY_SPACE = 32;
    int KEY_APOSTROPHE = 39;
    int KEY_COMMA = 44;
    int KEY_MINUS = 45;
    int KEY_PERIOD = 46;
    int KEY_SLASH = 47;
    int KEY_SEMICOLON = 59;
    int KEY_EQUAL = 61;
    int KEY_LEFT_BRACKET = 91;
    int KEY_BACKSLASH = 92;
    int KEY_RIGHT_BRACKET = 93;
    int KEY_GRAVE_ACCENT = 96;

    // 小键盘
    int KEY_KP_0 = 320;
    int KEY_KP_1 = 321;
    int KEY_KP_2 = 322;
    int KEY_KP_3 = 323;
    int KEY_KP_4 = 324;
    int KEY_KP_5 = 325;
    int KEY_KP_6 = 326;
    int KEY_KP_7 = 327;
    int KEY_KP_8 = 328;
    int KEY_KP_9 = 329;
    int KEY_KP_DECIMAL = 330;
    int KEY_KP_DIVIDE = 331;
    int KEY_KP_MULTIPLY = 332;
    int KEY_KP_SUBTRACT = 333;
    int KEY_KP_ADD = 334;
    int KEY_KP_ENTER = 335;
    int KEY_KP_EQUAL = 336;

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
