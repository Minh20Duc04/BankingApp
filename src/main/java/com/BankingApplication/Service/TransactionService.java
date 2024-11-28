package com.BankingApplication.Service;

import com.BankingApplication.Model.*;

import java.util.List;

public interface TransactionService {

    Transaction createAccountTransaction(double amount, Account account, Type type, User user, double txFee);

    Transaction createCardTransaction(double amount, Card card , Type type, User user, double txFee);

    List<Transaction> getAllTransactions(String page, User user);

    List<Transaction> getTransactionsByCardId(String page, String cardId, User user);

    List<Transaction> getTransactionsByAccountId(String page, String accountId, User user);
}
