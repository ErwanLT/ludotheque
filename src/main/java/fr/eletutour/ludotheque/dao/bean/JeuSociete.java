package fr.eletutour.ludotheque.dao.bean;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Lob;
import java.time.Duration;

@Entity
public class JeuSociete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private int nombreJoueursMin;
    private int nombreJoueursMax;
    private int ageMinimum;
    @Enumerated(EnumType.STRING)
    private TypeJeu typeDeJeu;
    private long tempsDeJeuEnMinutes;
    @Lob
    private byte[] image; // Champ pour stocker l'image en BLOB


    public JeuSociete() {}

    public JeuSociete(String nom, int nombreJoueursMin, int nombreJoueursMax, int ageMinimum, TypeJeu typeDeJeu, Duration tempsDeJeu, byte[] image) {
        this.nom = nom;
        this.nombreJoueursMin = nombreJoueursMin;
        this.nombreJoueursMax = nombreJoueursMax;
        this.ageMinimum = ageMinimum;
        this.typeDeJeu = typeDeJeu;
        this.tempsDeJeuEnMinutes = tempsDeJeu.toMinutes();
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

    public int getNombreJoueursMin() {
        return nombreJoueursMin;
    }

    public void setNombreJoueursMin(int nombreJoueursMin) {
        this.nombreJoueursMin = nombreJoueursMin;
    }

    public int getNombreJoueursMax() {
        return nombreJoueursMax;
    }

    public void setNombreJoueursMax(int nombreJoueursMax) {
        this.nombreJoueursMax = nombreJoueursMax;
    }

    public String getNombreJoueurs(){
        return nombreJoueursMin + " - " + nombreJoueursMax;
    }

    public int getAgeMinimum() {
        return ageMinimum;
    }

    public void setAgeMinimum(int ageMinimum) {
        this.ageMinimum = ageMinimum;
    }

    public TypeJeu getTypeDeJeu() {
        return typeDeJeu;
    }

    public void setTypeDeJeu(TypeJeu typeDeJeu) {
        this.typeDeJeu = typeDeJeu;
    }

    public Duration getTempsDeJeu() {
        return Duration.ofMinutes(tempsDeJeuEnMinutes);
    }

    public void setTempsDeJeu(Duration tempsDeJeu) {
        this.tempsDeJeuEnMinutes = tempsDeJeu.toMinutes();
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public String toString() {
        return "Jeu de société: " + nom + "\n" +
                "Type: " + typeDeJeu + "\n" +
                "Nombre de joueurs: " + nombreJoueursMin + " - " + nombreJoueursMax + "\n" +
                "Âge minimum: " + ageMinimum + " ans\n" +
                "Temps de jeu: " + tempsDeJeuEnMinutes + " minutes";
    }
}
