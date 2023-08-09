package com.example.votewebback.Entity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="Vote")
public class VoteEntity {
    @ManyToOne
    @JoinColumn(name="user_id")
    private UserEntity userid;


    @Column(name = "vote_name")
    private String votename;
    @Column(name = "vote_active")
    private boolean voteactive;

    @Id
    @Column(name = "vote_id")
    private String voteid;
    @Column(name = "vote_bundle_id")
    private String votebundleid;
    @Column(name = "major")
    private String major;
    @Column(name = "grade")
    private int grade;
    @Column(name = "vote_type")
    private VoteType votetype;
    @Column(name = "start_date")
    private LocalDate startdate;
    @Column(name = "end_date")
    private LocalDate enddate;
    @Column(name = "last_end_date")
    private LocalDate lastenddate;


}

