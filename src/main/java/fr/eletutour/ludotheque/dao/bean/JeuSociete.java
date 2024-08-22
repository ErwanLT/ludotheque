package fr.eletutour.ludotheque.dao.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;

@Entity
public class JeuSociete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom du jeu est obligatoire")
    private String nom;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Le type de jeu est obligatoire")
    private TypeJeu typeDeJeu;

    @Min(value = 1, message = "Le nombre minimum de joueurs doit être au moins de 1")
    private Integer nombreJoueursMin;

    @Min(value = 1, message = "Le nombre maximum de joueurs doit être au moins de 1")
    private Integer nombreJoueursMax;

    @Min(value = 0, message = "L'âge minimum doit être supérieur ou égal à 0")
    private Integer ageMinimum;

    @NotNull(message = "Le temps de jeu est obligatoire")
    private Duration tempsDeJeuEnMinutes;

    @Lob
    private byte[] image;

    // Constructeurs, getters et setters

    public JeuSociete() {
        this.tempsDeJeuEnMinutes = Duration.ofMinutes(0);
    }

    public JeuSociete(String nom, TypeJeu typeDeJeu, Integer nombreJoueursMin, Integer nombreJoueursMax, Integer ageMinimum, Duration tempsDeJeuEnMinutes, byte[] image) {
        this.nom = nom;
        this.typeDeJeu = typeDeJeu;
        this.nombreJoueursMin = nombreJoueursMin;
        this.nombreJoueursMax = nombreJoueursMax;
        this.ageMinimum = ageMinimum;
        this.tempsDeJeuEnMinutes = tempsDeJeuEnMinutes;
        this.image = image;
    }

    // Getters et setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public TypeJeu getTypeDeJeu() {
        return typeDeJeu;
    }

    public void setTypeDeJeu(TypeJeu typeDeJeu) {
        this.typeDeJeu = typeDeJeu;
    }

    public Integer getNombreJoueursMin() {
        return nombreJoueursMin;
    }

    public void setNombreJoueursMin(Integer nombreJoueursMin) {
        this.nombreJoueursMin = nombreJoueursMin;
    }

    public Integer getNombreJoueursMax() {
        return nombreJoueursMax;
    }

    public void setNombreJoueursMax(Integer nombreJoueursMax) {
        this.nombreJoueursMax = nombreJoueursMax;
    }

    public Integer getAgeMinimum() {
        return ageMinimum;
    }

    public void setAgeMinimum(Integer ageMinimum) {
        this.ageMinimum = ageMinimum;
    }

    public Duration getTempsDeJeuEnMinutes() {
        return tempsDeJeuEnMinutes;
    }

    public void setTempsDeJeuEnMinutes(Duration tempsDeJeuEnMinutes) {
        this.tempsDeJeuEnMinutes = tempsDeJeuEnMinutes;
    }

    public String getFormattedTempsDeJeu() {
        long hours = tempsDeJeuEnMinutes.toHours();
        long minutes = tempsDeJeuEnMinutes.toMinutes() % 60;
        if (hours > 0) {
            return String.format("%d h %02d min", hours, minutes);
        } else {
            return String.format("%d min", minutes);
        }
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }
}
