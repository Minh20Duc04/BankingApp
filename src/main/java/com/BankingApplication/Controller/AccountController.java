package com.BankingApplication.Controller;


import com.BankingApplication.Dto.AccountDto;
import com.BankingApplication.Model.Account;
import com.BankingApplication.Model.User;
import com.BankingApplication.Service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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














}
