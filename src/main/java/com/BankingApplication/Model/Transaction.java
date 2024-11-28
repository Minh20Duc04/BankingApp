package com.BankingApplication.Model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "transaction")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String txId;

    private Double amount;

    private Double txFee;

    private String sender;

    private String receiver;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private Type type;

    @ManyToOne()
    @JoinColumn(name = "card_id")
    private Card card;


    @ManyToOne()
    @JoinColumn(name = "owner_id")
//    @JsonBackReference
    @JsonIgnore
    private User owner;

    @ManyToOne
    @JoinColumn(name = "account_id")
//    @JsonBackReference
    @JsonIgnore
    private Account account;

}
