package com.BankingApplication.Exception;

import javax.naming.OperationNotSupportedException;

public class InsufficientFundsException extends OperationNotSupportedException {
    public InsufficientFundsException(String message) {
        super(message);
    }
}
