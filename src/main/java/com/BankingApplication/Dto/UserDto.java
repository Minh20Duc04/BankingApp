package com.BankingApplication.Dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Username is required")
    @Size(min = 5, max = 25, message = "Username must not to long")
    private String username;

    private Date dob;

    private long tel;

    private String email;

    private String tag;

    @NotBlank(message = "Password is required")
    private String password;

    private String gender;

}
