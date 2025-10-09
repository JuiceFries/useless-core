package org.useless.gui.exception;

import org.useless.annotation.Useless;

@Useless(isUseless = true)
public class UselessCheck extends Exception{

    public UselessCheck() {
        super();
    }

    public UselessCheck(String message) {
        super(message);
    }

    public UselessCheck(String message, Throwable cause) {
        super(message, cause);
    }

    public UselessCheck(Throwable cause) {
        super(cause);
    }

}
