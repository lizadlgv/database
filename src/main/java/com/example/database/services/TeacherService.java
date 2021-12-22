package com.example.database.services;


import com.example.database.entities.TeacherEntity;
import com.example.database.entities.ThemeEntity;
import com.example.database.repo.TeacherRepository;
import com.example.database.repo.ThemeRepository;
import com.vaadin.flow.component.notification.Notification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final ThemeRepository themeRepository;

    @Autowired
    public TeacherService(TeacherRepository teacherRepository, ThemeRepository themeRepository) {
        this.teacherRepository = teacherRepository;
        this.themeRepository = themeRepository;
    }

    public TeacherEntity createTeacher() {
        return new TeacherEntity();
    }

    public void save(TeacherEntity teacherEntity) {
        teacherRepository.save(teacherEntity);
    }

    public void delete(TeacherEntity teacher) {
        if(teacher != null) {
            List<ThemeEntity> themes = themeRepository.findAllByTeacher(teacher);
            if(themes.size() == 0) {
                teacherRepository.delete(teacher);
            } else {
                StringBuilder sb = new StringBuilder();
                themes.forEach(theme-> sb.append(theme.getTitle()).append("\n"));
                Notification notification = new Notification("Данный преподаватель связан с существующей темой:"+ sb,5000);
                notification.open();
            }
        } else {
            Notification notification = new Notification("Невозможно удалить пустого преподавателя",5000);
            notification.open();
        }
    }

    public List<TeacherEntity> load() {
        return teacherRepository.findAll();
    }
}
