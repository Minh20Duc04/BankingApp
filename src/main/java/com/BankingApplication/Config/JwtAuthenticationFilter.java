package com.BankingApplication.Config;

import com.BankingApplication.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Lấy token từ 1 request
        String jwtToken = request.getHeader("Authorization");
        // Kiểm tra xem token có hợp lệ hay không
        if(jwtToken == null || !jwtToken.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }
        // Loại bỏ đoạn Bearer ở phía trước, chỉ lấy phần token
        jwtToken = jwtToken.substring(7);
        if(!jwtService.isTokenValid(jwtToken))
        {
            filterChain.doFilter(request, response);
            return;
        }
        // Nếu token hợp lệ thì lấy User từ token đó
        String username = jwtService.extractSubject(jwtToken);
        var user = userDetailsService.loadUserByUsername(username);

        if(SecurityContextHolder.getContext().getAuthentication() == null)
        {
            var authenticationToken = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            authenticationToken.setDetails(request);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }
}
