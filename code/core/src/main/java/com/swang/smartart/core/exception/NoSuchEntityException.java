package com.swang.smartart.core.exception;

/**
 * Created by swang on 2/10/2015.
 * The exception indicates that the entity with the properties does not exist.
 * This exception should be thrown by the service layer and caught by the controller.
 */
public class NoSuchEntityException extends Exception {

    static final long serialVersionUID = 1L;

    private String message;

    public NoSuchEntityException(String message) {
        super(message);
        this.message = message;
    }

    public NoSuchEntityException(Throwable throwable) {
        super(throwable);
        this.message = throwable.getMessage();
    }

    public String getMessage() {
        return message;
    }

}
