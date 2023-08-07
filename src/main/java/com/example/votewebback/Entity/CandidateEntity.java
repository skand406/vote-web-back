package com.example.votewebback.Entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@IdClass(VoteStudentIdKey.class)
@Table(name="Candidate")
public class CandidateEntity {
    @Id
    @MapsId("candidate_id")
    private String candidateid;

    @Id
    @ManyToOne
    @MapsId("vote_id")
    @JoinColumn(name = "vote_id")
    private VoteEntity voteid;


    @Column(name = "candidate_spec")
    private String candidatespec;
    @Column(name = "candidate_promise")
    private String candidatepromise;
    @Column(name = "candidate_counter")
    private int candidatecounter;
    @Column(name = "img_path")
    private String imgpath;
}
