package com.example.votewebback.security;

import com.example.votewebback.DTO.UserDTO;
import com.example.votewebback.Service.UserService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class LoginAuthenticationProvider implements AuthenticationProvider {
    private UserDetailsService userService;
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDTO user=(UserDTO) userService.loadUserByUsername(authentication.getName().toString());
        String reqPassword = authentication.getCredentials().toString();
        if(!passwordEncoder.matches(reqPassword, user.getPassword())) throw new BadCredentialsException("Not Found User");
        return new UsernamePasswordAuthenticationToken(user, null,user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
