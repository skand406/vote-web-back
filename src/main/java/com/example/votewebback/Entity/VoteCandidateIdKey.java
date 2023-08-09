package com.example.votewebback.Entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class VoteCandidateIdKey implements Serializable {
    private String voteid;
    private String candidateid;
}
