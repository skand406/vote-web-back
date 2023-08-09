package com.example.votewebback.Entity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@IdClass(VoteCandidateIdKey.class)
@Table(name="Candidate")
public class CandidateEntity {


    @Id
    @Column(name = "candidate_id")
    private String candidateid;

    @Id
    @ManyToOne
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
