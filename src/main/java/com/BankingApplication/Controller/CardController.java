package com.BankingApplication.Controller;


import com.BankingApplication.Model.Card;
import com.BankingApplication.Model.Transaction;
import com.BankingApplication.Model.User;
import com.BankingApplication.Service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/card")
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<Card> getCard(Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.getCard(user));
    }

    @PostMapping("/create")
    public ResponseEntity<Card> createCard(@RequestParam("amount") double amount, Authentication authentication, @RequestParam(name = "file") MultipartFile file) throws Exception {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.createCard(user, amount, file));
    }

    @PostMapping("/credit")
    public ResponseEntity<Transaction> creditCard(@RequestParam("amount") double amount, Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.creditCard(user, amount));
    }

    @PostMapping("/debit")
    public ResponseEntity<Transaction> debitCard(@RequestParam("amount") double amount, Authentication authentication)
    {
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(cardService.debitCard(user, amount));
    }






}
