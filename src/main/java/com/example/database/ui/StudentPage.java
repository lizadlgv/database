package com.example.database.ui;


import com.example.database.entities.StudentEntity;
import com.example.database.entities.ThemeEntity;
import com.example.database.services.StudentService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@PageTitle("Студенты")
@Route(value = "students", layout = RootLayout.class)
public class StudentPage extends VerticalLayout {
    private final Grid<StudentEntity> grid = new Grid<>();
    private final StudentService studentService;
    private final StudentEditor editor;
    private final HorizontalLayout masterDetail = new HorizontalLayout();
    private final List<StudentEntity> items = new ArrayList<>();

    private Button create;

    public StudentPage(StudentService studentService) {
        this.studentService = studentService;
        editor = new StudentEditor();
        editor.setVisible(false);
        editor.setThemeItems(studentService.loadThemes());
        editor.setRemoveAction(this::delete);
        items.addAll(studentService.loadStudents());
        initTable();
        initLayout();
    }

    private void initTable() {
        grid.addColumn(StudentEntity::getName)
                .setHeader("ФИО")
                .setSortable(true);
        grid.addColumn(StudentEntity::getFaculty)
                .setHeader("Факультет")
                .setSortable(true);
        grid.addColumn(StudentEntity::getGroup)
                .setHeader("Группа")
                .setSortable(true);
        grid.addColumn(StudentEntity::getTheme)
                .setHeader("Тема")
                .setSortable(true);
        grid.addColumn(StudentEntity::getExamMark)
                .setHeader("Оценка(Экзамен)")
                .setSortable(true);
        grid.addColumn(StudentEntity::getDefenseMark)
                .setHeader("Оценка(Защита)")
                .setSortable(true);

        grid.addItemDoubleClickListener(event -> {
            editor.openUpdate(event.getItem(), this::update);
            create.setVisible(false);
            refresh();
        });
        grid.setItems(items);
        grid.addClassName("common-grid");
    }

    public void initLayout() {
        create = new Button("Добавить", e -> {
            editor.openCreate(studentService.createStudent(), this::create);
            refresh();
        });
        masterDetail.addClassName("master-detail");
        masterDetail.add(grid, editor);
        add(masterDetail, create);

    }

    private void create(StudentEntity studentEntity) {
        studentService.save(studentEntity);
        refresh();
    }

    private void update(StudentEntity studentEntity) {
        studentService.save(studentEntity);
        refresh();
    }

    private void delete(StudentEntity studentEntity) {
        studentService.delete(studentEntity);
        refresh();
    }

    public void refresh() {
        grid.setItems(studentService.loadStudents());
        create.setVisible(true);
    }

    public static class StudentEditor extends VerticalLayout {
        private final Button save = new Button("Сохранить");
        private final Button cancel = new Button("Отмена");
        private final Button delete = new Button("Удалить");
        private final TextField name = new TextField("Имя");
        private final TextField faculty = new TextField("Факультет");
        private final IntegerField group = new IntegerField("Группа");
        private final ComboBox<ThemeEntity> themes = new ComboBox<>("Тема");
        private final IntegerField markExam = new IntegerField("Оценка за экзамен");
        private final IntegerField markDefence = new IntegerField("Оценка за защиту");
        private final HorizontalLayout toolBar = new HorizontalLayout(delete, save, cancel);
        private final HorizontalLayout items = new HorizontalLayout(name);
        private final HorizontalLayout items2 = new HorizontalLayout(faculty, group);
        private final HorizontalLayout items3 = new HorizontalLayout(themes);
        private final HorizontalLayout items4 = new HorizontalLayout(markExam, markDefence);

        private Binder<StudentEntity> binder = new Binder<>(StudentEntity.class);
        private StudentEntity buffer;
        private Consumer<StudentEntity> action;
        private Consumer<StudentEntity> deleteAction;

        public StudentEditor() {

            addClassName("master-detail-editor");
            items.setWidth("100%");
            items.setPadding(false);
            items2.setWidth("100%");
            items2.setPadding(false);
            items3.setWidth("100%");
            items3.setPadding(false);
            items4.setWidth("100%");
            items4.setPadding(false);

            cancel.addClassName("cancel");
            cancel.addClickListener(e ->
                    this.setVisible(false));

            binder.forField(name)
                    .asRequired("Имя не может быть пустым")
                    .bind(StudentEntity::getName, StudentEntity::setName);
            name.setWidth("100%");

            binder.forField(faculty)
                    .asRequired("Факультет не может быть пустым")
                    .bind(StudentEntity::getFaculty, StudentEntity::setFaculty);
            faculty.setWidth("100%");

            binder.forField(group)
                    .asRequired("Группа не может быть пустой")
                    .bind(StudentEntity::getGroup, StudentEntity::setGroup);
            group.setWidth("100%");

            binder.forField(markExam)
                    .asRequired("Оценка не может быть пустой")
                    .bind(StudentEntity::getExamMark,
                            StudentEntity::setExamMark);
            markExam.setWidth("100%");

            binder.forField(markDefence)
                    .asRequired("Оценка не может быть пустой")
                    .bind(StudentEntity::getDefenseMark,
                            StudentEntity::setDefenseMark);
            markDefence.setWidth("100%");

            binder.forField(themes)
                    .asRequired("Тема не может быть пустой")
                    .bind(StudentEntity::getTheme, StudentEntity::setTheme);
            themes.setWidth("100%");


            save.addClickListener(event -> {
                if (binder.writeBeanIfValid(buffer)) {
                    action.accept(buffer);
                    name.setValue("");
                    this.setVisible(false);
                }
            });

            delete.addClickListener(click -> deleteAction.accept(buffer));
            delete.addThemeName("error");
            save.addClickShortcut(Key.ENTER);
            add(items, items2, items3, items4, toolBar);
        }

        public void open(StudentEntity student) {
            buffer = student;
            binder.readBean(buffer);
        }

        public void openCreate(StudentEntity student, Consumer<StudentEntity> action) {
            this.action = action;
            this.setVisible(true);
            delete.setVisible(false);
            save.setText("Сохранить");
            open(student);
        }

        public void openUpdate(StudentEntity student, Consumer<StudentEntity> action) {
            this.action = action;
            this.setVisible(true);
            delete.setVisible(true);
            save.setText("Обновить");
            open(student);
        }

        public void setRemoveAction(Consumer<StudentEntity> deleteAction) {
            this.deleteAction = deleteAction;
        }

        public void setThemeItems(List<ThemeEntity> themeItems) {
            themes.setItems(themeItems);
        }
    }
}
