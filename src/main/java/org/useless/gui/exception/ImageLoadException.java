package org.useless.gui.exception;

public class ImageLoadException extends Exception {
    public ImageLoadException() {
        super();
    }


    public ImageLoadException(String message) {
        super(message);
    }


    public ImageLoadException(String message, Throwable cause) {
        super(message, cause);
    }

    public ImageLoadException(Throwable cause) {
        super(cause);
    }

}
