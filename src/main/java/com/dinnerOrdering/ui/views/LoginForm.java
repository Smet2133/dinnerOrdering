package com.dinnerOrdering.ui.views;

import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;


public class LoginForm extends VerticalLayout {

    private static final String LABEL_TEXT = "Войдите или ознакомьтесь <br> с панелью управления <a href=\"/menu\">здесь</a>";
    private static final String NOTIFICATION_TEXT = "Не удалось войти, неверный логин или пароль";

    public LoginForm(LoginCallback callback) {
        setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setMargin(true);
        setSpacing(true);

        Label label = new Label("Пожалуйста, войдите:");
        addComponent(label);

        TextField username = new TextField("Username");
        addComponent(username);

        PasswordField password = new PasswordField("Password");
        addComponent(password);

        Button login = new Button("Login", evt -> {
            String pword = password.getValue();
            password.setValue("");
            if (!callback.login(username.getValue(), pword)) {
                Notification.show("Login failed");
                username.focus();
            }
        });
        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
        addComponent(login);



/*
        CustomLoginView loginView = new CustomLoginView();
        loginView.label().setValue(LABEL_TEXT);

        loginView.button().addClickListener( clickEvent -> {
            String pword = loginView.password().getValue();
            loginView.password().setValue("");
            if (!callback.login(loginView.login().getValue(), pword)) {
                Notification.show(NOTIFICATION_TEXT);
                loginView.login().focus();
            }
        });


        loginView.button().setClickShortcut(ShortcutAction.KeyCode.ENTER);

        addComponent(loginView);
        */
    }

    @FunctionalInterface
    public interface LoginCallback {

        boolean login(String username, String password);
    }
}
