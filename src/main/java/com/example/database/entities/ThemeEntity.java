package com.example.database.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "theme")
@Getter
@Setter
public class ThemeEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NotNull
    private Long id;

    @Column(name = "title")
    @NotNull
    private String title;

    @OneToOne
    @JoinColumn(name="teacher",referencedColumnName = "id")
    @NotNull
    private TeacherEntity teacher;

    @Override
    public String toString() {
        return title;
    }
}
