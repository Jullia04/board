package br.com.dio.exception;

public class CardFinishedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public CardFinishedException(final String message) {
        super(message);
        this.errorCode = "CARD_FINISHED";
    }

    public CardFinishedException(final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = "CARD_FINISHED";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
