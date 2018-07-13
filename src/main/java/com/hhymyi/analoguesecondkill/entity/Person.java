package com.hhymyi.analoguesecondkill.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Person {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = true, length = 20)
    private String name;

    @Column(name = "age", nullable = true, length = 4)
    private int age;

//    @Version
    @Column(name = "version")
    private int version;
}
