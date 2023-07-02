package com.example.votewebback.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name="Vote")
public class Vote {
    @ManyToOne
    @JoinColumn(name="manager_id")
    private Manager managerid;

    @Column(name = "manager_name")
    private String managername;
    @Column(name = "manager_tel")
    private String managertel;
    @Column(name = "vote_name")
    private String votename;
    @Column(name = "vote_active")
    private boolean voteactive;

    @Id
    @Column(name = "vote_id")
    private String voteid;
    @Column(name = "student_major")
    private String studentmajor;
    @Column(name = "student_grade")
    private int studentgrade;

    @Column(name = "start_date")
    private LocalDate startdate;
    @Column(name = "end_date")
    private LocalDate enddate;
    @Column(name = "last_end_date")
    private LocalDate lastenddate;
}

