package com.BankingApplication.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConvertDto {

    private String fromCurrency;

    private String toCurrency;

    private double amount;

}
