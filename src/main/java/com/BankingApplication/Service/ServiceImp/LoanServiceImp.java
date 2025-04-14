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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class LoanServiceImp implements LoanService {

    private final LoanRepository loanRepository;
    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;
    private final JavaMailSender mailSender;
    private Logger loggerFactory = LoggerFactory.getLogger(LoanServiceImp.class);

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public Loan createLoan(LoanDto loanDto, User user, String collateralAsset) throws Exception {
        Account account = accountRepository.findByAccountNumberAndOwnerUid(loanDto.getAccountNumber(), user.getUid()).orElseThrow(() -> new IllegalArgumentException("The user with this accountNumber is not existed"));
        accountHelper.validateAmount(loanDto.getLoanAmount());
        Loan loan = createNewLoan(loanDto, collateralAsset, account);
        account.setBalance(account.getBalance() + loan.getLoanAmount());

        sendEmail(account.getOwnerEmail());
        return loanRepository.save(loan);
    }


    @Scheduled(cron = "0 0 0 1 * ?")
    // Mỗi tháng trừ tiền 1 lần, vì Schedule không hỗ trợ lập lịch từng đối tượng nên ta sẽ lập lịch tất cả khoản vay
    private void deductibleScheduling() {
        List<Loan> activeLoans = loanRepository.findByStatus(Status.REPAYING);

        for (Loan loan : activeLoans) {
            Account account = loan.getAccount();
            double payment = loan.getMonthlyPayment();

            //kiểm tra tính hợp lệ
            if (account.getBalance() >= payment) {
                account.setBalance(account.getBalance() - payment);
                loan.setRemainingAmount(loan.getRemainingAmount() - payment);

                //trả xong thì xóa những thao tác vay tiền
                if (loan.getRemainingAmount() <= 0) {
                    loan.setStatus(Status.COMPLETED);
                    loan.setCollateralAsset(null);
                }
                accountRepository.save(account);
                loanRepository.save(loan);
            } else {
                log.warn("Your account can't effort the loan, you have lost the collateralAsset", account.getAccountName());
            }
        }

    }

    private Loan createNewLoan(LoanDto loanDto, String collateralAsset, Account account) {
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime dueDate = startDate.plusMonths((long) loanDto.getLoanTerm());

        double monthlyPayment = (loanDto.getLoanAmount() * (1 + loanDto.getInterestRate())) / loanDto.getLoanTerm();
        double remainingAmount = loanDto.getLoanAmount() * (1 + loanDto.getInterestRate());

        return Loan.builder()
                .loanAmount(loanDto.getLoanAmount())
                .interestRate(loanDto.getInterestRate())
                .loanTerm(loanDto.getLoanTerm())
                .monthlyPayment(monthlyPayment)
                .remainingAmount(remainingAmount)
                .account(account)
                .ownerEmail(account.getOwnerEmail())
                .status(Status.REPAYING)
                .collateralAsset(CollateralAsset.valueOf(collateralAsset))
                .loanStartDate(startDate)
                .dueDate(dueDate)
                .build();
    }

    public void sendEmail(String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Loan Request Approved");
            message.setText("Your loan request has been successfully approved. Please make sure to repay the loan on time to avoid any penalties. If you did not request this loan, please contact our support team immediately.");
            message.setFrom(fromEmail);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace(); // In ra stack trace để kiểm tra lỗi
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}
