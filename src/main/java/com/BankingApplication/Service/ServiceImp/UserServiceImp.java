package com.BankingApplication.Service.ServiceImp;

import com.BankingApplication.Dto.UserDto;
import com.BankingApplication.Model.User;
import com.BankingApplication.Repository.UserRepository;
import com.BankingApplication.Service.JwtService;
import com.BankingApplication.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager; //là phần được cấu hình bên AppConfig
    private final JwtService jwtService;

    public User registerUser(UserDto userDto)
    {
        User user = mapToUser(userDto);
        return userRepository.save(user);
    }

    public Map<String, Object> authenticateUser(UserDto userDto)
    {
        Map<String, Object> authenticatedUser = new HashMap<String, Object>(); //tạo 1 map có 2 dữ liệu cho 2 phần cho header và body
        User user = (User) userDetailsService.loadUserByUsername(userDto.getUsername());
        if(user == null)
        {
            throw new UsernameNotFoundException("User not found");
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getUsername(), userDto.getPassword()));
        authenticatedUser.put("token", "Bearer".concat(jwtService.generateToken(userDto.getUsername())));
        authenticatedUser.put("user", user);
        return authenticatedUser;
    }

    private User mapToUser(UserDto userDto)
    {
        return User.builder()
                .firstName(userDto.getFirstname())
                .lastName(userDto.getLastName())
                .dob(userDto.getDob())
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .tel(userDto.getTel())
                .gender(userDto.getGender())
                .tag("io_" + userDto.getUsername())
                .roles(List.of("USER"))
                .build();
    }
}
