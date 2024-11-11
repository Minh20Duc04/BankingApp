package com.BankingApplication.Model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "card")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String cardId;

    @Column(unique = true, nullable = false)
    private long cardNumber;

    private String cardHolder;

    private Double balance;

    @CreationTimestamp
    private LocalDateTime iss;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime exp;

    private String cvv;

    private String pin;

    private String billingAddress;

    @OneToOne()
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Transaction> transactions;

}
