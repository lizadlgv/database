package com.example.database.repo;



import com.example.database.entities.TeacherEntity;
import com.example.database.entities.ThemeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<ThemeEntity,Long> {

    List<ThemeEntity> findAllByTeacher(TeacherEntity teacher);
}
