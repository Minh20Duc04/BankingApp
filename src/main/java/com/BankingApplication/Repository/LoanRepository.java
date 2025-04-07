package com.BankingApplication.Repository;

import com.BankingApplication.Model.Loan;
import com.BankingApplication.Model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, String> {

    List<Loan> findByStatus(Status status);
}
