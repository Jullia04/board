package br.com.dio.exception;

public class EntityNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public EntityNotFoundException(String message) {
        super(message);
        this.errorCode = "ENTITY_NOT_FOUND";
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "ENTITY_NOT_FOUND";
    }

    public EntityNotFoundException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
