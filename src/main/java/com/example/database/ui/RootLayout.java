package com.example.database.ui;


import com.example.database.settings.User;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;

@CssImport("./styles/main.css")
public class RootLayout extends VerticalLayout implements RouterLayout {
    private final HorizontalLayout header = new HorizontalLayout();
    private final Label title = new Label();
    private final VerticalLayout content = new VerticalLayout();

    public RootLayout() {
        setPadding(false);
        setAlignItems(Alignment.CENTER);

        header.addClassName("header");

        HorizontalLayout menu = new HorizontalLayout();
        menu.addClassName("header-menu");
        menu.add(new RouterLink("Студенты", StudentPage.class));
        menu.add(new RouterLink("Преподаватели", TeacherPage.class));
        menu.add(new RouterLink("Темы", ThemePage.class));


        User user = (User) VaadinSession.getCurrent().getAttribute("user");
        Button button = new Button(user.getName());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.setIcon(VaadinIcon.USER.create());
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);

        ContextMenu contextMenu = new ContextMenu();
        contextMenu.setOpenOnClick(true);
        contextMenu.addItem("Выход", event -> {
            VaadinSession.getCurrent().close();
        });

        contextMenu.setTarget(button);

        HorizontalLayout headerWrapper = new HorizontalLayout();
        headerWrapper.setPadding(false);
        headerWrapper.add(header);
        headerWrapper.addClassName("header-wrapper");

        Icon mainIcon = new Icon(VaadinIcon.ARROW_CIRCLE_DOWN);

        header.add(mainIcon, title, menu, button);

        content.addClassName("content");
        content.setPadding(false);
        content.setSpacing(false);


        add(headerWrapper, content);
    }

    @Override
    public void showRouterLayoutContent(HasElement content) {
        this.content.getElement().appendChild(content.getElement());

        PageTitle pageTitle = content.getClass().getAnnotation(PageTitle.class);
        if (pageTitle != null) {
            title.setText(pageTitle.value());
        } else {
            title.setText("");
        }
    }
}

