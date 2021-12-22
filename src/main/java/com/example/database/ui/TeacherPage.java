package com.example.database.ui;


import com.example.database.entities.TeacherEntity;
import com.example.database.services.TeacherService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@PageTitle("Преподаватели")
@Route(value = "teachers", layout = RootLayout.class)
public class TeacherPage extends VerticalLayout {
    private final Grid<TeacherEntity> grid = new Grid<>();
    private final TeacherService teacherService;
    private final TeacherEditor editor;
    private final HorizontalLayout masterDetail = new HorizontalLayout();
    private final List<TeacherEntity> items = new ArrayList<>();
    private Button create;

    public TeacherPage(TeacherService teacherService) {
        this.teacherService = teacherService;
        editor = new TeacherEditor();
        this.editor.setVisible(false);
        this.editor.setDeleteAction(this::delete);
        items.addAll(teacherService.load());
        initTable();
        initLayout();
    }

    private void initTable() {
        grid.addColumn(TeacherEntity::getName)
                .setHeader("ФИО")
                .setSortable(true);
        grid.addColumn(TeacherEntity::getDegree)
                .setHeader("Степень")
                .setSortable(true);
        grid.addColumn(TeacherEntity::getRank)
                .setHeader("Звание")
                .setSortable(true);
        grid.addColumn(TeacherEntity::getDepartment)
                .setHeader("Кафедра")
                .setSortable(true);
        grid.addColumn(TeacherEntity::getPhoneNumber)
                .setHeader("Номер")
                .setSortable(true);
        grid.addColumn(TeacherEntity::getEmail)
                .setHeader("E-mail")
                .setSortable(true);

        grid.addItemDoubleClickListener(event -> {
            editor.openUpdate(event.getItem(), this::update);
            create.setVisible(false);
            refresh();
        });
        grid.setItems(items);
        grid.addClassName("common-grid");
    }

    private void initLayout() {
        create = new Button("Добавить", e -> {
            editor.openCreate(teacherService.createTeacher(), this::create);
            refresh();
        });
        masterDetail.addClassName("master-detail");
        masterDetail.add(grid, editor);
        add(masterDetail, create);

    }

    private void create(TeacherEntity teacher) {
        teacherService.save(teacher);
        refresh();
    }

    private void update(TeacherEntity teacher) {
        teacherService.save(teacher);
        refresh();
    }

    private void delete(TeacherEntity teacher) {
        teacherService.delete(teacher);
        refresh();
    }

    private void refresh() {
        grid.setItems(teacherService.load());
        create.setVisible(true);
    }

    public static class TeacherEditor extends VerticalLayout {
        private final Button save = new Button("Сохранить");
        private final Button cancel = new Button("Отмена");
        private final Button delete = new Button("Удалить");
        private final TextField name = new TextField("Имя");
        private final TextField degree = new TextField("Степень");
        private final TextField rank = new TextField("Звание");
        private final TextField pulpit = new TextField("Кафедра");
        private final TextField phone = new TextField("Телефон");
        private final TextField email = new TextField("E-mail");
        private final HorizontalLayout toolBar = new HorizontalLayout(delete, save, cancel);
        private final HorizontalLayout items = new HorizontalLayout(name);
        private final HorizontalLayout items2 = new HorizontalLayout(degree, rank);
        private final HorizontalLayout items3 = new HorizontalLayout(pulpit, email);
        private final HorizontalLayout items4 = new HorizontalLayout(phone);

        private final Binder<TeacherEntity> binder = new Binder<>(TeacherEntity.class);
        private TeacherEntity buffer;
        private Consumer<TeacherEntity> action;
        private Consumer<TeacherEntity> deleteAction;

        public TeacherEditor() {
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
                    .bind(TeacherEntity::getName, TeacherEntity::setName);
            name.setWidth("100%");

            binder.forField(degree)
                    .bind(TeacherEntity::getDegree, TeacherEntity::setDegree);
            degree.setWidth("100%");

            binder.forField(rank)
                    .bind(TeacherEntity::getRank, TeacherEntity::setRank);
            rank.setWidth("100%");

            binder.forField(pulpit)
                    .asRequired("Кафедра не может быть пустой")
                    .bind(TeacherEntity::getDepartment, TeacherEntity::setDepartment);
            pulpit.setWidth("100%");

            binder.forField(phone)
                    .asRequired("Номер телефона не может быть пустым")
                    .bind(TeacherEntity::getPhoneNumber, TeacherEntity::setPhoneNumber);
            phone.setWidth("100%");

            binder.forField(email)
                    .bind(TeacherEntity::getEmail, TeacherEntity::setEmail);
            email.setWidth("100%");

            save.addClickListener(event -> {
                if (binder.writeBeanIfValid(buffer)) {
                    action.accept(buffer);
                    this.setVisible(false);
                }
            });

            delete.addClickListener(click -> {
                deleteAction.accept(buffer);
            });
            delete.addThemeName("error");

            save.addClickShortcut(Key.ENTER);
            add(items, items2, items3, items4, toolBar);
        }


        public void open(TeacherEntity teatcher) {
            buffer = teatcher;
            binder.readBean(buffer);
        }

        public void openCreate(TeacherEntity teatcher, Consumer<TeacherEntity> action) {
            this.action = action;
            this.setVisible(true);
            delete.setVisible(false);
            save.setText("Сохранить");
            open(teatcher);
        }

        public void openUpdate(TeacherEntity teatcher, Consumer<TeacherEntity> action) {
            this.action = action;
            this.setVisible(true);
            delete.setVisible(true);
            save.setText("Обновить");
            open(teatcher);
        }

        public void setDeleteAction(Consumer<TeacherEntity> deleteAction) {
            this.deleteAction = deleteAction;
        }
    }


}
