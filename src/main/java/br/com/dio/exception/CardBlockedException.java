package br.com.dio.exception;

public class CardBlockedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public CardBlockedException(final String message) {
        super(message);
        this.errorCode = "CARD_BLOCKED";
    }

    public CardBlockedException(final String message, final Throwable cause) {
        super(message, cause);
        this.errorCode = "CARD_BLOCKED";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
