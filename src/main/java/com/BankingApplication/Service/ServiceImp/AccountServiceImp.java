package com.BankingApplication.Service.ServiceImp;

import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Dto.ConvertDto;
import com.BankingApplication.Dto.TransferDto;
import com.BankingApplication.Model.Account;
import com.BankingApplication.Model.Transaction;
import com.BankingApplication.Model.User;
import com.BankingApplication.Repository.AccountRepository;
import com.BankingApplication.Service.AccountService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class AccountServiceImp implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final ExchangeRateService exchangeRateService;

    @Override
    public Account createAccount(AccountDto accountDto, User user) {
        return accountHelper.createAccount(accountDto, user);
    }

    @Override
    public List<Account> getUserAccount(String uId) {
        return accountRepository.findAllByOwnerUid(uId);
    }

    @Override
    public String deleteByAccountNumber(Long accountNumber)
    {
        Account accountDB = accountRepository.findByAccountNumber(accountNumber).orElseThrow(()-> new RuntimeException("Account not found with number: "  + accountNumber));
        accountRepository.delete(accountDB);
        return "Account has been delete successfully";
    }

    @Override
    public Transaction transferFunds(TransferDto transferDto, User user) throws Exception {
        //kiểm tra đơn giản xem code người gửi và người nhận có cùng 1 loại tiền không, người nhận có tồn tại không
        var senderAccount = accountRepository.findTopByCodeAndOwnerUidOrderByBalanceDesc(transferDto.getCode(), user.getUid())
                .orElseThrow(()-> new UnsupportedOperationException("Account of type currency do not exists for user"));
        var receiverAccount = accountRepository.findByAccountNumber(transferDto.getReceiverAccountNumber()).orElseThrow();
        return accountHelper.performTransfer(senderAccount, receiverAccount, transferDto.getAmount(), user); //kiểm tra chi tiết ở lớp helper
    }

    @Override
    public Map<String, Double> getExchangeRate() {
        return exchangeRateService.getRates();
    }

    @Override
    public Transaction convertCurrency(ConvertDto convertDto, User user) throws Exception {
        return accountHelper.convertCurrency(convertDto, user);
    }



}
