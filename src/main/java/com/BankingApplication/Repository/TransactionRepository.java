package com.BankingApplication.Repository;

import com.BankingApplication.Model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, String> {
    Page<Transaction> findAllByOwnerUid(String uid, Pageable pageable);

    Page<Transaction> findAllByCardCardIdAndOwnerUid(String uid, Pageable pageable, String cardId);

    Page<Transaction> findAllByAccountAccountIdAndOwnerUid(String uid, Pageable pageable, String accountId);

}
