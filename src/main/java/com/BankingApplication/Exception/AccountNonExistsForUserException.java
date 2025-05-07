package com.BankingApplication.Exception;

public class AccountNonExistsForUserException extends Exception{
    public AccountNonExistsForUserException(String message) {
        super(message);
    }
}
