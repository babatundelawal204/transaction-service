package dev.babatunde.transferservice.exceptions;

public class TransactionAlreadyExistException extends RuntimeException{

    public TransactionAlreadyExistException(String message){
        super(message);
    }

    public TransactionAlreadyExistException(String message, Throwable cause){
        super(message, cause);
    }
}
