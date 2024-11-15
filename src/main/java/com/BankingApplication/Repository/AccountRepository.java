package com.BankingApplication.Repository;

import com.BankingApplication.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByAccountNumber(Long accountNumber);

    boolean existsByCodeAndOwnerUid(String code, String uId);

    List<Account> findAllByOwnerUid(String uId);
}
