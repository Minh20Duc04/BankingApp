package com.BankingApplication.Service;

import com.BankingApplication.Model.Card;
import com.BankingApplication.Model.Transaction;
import com.BankingApplication.Model.User;

public interface CardService {

    Card getCard(User user);

    Card createCard(User user, double amount) throws Exception;

    Transaction creditCard(User user, double amount);

    Transaction debitCard(User user, double amount);
}
