package com.BankingApplication.Service.ServiceImp;

import com.BankingApplication.Model.*;
import com.BankingApplication.Repository.TransactionRepository;
import com.BankingApplication.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImp implements TransactionService {

    private final TransactionRepository transactionRepository;

    public Transaction createAccountTransaction(double amount, Account account, Type type, User user, double txFee)
    {
        Transaction createAccountTransaction = Transaction.builder()
                .account(account)
                .amount(amount)
                .owner(user)
                .txFee(txFee)
                .type(type)
                .status(Status.COMPLETED)
                .build();
        return transactionRepository.save(createAccountTransaction);
    }

    public Transaction createCardTransaction(double amount, Card card , Type type, User user, double txFee)
    {
        Transaction createCardTransaction = Transaction.builder()
                .card(card)
                .amount(amount)
                .owner(user)
                .txFee(txFee)
                .type(type)
                .status(Status.COMPLETED)
                .build();
        return transactionRepository.save(createCardTransaction);
    }

    @Override
    public List<Transaction> getAllTransactions(String page, User user) {
       Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
       return transactionRepository.findAllByOwnerUid(user.getUid(), pageable).getContent();
    }

    @Override
    public List<Transaction> getTransactionsByCardId(String page, String cardId, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByCardCardIdAndOwnerUid(user.getUid(), pageable, cardId).getContent();
    }

    @Override
    public List<Transaction> getTransactionsByAccountId(String page, String accountId, User user) {
        Pageable pageable = PageRequest.of(Integer.parseInt(page), 10, Sort.by("createdAt").ascending());
        return transactionRepository.findAllByAccountAccountIdAndOwnerUid(user.getUid(), pageable, accountId).getContent();
    }


}
