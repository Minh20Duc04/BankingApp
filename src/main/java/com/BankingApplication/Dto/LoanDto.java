package com.BankingApplication.Dto;

import com.BankingApplication.Model.CollateralAsset;
import com.BankingApplication.Model.Status;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class LoanDto {

    @NotNull(message = "Loan amount cannot be null")
    private Double loanAmount;

    @NotNull(message = "Interest rate is required")
    @DecimalMin(value = "0.01", message = "Interest rate must be at least 0.01")
    @DecimalMax(value = "1.0", message = "Interest rate cannot be more than 1.0")
    private Double interestRate;

    @NotNull(message = "Loan term is required")
    @Min(value = 1, message = "Loan term must be at least 1 month")
    @Max(value = 360, message = "Loan term cannot exceed 360 months")
    private Integer loanTerm;

    @NotNull(message = "Account number is required")
    private Long accountNumber;

}
