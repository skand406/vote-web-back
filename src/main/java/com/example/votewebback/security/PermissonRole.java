package com.example.votewebback.security;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
@AllArgsConstructor
@Getter
public enum PermissonRole {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER"),
    ELECTOR("ROLE_ELETOR");


    private String value;
}
