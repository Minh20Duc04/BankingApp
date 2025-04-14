package com.BankingApplication.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "loan_contract")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String loanId;

    private double loanAmount;

    @JsonIgnore
    private double remainingAmount;

    private double interestRate;

    private int loanTerm;

    @CreationTimestamp
    private LocalDateTime loanStartDate;

    private double monthlyPayment;

    private LocalDateTime dueDate;

    @JsonIgnore
    private String ownerEmail;

    @ManyToOne
    @JoinColumn(name = "account_id")
    @JsonIgnore
    private Account account;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @Enumerated(value = EnumType.STRING)
    private CollateralAsset collateralAsset;








}
