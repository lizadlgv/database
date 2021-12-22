package com.example.database.repo;


import com.example.database.entities.StudentEntity;
import com.example.database.entities.ThemeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<StudentEntity,Long> {

    List<StudentEntity> findAllByTheme(ThemeEntity theme);
}
