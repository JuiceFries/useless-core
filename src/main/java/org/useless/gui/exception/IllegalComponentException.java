package org.useless.gui.exception;

import static java.lang.System.err;
import static java.lang.System.exit;

public class IllegalComponentException extends RuntimeException {
    public IllegalComponentException(String message) {
        super(message);
        err.println("UI架构异常: " + message);
        exit(1); // 直接退程序
    }
    public IllegalComponentException(String message,Exception exception) {
        super(message,exception);
        err.println("UI架构异常: " + message);
        exit(1); // 直接退程序
    }
}