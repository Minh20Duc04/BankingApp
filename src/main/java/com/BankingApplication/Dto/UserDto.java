package com.BankingApplication.Dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserDto {

    private String firstname;

    private String lastName;

    private String username;

    private Date dob;

    private long tel;

    private String tag;

    private String password;

    private String gender;

}