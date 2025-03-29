package com.BankingApplication.Controller;

import com.BankingApplication.Dto.LoanDto;
import com.BankingApplication.Model.CollateralAsset;
import com.BankingApplication.Model.Loan;
import com.BankingApplication.Model.User;
import com.BankingApplication.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/loan")
public class LoanController {

    private final LoanService loanService;

    @PostMapping("/create")
    public ResponseEntity<Loan> createLoan(@RequestBody LoanDto loanDto, @RequestParam("collateralAsset") CollateralAsset collateralAsset, Authentication authentication){
        User user = (User) authentication.getPrincipal();
        return ResponseEntity.ok(loanService.createLoan(loanDto, user, collateralAsset));
    }


}
