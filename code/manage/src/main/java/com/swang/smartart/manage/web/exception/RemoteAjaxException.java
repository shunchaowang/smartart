package com.swang.smartart.manage.web.exception;

/**
 * Created by swang on 3/18/2015.
 */
public class RemoteAjaxException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String code;
    private String message;

    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public RemoteAjaxException(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
