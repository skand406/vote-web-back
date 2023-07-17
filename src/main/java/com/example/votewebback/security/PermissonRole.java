package com.example.votewebback.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

@Getter
public enum PermissonRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    ELECTOR("ROLE_ELETOR");

    PermissonRole(String value){
        this.value=value;
    }

    private String value;
}
