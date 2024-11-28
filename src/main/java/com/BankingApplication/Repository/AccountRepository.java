package com.BankingApplication.Repository;

import com.BankingApplication.Model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByAccountNumber(Long accountNumber);

    boolean existsByCodeAndOwnerUid(String code, String uId);

    List<Account> findAllByOwnerUid(String uId);

    @Query("SELECT a FROM Account a WHERE a.code = :code AND a.owner.uid = :uid AND a.balance = (SELECT MAX(b.balance) FROM Account b WHERE b.code = :code AND b.owner.uid = :uid)")
    Optional<Account> findTopByCodeAndOwnerUidOrderByBalanceDesc(@Param("code") String code, @Param("uid") String uid);

    Optional<Account> findByAccountNumber(Long accountNumber);

    Optional<Account> findByCodeAndOwnerUid(String code, String uid);
}
