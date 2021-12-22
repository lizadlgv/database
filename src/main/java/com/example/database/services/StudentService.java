package com.example.database.services;


import com.example.database.entities.StudentEntity;
import com.example.database.entities.ThemeEntity;
import com.example.database.repo.StudentRepository;
import com.example.database.repo.ThemeRepository;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository, ThemeRepository themeRepository) {
        this.studentRepository = studentRepository;
        this.themeRepository = themeRepository;
    }

    public StudentEntity createStudent() {
        return new StudentEntity();
    }

    public void save(StudentEntity student){
        studentRepository.save(student);
    }

    public void delete (StudentEntity student) {
        if(student != null) {
            studentRepository.delete(student);
        } else {
            Notification notification = new Notification("Невозможно удалить пустого студента",5000);
            notification.open();
        }
    }

    public List<StudentEntity> loadStudents() {
        return studentRepository.findAll();
    }

    public List<ThemeEntity> loadThemes() {
        return themeRepository.findAll();
    }
}
