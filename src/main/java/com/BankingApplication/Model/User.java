package com.BankingApplication.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "bank_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uId;

    private String lastName;

    @Column(unique = true, nullable = false)
    private String userName;

    private Date dob;

    private long tel;

    private String tag;

    @JsonIgnore
    private String password;

    private String gender;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private List<String> roles;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.EAGER)
    private Card card;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner", fetch = FetchType.LAZY)
    private List<Account> accounts;


}
