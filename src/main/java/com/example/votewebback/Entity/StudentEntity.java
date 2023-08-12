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
@Table(name="student")
public class StudentEntity {
    @Id
    @Column(name = "student_id")
    private String studentid;
    @Column(name = "student_name")
    private String studentname;
    @Column(name = "student_major")
    private String studentmajor;
    @Column(name = "student_grade")
    private int studentgrade;
    @Column(name = "student_email",unique = true)
    private String studentemail;
    @Column(name = "student_tel")
    private String studenttel;
}
