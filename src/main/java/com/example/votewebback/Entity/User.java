package com.example.votewebback.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(VoteStudentIdKey.class)
@Table(name="User")
public class User {

    @Column(name = "email_confirm")
    private String emailconfirm;
    @Column(name = "vote_confirm")
    private boolean voteconfirm;


    @Id
    @ManyToOne
    @MapsId("student_id")
    @JoinColumn(name="student_id")
    private Student studentid;
    @Id
    @ManyToOne
    @MapsId("vote_id")
    @JoinColumn(name = "vote_id")
    private Vote voteid;


}
