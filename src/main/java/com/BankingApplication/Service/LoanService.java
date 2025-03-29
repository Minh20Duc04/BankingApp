package com.BankingApplication.Service;

import com.BankingApplication.Dto.LoanDto;
import com.BankingApplication.Model.CollateralAsset;
import com.BankingApplication.Model.Loan;
import com.BankingApplication.Model.User;

public interface LoanService {

    Loan createLoan(LoanDto loanDto, User user, CollateralAsset collateralAsset);
}
