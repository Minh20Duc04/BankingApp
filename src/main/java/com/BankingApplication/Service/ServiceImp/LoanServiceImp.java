package com.BankingApplication.Service.ServiceImp;

import com.BankingApplication.Dto.LoanDto;
import com.BankingApplication.Model.*;
import com.BankingApplication.Repository.AccountRepository;
import com.BankingApplication.Repository.LoanRepository;
import com.BankingApplication.Service.LoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LoanServiceImp implements LoanService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;

    @Override
    public Loan createLoan(LoanDto loanDto, User user, CollateralAsset collateralAsset) {
        Account account = accountRepository.findByAccountNumberAndOwnerUid(loanDto.getAccountNumber(), user.getUid()).orElseThrow(()-> new IllegalArgumentException("The user with this accountNumber is not existed"));
        Loan loan = createNewLoan(loanDto, collateralAsset, account);
        return loanRepository.save(loan);
    }

    private Loan createNewLoan(LoanDto loanDto, CollateralAsset collateralAsset, Account account) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = startDate.plusMonths((long) loanDto.getLoanTerm());

        double monthlyPayment = (loanDto.getLoanAmount() * (1 + loanDto.getInterestRate())) / loanDto.getLoanTerm();

        return Loan.builder()
                .loanAmount(loanDto.getLoanAmount())
                .interestRate(loanDto.getInterestRate())
                .loanTerm(loanDto.getLoanTerm())
                .monthlyPayment(monthlyPayment)
                .account(account)
                .status(Status.REPAYING)
                .collateralAsset(collateralAsset)
                .loanStartDate(startDate)
                .dueDate(dueDate)
                .build();
    }
}