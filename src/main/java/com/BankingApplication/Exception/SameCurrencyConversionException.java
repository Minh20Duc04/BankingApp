package com.BankingApplication.Exception;

public class SameCurrencyConversionException extends IllegalArgumentException{
    public SameCurrencyConversionException(String message) {
        super(message);
    }
}
