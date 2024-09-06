package dev.babatunde.transferservice.exceptions;

public class CurrencyMismatchException extends RuntimeException{

    public CurrencyMismatchException(String message) {
        super(message);
    }

    public CurrencyMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
