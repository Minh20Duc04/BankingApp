package com.BankingApplication.Service.ServiceImp;

import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Model.Account;
import com.BankingApplication.Model.User;
import com.BankingApplication.Repository.AccountRepository;
import com.BankingApplication.Util.RandomUtil;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class AccountHelper {

    private final AccountRepository accountRepository;
    private final Logger logger = LoggerFactory.getLogger(AccountHelper.class);

    private final Map<String, String> CURRENCIES = Map.of(
            "USD", "United States Dollar",
            "EUR", "Euro",
            "GBP", "British Pound",
            "JPY", "Japanese Yen",
            "NGN", "Nigerian Naira",
            "INR", "Indian Rupee",
            "DONG", "VietNam Dong"
    );

    public Account createAccount(AccountDto accountDto, User user)
    {
        try
        {
            validateAccountNonExistsForUser(accountDto.getCode(), user.getUid());
        }catch (Exception e)
        {
            logger.error(e.getMessage(), e);
        }
        long accountNumber;
        do {
            accountNumber = RandomUtil.generateRandom(10);
        }while(accountRepository.existsByAccountNumber(accountNumber));

        Account account = Account.builder()
                .accountName(user.getFirstName() + " " + user.getLastName())
                .accountNumber(accountNumber)
                .owner(user)
                .label(CURRENCIES.get(accountDto.getCode()))
                .code(accountDto.getCode())
                .balance(1000)
                .symbol(accountDto.getSymbol())
                .build();
        return accountRepository.save(account);
    }

    public void validateAccountNonExistsForUser(String code, String uId) throws Exception {
        if(accountRepository.existsByCodeAndOwnerUid(code, uId))
        {
            throw new Exception("Account of this type already exist for this user");
        }
    }





}
