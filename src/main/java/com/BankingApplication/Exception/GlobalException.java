package com.BankingApplication.Exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(AccountNonExistsForUserException.class)
    public String printExceptionMess(AccountNonExistsForUserException exception){
        return exception.getMessage();
    }


    @ExceptionHandler(InsufficientFundsException.class)
    public String printExceptionMess(InsufficientFundsException exception){
        return exception.getMessage();
    }

    @ExceptionHandler(SameCurrencyConversionException.class)
    public String printExceptionMess(SameCurrencyConversionException exception){
        return exception.getMessage();
    }


}
