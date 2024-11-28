package com.BankingApplication.Repository;

import com.BankingApplication.Model.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {
    Optional<Card> findByOwnerUid(String uid);

    boolean existsByCardNumber(long cardNumber);
}
