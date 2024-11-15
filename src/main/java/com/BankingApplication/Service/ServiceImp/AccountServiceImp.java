package com.BankingApplication.Service.ServiceImp;

import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Model.Account;
import com.BankingApplication.Model.User;
import com.BankingApplication.Repository.AccountRepository;
import com.BankingApplication.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;

    @Override
    public Account createAccount(AccountDto accountDto, User user) {
        return accountHelper.createAccount(accountDto, user);
    }

    @Override
    public List<Account> getUserAccount(String uId) {
        return accountRepository.findAllByOwnerUid(uId);
    }


}
