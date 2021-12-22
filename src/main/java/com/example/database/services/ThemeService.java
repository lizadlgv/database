package com.example.database.services;


import com.example.database.entities.StudentEntity;
import com.example.database.entities.TeacherEntity;
import com.example.database.entities.ThemeEntity;
import com.example.database.repo.StudentRepository;
import com.example.database.repo.TeacherRepository;
import com.example.database.repo.ThemeRepository;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ThemeService {
    private final ThemeRepository themeRepository;
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ThemeService(ThemeRepository themeRepository, TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.themeRepository = themeRepository;
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    public ThemeEntity createTheme() {
        return new ThemeEntity();
    }

    public List<ThemeEntity> loadThemes() {
        return themeRepository.findAll();
    }

    public List<TeacherEntity> loadTeachers() {
        return teacherRepository.findAll();
    }

    public void save(ThemeEntity theme) {
        themeRepository.save(theme);
    }

    public void delete(ThemeEntity theme) {
        if(theme != null) {
            List<StudentEntity> students = studentRepository.findAllByTheme(theme);
            if(students.size() == 0) {
                themeRepository.delete(theme);
            } else {
                StringBuilder sb = new StringBuilder();
                students.forEach(student-> sb.append(student.getName()).append("\n"));
                Notification notification = new Notification("Данная тема уже используется студентами:"+ sb,5000);
                notification.open();
            }
        } else {
            Notification notification = new Notification("Невозможно удалить пустую тему",5000);
            notification.open();
        }
    }
}
