package com.example.database.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "teacher")
@Getter
@Setter
public class TeacherEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "degree")
    private String degree;

    @Column(name = "rank")
    private String rank;

    @Column(name = "department")
    @NotNull
    private String department;

    @Column(name = "phone")
    @NotNull
    private String phoneNumber;

    @Column(name = "mail")
    private String email;

    @Override
    public String toString() {
        return  name;
    }
}
