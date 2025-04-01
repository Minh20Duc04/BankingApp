package com.BankingApplication.Service;

import com.BankingApplication.Dto.LoanDto;
import com.BankingApplication.Model.Loan;
import com.BankingApplication.Model.User;

public interface LoanService {

    Loan createLoan(LoanDto loanDto, User user, String collateralAsset) throws Exception;
}
