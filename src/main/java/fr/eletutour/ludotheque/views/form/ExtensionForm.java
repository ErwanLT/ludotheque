package fr.eletutour.ludotheque.views.form;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.shared.Registration;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;

import java.io.IOException;
import java.time.Duration;

public class ExtensionForm extends FormLayout {

    private JeuSociete jeuPrincipal;
    private JeuSociete extension;

    private Binder<JeuSociete> binder = new BeanValidationBinder<>(JeuSociete.class);

    private TextField nom = new TextField("Nom de l'extension");
    private NumberField minJoueur = new NumberField("Nombre de joueur minimum");
    private NumberField maxJoueur = new NumberField("Nombre de joueur maximum");
    private NumberField ageMinimum = new NumberField("Âge minimum requis");
    private NumberField tempsDeJeu = new NumberField("Temps de jeu (minutes)");
    Upload imageUpload = new Upload(new MemoryBuffer());

    private Button save = new Button("Ajouter Extension");
    private Button close = new Button("Annuler");

    public ExtensionForm(JeuSociete jeuPrincipal, JeuSociete extension) {
        this.jeuPrincipal = jeuPrincipal;
        this.extension = extension;

        MemoryBuffer buffer = new MemoryBuffer();
        imageUpload.setReceiver(buffer);
        imageUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imageUpload.addSucceededListener(event -> {
            try {
                extension.setImage(buffer.getInputStream().readAllBytes());
            } catch (IOException e) {
                Notification.show("Erreur lors du téléchargement de l'image: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
                e.printStackTrace();
            }
        });

        binder.forField(minJoueur)
                .withConverter(new DoubleToIntegerConverter())
                .bind(JeuSociete::getNombreJoueursMin, JeuSociete::setNombreJoueursMin);
        binder.forField(maxJoueur)
                .withConverter(new DoubleToIntegerConverter())
                .bind(JeuSociete::getNombreJoueursMax, JeuSociete::setNombreJoueursMax);
        binder.forField(ageMinimum)
                .withConverter(new DoubleToIntegerConverter())
                .bind(JeuSociete::getAgeMinimum, JeuSociete::setAgeMinimum);
        binder.forField(tempsDeJeu)
                .withConverter(new DoubleToLongConverter())
                .bind(jeu -> jeu.getTempsDeJeuEnMinutes().toMinutes(),
                        (jeu, value) -> jeu.setTempsDeJeuEnMinutes(Duration.ofMinutes(value)));
        binder.bindInstanceFields(this);

        add(
                nom,
                minJoueur,
                maxJoueur,
                ageMinimum,
                tempsDeJeu,
                imageUpload,
                createButtonsLayout()
        );

        binder.readBean(extension);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(extension);
            jeuPrincipal.getExtensions().add(extension); // Ajout de l'extension à la liste des extensions du jeu principal
            fireEvent(new SaveEvent(this, extension));
        } catch (ValidationException e) {
            Notification.show("Erreur de validation des données");
        }
    }

    public static abstract class ExtensionFormEvent extends ComponentEvent<ExtensionForm> {
        private JeuSociete extension;

        public ExtensionFormEvent(ExtensionForm source, JeuSociete extension) {
            super(source, false);
            this.extension = extension;
        }

        public JeuSociete getExtension() {
            return extension;
        }
    }

    public static class SaveEvent extends ExtensionFormEvent {
        SaveEvent(ExtensionForm source, JeuSociete extension) {
            super(source, extension);
        }
    }

    public static class CloseEvent extends ExtensionFormEvent {
        CloseEvent(ExtensionForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }

    private static class DoubleToIntegerConverter implements Converter<Double, Integer> {
        @Override
        public Result<Integer> convertToModel(Double value, ValueContext context) {
            return value == null ? Result.ok(null) : Result.ok(value.intValue());
        }

        @Override
        public Double convertToPresentation(Integer value, ValueContext context) {
            return value == null ? null : value.doubleValue();
        }
    }

    private static class DoubleToLongConverter implements Converter<Double, Long> {
        @Override
        public Result<Long> convertToModel(Double value, ValueContext context) {
            return value == null ? Result.ok(null) : Result.ok(value.longValue());
        }

        @Override
        public Double convertToPresentation(Long value, ValueContext context) {
            return value == null ? null : value.doubleValue();
        }
    }
}
