package com.BankingApplication.Service.ServiceImp;

import com.BankingApplication.Dto.LoanDto;
import com.BankingApplication.Model.*;
import com.BankingApplication.Repository.AccountRepository;
import com.BankingApplication.Repository.LoanRepository;
import com.BankingApplication.Service.LoanService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class LoanServiceImp implements LoanService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private Logger loggerFactory = LoggerFactory.getLogger(LoanServiceImp.class);

    @Override
    public Loan createLoan(LoanDto loanDto, User user, String collateralAsset) throws Exception {
        Account account = accountRepository.findByAccountNumberAndOwnerUid(loanDto.getAccountNumber(), user.getUid()).orElseThrow(()-> new IllegalArgumentException("The user with this accountNumber is not existed"));
        accountHelper.validateAmount(loanDto.getLoanAmount());
        Loan loan = createNewLoan(loanDto, collateralAsset, account);
        account.setBalance(account.getBalance() + loan.getLoanAmount());
        deductibleScheduling(account, loan.getLoanAmount());
        return loanRepository.save(loan);
    }


    @Scheduled(fixedDelay = 1000)
    private void deductibleScheduling(Account account, double deduction){
        if(account.getBalance() < 0){
            log.info("Đã mất tài sản thế chấp");
        }
        else{
            account.setBalance(account.getBalance() - deduction);
        }
    }

    private Loan createNewLoan(LoanDto loanDto, String collateralAsset, Account account) {
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
                .collateralAsset(CollateralAsset.valueOf(collateralAsset))
                .loanStartDate(startDate)
                .dueDate(dueDate)
                .build();
    }
}