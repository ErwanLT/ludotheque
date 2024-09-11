package fr.eletutour.ludotheque.views.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;
import fr.eletutour.ludotheque.dao.bean.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UserForm extends FormLayout {
    Binder<AppUser> binder = new BeanValidationBinder<>(AppUser.class);
    TextField username = new TextField("Username");
    PasswordField password = new PasswordField("Password");
    ComboBox<String> role = new ComboBox<>("Role");

    Button save = new Button("Sauvegarder");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");

    private PasswordEncoder passwordEncoder;

    private AppUser user;
    private String beforeEditPassword;
    private boolean isEditing = false;

    public UserForm(){

        role.setItems("ROLE_ADMIN", "ROLE_USER");

        binder.bindInstanceFields(this);
        binder.forField(password)
                .withValidator(pass -> isEditing || !pass.isEmpty(), "Le mot de passe ne peut pas être vide")
                .bind(AppUser::getPassword, AppUser::setPassword);

        add(username, password, role, createButtonsLayout());
    }

    public void setPasswordEncoder(PasswordEncoder passwordEncoder){
        this.passwordEncoder = passwordEncoder;
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, user)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(user);
            if (isEditing) {
                if (!password.isEmpty()) {
                    user.setPassword(passwordEncoder.encode(password.getValue()));
                } else {
                    user.setPassword(beforeEditPassword);
                }
            } else {
                user.setPassword(passwordEncoder.encode(password.getValue()));
            }
            fireEvent(new SaveEvent(this, user));
        } catch (ValidationException e) {
            Notification.show("Erreur de validation des données");
        }
    }

    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private final AppUser user;

        public UserFormEvent(UserForm source, AppUser user) {
            super(source, false);
            this.user = user;
        }

        public AppUser getUser() {
            return user;
        }
    }

    public static class SaveEvent extends UserForm.UserFormEvent {
        SaveEvent(UserForm source, AppUser user) {
            super(source, user);
        }
    }

    public static class DeleteEvent extends UserForm.UserFormEvent {
        DeleteEvent(UserForm source, AppUser user) {
            super(source, user);
        }
    }

    public static class CloseEvent extends UserForm.UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user, String beforeEditPassword, boolean isEditing) {
        this.user = user;
        this.beforeEditPassword = beforeEditPassword;
        this.isEditing = isEditing;
        binder.readBean(user);

        if (isEditing) {
            password.clear();
            password.setPlaceholder("Laisser vide pour conserver le mot de passe actuel");
        }
    }
}
