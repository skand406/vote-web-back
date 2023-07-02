package com.example.votewebback.Entity;


import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Embeddable
public class VoteStudentIdKey implements Serializable {
    private String voteid;
    private String studentid;
}
