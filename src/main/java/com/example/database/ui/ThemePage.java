package com.example.database.ui;


import com.example.database.entities.TeacherEntity;
import com.example.database.entities.ThemeEntity;
import com.example.database.services.ThemeService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@PageTitle("Темы")
@Route(value = "theme", layout = RootLayout.class)
public class ThemePage extends VerticalLayout {
    private final Grid<ThemeEntity> grid = new Grid<>();
    private final ThemeService themeService;
    private final ThemeEditor editor;
    private final HorizontalLayout masterDetail = new HorizontalLayout();
    private final List<ThemeEntity> items = new ArrayList<>();
    private Button create;

    @Autowired
    public ThemePage(ThemeService themeService) {
        this.themeService = themeService;
        this.editor = new ThemeEditor();
        this.editor.setVisible(false);
        this.editor.setTeacherItems(themeService.loadTeachers());
        this.editor.setDeleteConsumer(this::remove);
        initTable();
        initLayout();
    }

    private void initTable() {
        grid.addColumn(ThemeEntity::getTitle)
                .setHeader("Тема")
                .setSortable(true);

        grid.addColumn(ThemeEntity::getTeacher)
                .setHeader("Преподаватель")
                .setSortable(true);

        grid.addItemDoubleClickListener(event -> {
            editor.openUpdate(event.getItem(), this::update);
            create.setVisible(false);
            refresh();
        });
        items.addAll(themeService.loadThemes());
        grid.setItems(items);
        grid.addClassName("common-grid");
    }

    private void initLayout() {
        create = new Button("Добавить", e -> {
            editor.openCreate(themeService.createTheme(), this::create);
            refresh();
        });
        masterDetail.addClassName("master-detail");
        masterDetail.add(grid, editor);
        add(masterDetail, create);
    }

    private void create(ThemeEntity themeEntity) {
        themeService.save(themeEntity);
        refresh();
    }

    private void update(ThemeEntity themeEntity) {
        themeService.save(themeEntity);
        refresh();
    }

    private void remove(ThemeEntity themeEntity) {
        themeService.delete(themeEntity);
        refresh();
    }

    private void refresh() {
        grid.setItems(themeService.loadThemes());
        create.setVisible(true);
    }

    public static class ThemeEditor extends VerticalLayout {
        private final Button save = new Button("Сохранить");
        private final Button cancel = new Button("Отмена");
        private final Button remove = new Button("Удалить");
        private final TextField title = new TextField("Название");
        private final ComboBox<TeacherEntity> teacher = new ComboBox<>("Преподаватель");
        private final HorizontalLayout toolBar = new HorizontalLayout(remove, save, cancel);
        private final VerticalLayout items = new VerticalLayout(title, teacher);

        private final Binder<ThemeEntity> binder = new Binder<>(ThemeEntity.class);
        private ThemeEntity buffer;
        private Consumer<ThemeEntity> action;
        private Consumer<ThemeEntity> deleteAction;

        public ThemeEditor() {
            addClassName("master-detail-editor");
            items.setWidth("100%");
            items.setPadding(false);


            cancel.addClassName("cancel");
            cancel.addClickListener(e ->
                    this.setVisible(false));

            binder.forField(title)
                    .asRequired("Имя не может быть пустым")
                    .bind(ThemeEntity::getTitle, ThemeEntity::setTitle);
            title.setWidth("100%");

            binder.forField(teacher)
                    .asRequired("Преподаватель не может быть пустым")
                    .bind(ThemeEntity::getTeacher, ThemeEntity::setTeacher);
            teacher.setWidth("100%");

            save.addClickListener(event -> {
                if (binder.writeBeanIfValid(buffer)) {
                    action.accept(buffer);
                    this.setVisible(false);

                }
            });

            remove.setVisible(false);
            remove.addThemeName("error");
            remove.addClickListener(click -> deleteAction.accept(buffer));
            save.addClickShortcut(Key.ENTER);
            add(items, toolBar);
        }


        public void open(ThemeEntity themeEntity) {
            buffer = themeEntity;
            binder.readBean(buffer);
        }

        public void openCreate(ThemeEntity themeEntity, Consumer<ThemeEntity> action) {
            this.action = action;
            this.setVisible(true);
            remove.setVisible(false);
            save.setText("Сохранить");
            open(themeEntity);
        }

        public void openUpdate(ThemeEntity themeEntity, Consumer<ThemeEntity> action) {
            this.action = action;
            this.setVisible(true);
            save.setText("Обновить");
            remove.setVisible(true);
            open(themeEntity);
        }

        public void setTeacherItems(List<TeacherEntity> teacherItems) {
            teacher.setItems(teacherItems);
        }

        public void setDeleteConsumer(Consumer<ThemeEntity> delete) {
            this.deleteAction = delete;
        }
    }
}
