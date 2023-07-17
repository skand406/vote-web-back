package com.example.votewebback.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="User")
public class UserEntity {
    @Id
    @Column(name="user_id",unique = true)
    private String userid;
    @Column(name="user_password")
    private String userpassword;
    @Column(name="user_name")
    private String username;
    @Column(name="user_tel")
    private String usertel;
    @Column(name="user_email",unique = true)
    private String useremail;
}
