package com.example.votewebback.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(VoteStudentIdKey.class)
@Table(name="Candidate")
public class Candidate {
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


    @Column(name = "candidate_spec")
    private String candidatespec;
    @Column(name = "candidate_promise")
    private String candidatepromise;

    @Column(name = "vote_counter")
    private int votecounter;
    @Column(name = "img_path")
    private String imgpath;
}
