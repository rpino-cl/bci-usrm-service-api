package cl.bci.users.v1.exception;

import org.springframework.http.HttpStatus;

public class UserException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final HttpStatus status;

    public UserException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public UserException(String message, HttpStatus status) {
        super(message);
        this.status = status == null ? HttpStatus.BAD_REQUEST : status;
    }

    public UserException(String message, HttpStatus status, Throwable cause) {
        super(message, cause);
        this.status = status == null ? HttpStatus.BAD_REQUEST : status;
    }

    public HttpStatus getStatus() {
        return this.status;
    }

}
