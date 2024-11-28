package com.BankingApplication.Controller;

import com.BankingApplication.Model.Transaction;
import com.BankingApplication.Model.User;
import com.BankingApplication.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/getAll")
    public ResponseEntity<List<Transaction>> getAllTransactions (@RequestParam("page") String page, Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getAllTransactions(page, user));
    }

    @GetMapping("/getAllByCardId/{cardId}")
    public ResponseEntity<List<Transaction>> getTransactionsByCardId (@RequestParam("page") String page, Authentication authentication, @PathVariable String cardId)
    {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getTransactionsByCardId(page, cardId,user));
    }

    @GetMapping("getAllByAccountId/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsByAccountId (@RequestParam("page") String page, Authentication authentication, @PathVariable String accountId)
    {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(transactionService.getTransactionsByAccountId(page, accountId,user));
    }

}
