package com.hhymyi.analoguesecondkill.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PersonLog {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "no")
    private int no;

    @Column(name = "kill_result")
    private boolean killResult;

    @Column(name = "kill_info")
    private String killInfo;

}
