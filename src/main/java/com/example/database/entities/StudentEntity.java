package com.example.database.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "student")
@Getter
@Setter
public class StudentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @Column(name = "name")
    @NotNull
    @NotEmpty
    private String name;

    @Column(name = "faculty")
    @NotNull
    @NotEmpty
    private String faculty;

    @Column(name = "student_group")
    @NotNull
    private int group;

    @OneToOne
    @JoinColumn(name = "theme", referencedColumnName ="id")
    private ThemeEntity theme;

    @Column(name = "defense_mark")
    private int defenseMark;

    @Column(name = "exam_mark")
    private int examMark;

}
