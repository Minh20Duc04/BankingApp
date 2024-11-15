package com.BankingApplication.Service;

import com.BankingApplication.Dto.UserDto;
import com.BankingApplication.Model.User;

import java.util.Map;

public interface UserService {
    User registerUser(UserDto userDto);
    Map<String, Object> authenticateUser(UserDto userDto);
}
