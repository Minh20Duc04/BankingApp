package com.BankingApplication.Service;

import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Dto.ConvertDto;
import com.BankingApplication.Dto.TransferDto;
import com.BankingApplication.Model.Account;
import com.BankingApplication.Model.Transaction;
import com.BankingApplication.Model.User;

import java.util.List;
import java.util.Map;

public interface AccountService {
    Account createAccount(AccountDto accountDto, User user);

    List<Account> getUserAccount(String uId);

    String deleteByAccountNumber(Long accountNumber);

    Transaction transferFunds(TransferDto transferDto, User user) throws Exception;

    Map<String, Double> getExchangeRate();

    Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception;

    Account findByAccountNumber(Long accountNumber);

    String recharge(User user, Long accountNumber, double amount) throws Exception;
}
