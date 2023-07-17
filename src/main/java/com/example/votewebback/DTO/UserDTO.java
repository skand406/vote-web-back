package com.example.votewebback.DTO;

import com.example.votewebback.Entity.UserEntity;
import com.example.votewebback.security.PermissonRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDTO implements UserDetails {
    private UserEntity user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(PermissonRole.USER.getValue()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getUserpassword();
    }

    @Override
    public String getUsername() {
        return user.getUserid();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
