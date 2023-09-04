package com.example.votewebback.Entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder(toBuilder = true)
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(VoteStudentIdKey.class)
@Table(name="Elector")
public class ElectorEntity {
    @Id
    @ManyToOne
    @MapsId("student_id")
    @JoinColumn(name="student_id")
    private StudentEntity studentid;
    @Id
    @ManyToOne
    @MapsId("vote_id")
    @JoinColumn(name = "vote_id")
    private VoteEntity voteid;

    @Column(name = "email_confirm")
    private String emailconfirm;
    @Column(name = "vote_confirm")
    private boolean voteconfirm;





}
