package com.example.votewebback.Entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="User")
public class UserEntity implements UserDetails{
    @Id
    @Column(name="user_id",unique = true)
    private String userid; //username
    @Column(name="user_password")
    private String userpassword;
    @Column(name="user_name")
    private String username;
    @Column(name="user_tel")
    private String usertel;
    @Column(name="user_email",unique = true)
    private String useremail;


    @Override //권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }

    @Override//사용자 id 반환
    public String getUsername() {
        return userid;
    }
    @Override//사용자 비번 반환
    public String getPassword() {
        return userpassword;
    }

    @Override//계정 만료 여부
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override//계정 잠금 여부
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override//패스워드 만료 여부
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override//계정 사용 가능 여부
    public boolean isEnabled() {
        return true;
    }
}
