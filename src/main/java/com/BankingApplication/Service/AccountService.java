package com.BankingApplication.Service;

import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Model.Account;
import com.BankingApplication.Model.User;

import java.util.List;

public interface AccountService {
    Account createAccount(AccountDto accountDto, User user);

    List<Account> getUserAccount(String uId);
}
