package com.BankingApplication.Config;

import com.BankingApplication.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
@RequiredArgsConstructor
public class AppConfig  { //cấu hình cách tải dữ liệu người dùng

    private final UserRepository userRepository;


    @Bean
    public UserDetailsService userDetailsService() //ko nhận tham số truyền vào vì đang implement UserDetailService chứa th loadByUserName(String userName)
    {
        return userRepository::findByUsernameIgnoreCase;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
    //DaoAuthenticationProvider là một loại AuthenticationProvider cụ thể. Nó được thiết kế để làm việc với các chi tiết người dùng được lưu trữ trong cơ sở dữ liệu và sử dụng mã hóa mật khẩu để xác thực người dùng.
    //DaoAuthenticationProvider sử dụng UserDetailsService để lấy thông tin người dùng và PasswordEncoder để mã hóa mật khẩu
    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        var daoProvider = new DaoAuthenticationProvider(passwordEncoder());
        daoProvider.setUserDetailsService(userDetailsService());
        return daoProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public RestTemplate restTemplate()
    {
        return new RestTemplate();
    }

    @Bean
    public ScheduledExecutorService scheduledExecutorService() //tự động lập lịch 1 tác vụ
    {
        return Executors.newScheduledThreadPool(1);
    }

}
