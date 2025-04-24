package com.BankingApplication.Controller;


import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Dto.ConvertDto;
import com.BankingApplication.Dto.TransferDto;
import com.BankingApplication.Model.Account;
import com.BankingApplication.Model.Transaction;
import com.BankingApplication.Model.User;
import com.BankingApplication.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountDto accountDto, Authentication authentication) //muốn tạo Account phải liên kết với 1 User đã xác thực, giống đặt hàng bên Ecommerce
    {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.createAccount(accountDto, user));
    }

    @GetMapping("/")
    public ResponseEntity<List<Account>> getUserAccount(Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.getUserAccount(user.getUid()));
    }

    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transferFunds(@RequestBody TransferDto transferDto, Authentication authentication) throws Exception {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.transferFunds(transferDto, user));
    }

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getExchangeRate()
    {
        return ResponseEntity.ok(accountService.getExchangeRate());
    }

    @PostMapping("/convert")
    public ResponseEntity<Transaction> convertCurrency(@RequestBody ConvertDto convertDto, Authentication authentication) throws Exception {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.convertCurrency(convertDto, user));

    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteByAccountNumber(@RequestParam("accountNumber") Long accountNumber)
    {
        return ResponseEntity.ok(accountService.deleteByAccountNumber(accountNumber));
    }

    @PostMapping("/recharge")
    public ResponseEntity<String> recharge(@RequestParam("accountNumber") Long accountNumber, double amount, Authentication authentication) throws Exception {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(accountService.recharge(user,accountNumber, amount));
    }

}
