package dev.babatunde.transferservice.exceptions;

public class AccountNumberNotExistException extends RuntimeException{

    public AccountNumberNotExistException(String message){
        super(message);
    }

    public AccountNumberNotExistException(String message, Throwable cause){
        super(message, cause);
    }
}
