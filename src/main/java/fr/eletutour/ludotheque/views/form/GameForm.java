package fr.eletutour.ludotheque.views.form;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.data.converter.Converter;
import com.vaadin.flow.shared.Registration;
import elemental.json.Json;
import fr.eletutour.ludotheque.dao.bean.JeuSociete;
import fr.eletutour.ludotheque.dao.bean.TypeJeu;

import java.io.IOException;
import java.time.Duration;

public class GameForm extends FormLayout {

    Binder<JeuSociete> binder = new BeanValidationBinder<>(JeuSociete.class);

    TextField nom = new TextField("Nom du jeu");
    MultiSelectComboBox<TypeJeu> typeJeu = new MultiSelectComboBox<>("Type de jeu");
    NumberField minJoueur = new NumberField("Nombre de joueur minimum");
    NumberField maxJoueur = new NumberField("Nombre de joueur maximum");
    NumberField ageMinimum = new NumberField("Âge minimum requis");
    NumberField tempsDeJeu = new NumberField("Temps de jeu (minutes)");
    Upload imageUpload = new Upload(new MemoryBuffer());

    Checkbox estExtension = new Checkbox("Est une extension");
    Button ajouterExtensionButton = new Button("Ajouter une extension");
    VerticalLayout extensionsLayout = new VerticalLayout();

    Button save = new Button("Sauvegarder");
    Button delete = new Button("Supprimer");
    Button close = new Button("Annuler");

    private JeuSociete jeuSociete;

    public GameForm() {
        // Configure ComboBox
        typeJeu.setItems(TypeJeu.values());
        typeJeu.setItemLabelGenerator(TypeJeu::name);

        // Configure Image Upload
        MemoryBuffer buffer = new MemoryBuffer();
        imageUpload.setReceiver(buffer);
        imageUpload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        imageUpload.addSucceededListener(event -> {
            try {
                jeuSociete.setImage(buffer.getInputStream().readAllBytes());
            } catch (IOException e) {
                Notification.show("Erreur lors du téléchargement de l'image: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
                e.printStackTrace();
            }
        });

        // Binder configuration with Converters for NumberFields
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
        binder.forField(typeJeu)
                        .bind(JeuSociete::getTypeDeJeu, JeuSociete::setTypeDeJeu);

        binder.bindInstanceFields(this);

        if(jeuSociete != null && jeuSociete.isEstExtension()){
            estExtension.setEnabled(false);
        }
        estExtension.addValueChangeListener(event -> {
            boolean isExtension = event.getValue();
            ajouterExtensionButton.setVisible(!isExtension);
        });

        ajouterExtensionButton.addClickListener(event -> ouvrirPopupAjoutExtension());

        add(
                nom,
                typeJeu,
                minJoueur,
                maxJoueur,
                ageMinimum,
                tempsDeJeu,
                imageUpload,
                estExtension,
                ajouterExtensionButton,
                extensionsLayout,
                createButtonsLayout()
        );
    }

    private void ouvrirPopupAjoutExtension() {
        JeuSociete nouvelleExtension = new JeuSociete();
        nouvelleExtension.setEstExtension(true);
        nouvelleExtension.setTypeDeJeu(jeuSociete.getTypeDeJeu());
        nouvelleExtension.setJeuPrincipal(jeuSociete);

        ExtensionForm extensionForm = new ExtensionForm(jeuSociete, nouvelleExtension);

        Dialog dialog = new Dialog(extensionForm);
        extensionForm.addListener(ExtensionForm.SaveEvent.class, event -> {
            // Ajouter l'extension au jeu principal
            jeuSociete.getExtensions().add(event.getExtension());

            // Mettre à jour la liste des extensions dans le GameForm
            updateExtensionsLayout(jeuSociete);

            dialog.close();
            Notification.show("Extension ajoutée avec succès !");
        });

        extensionForm.addListener(ExtensionForm.CloseEvent.class, event -> dialog.close());

        dialog.open();
    }

    public void setJeuSociete(JeuSociete jeuSociete) {
        this.jeuSociete = jeuSociete;
        if (jeuSociete != null && jeuSociete.getImage() != null) {
            // Assurez-vous que l'image existante est affichée correctement si nécessaire
            imageUpload.getElement().setPropertyJson("files", Json.createArray());
        }
        binder.readBean(jeuSociete);
        updateExtensionsLayout(jeuSociete);
    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, jeuSociete)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));
        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(jeuSociete);
            fireEvent(new SaveEvent(this, jeuSociete));
        } catch (ValidationException e) {
            Notification.show("Erreur de validation des données");
        }
    }

    private void updateExtensionsLayout(JeuSociete jeuSociete) {
        extensionsLayout.removeAll(); // Nettoyer l'affichage précédent

        if (jeuSociete != null && jeuSociete.getExtensions() != null && !jeuSociete.getExtensions().isEmpty()) {
            jeuSociete.getExtensions().forEach(extension -> {
                Div extensionItem = createExtensionItem(extension);
                extensionsLayout.add(extensionItem);
            });
            extensionsLayout.setVisible(true);
        } else {
            extensionsLayout.setVisible(false);
        }
    }

    private Div createExtensionItem(JeuSociete extension) {
        Div itemLayout = new Div();
        itemLayout.addClassName("extension-item");

        // Nom de l'extension
        Div name = new Div();
        name.setText(extension.getNom());

        // Bouton supprimer
        Button deleteButton = new Button("Supprimer");
        deleteButton.addClickListener(event -> {
            jeuSociete.getExtensions().remove(extension);
            updateExtensionsLayout(jeuSociete);
        });

        itemLayout.add(name, deleteButton);
        return itemLayout;
    }

    // Event classes
    public static abstract class GameFormEvent extends ComponentEvent<GameForm> {
        private JeuSociete jeuSociete;

        public GameFormEvent(GameForm source, JeuSociete jeuSociete) {
            super(source, false);
            this.jeuSociete = jeuSociete;
        }

        public JeuSociete getJeuSociete() {
            return jeuSociete;
        }
    }

    public static class SaveEvent extends GameFormEvent {
        SaveEvent(GameForm source, JeuSociete jeuSociete) {
            super(source, jeuSociete);
        }
    }

    public static class DeleteEvent extends GameFormEvent {
        DeleteEvent(GameForm source, JeuSociete jeuSociete) {
            super(source, jeuSociete);
        }
    }

    public static class CloseEvent extends GameFormEvent {
        CloseEvent(GameForm source) {
            super(source, null);
            source.imageUpload.getElement().setPropertyJson("files", Json.createArray());
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
