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
@Table(name="Manager")
public class ManagerEntity {
    @Id
    @Column(name="manager_id",unique = true)
    private String managerid;
    @Column(name="manager_password")
    private String managerpassword;
    @Column(name="manager_name")
    private String managername;
    @Column(name="manager_tel")
    private String managertel;
    @Column(name="manager_email",unique = true)
    private String manageremail;
}
