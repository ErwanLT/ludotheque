package fr.eletutour.ludotheque.dao.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Entity
public class JeuSociete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @NotBlank(message = "Le nom du jeu est obligatoire")
    private String nom;


    @NotNull(message = "Le type de jeu est obligatoire")
    @Convert(converter = TypeJeuConverter.class)
    private Set<TypeJeu> typeDeJeu;

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

    private boolean estExtension;

    @ManyToOne
    @JoinColumn(name = "jeu_principal_id")
    @JsonIgnore
    private JeuSociete jeuPrincipal;

    @OneToMany(mappedBy = "jeuPrincipal", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<JeuSociete> extensions = new HashSet<>();

    @Column(nullable = true)
    private Double rating;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private AppUser owner;

    // Constructeurs, getters et setters

    public JeuSociete() {
        this.tempsDeJeuEnMinutes = Duration.ofMinutes(0);
        this.setEstExtension(false);
    }

    public JeuSociete(String nom, Set<TypeJeu> typeDeJeu, Integer nombreJoueursMin, Integer nombreJoueursMax, Integer ageMinimum, Duration tempsDeJeuEnMinutes, byte[] image) {
        this.nom = nom;
        this.typeDeJeu = typeDeJeu;
        this.nombreJoueursMin = nombreJoueursMin;
        this.nombreJoueursMax = nombreJoueursMax;
        this.ageMinimum = ageMinimum;
        this.tempsDeJeuEnMinutes = tempsDeJeuEnMinutes;
        this.image = image;
    }

    public JeuSociete(String nom, Set<TypeJeu> typeDeJeu, Integer nombreJoueursMin, Integer nombreJoueursMax, Integer ageMinimum, Duration tempsDeJeuEnMinutes, byte[] image, boolean estExtension, Set<JeuSociete> extensions) {
        this.nom = nom;
        this.typeDeJeu = typeDeJeu;
        this.nombreJoueursMin = nombreJoueursMin;
        this.nombreJoueursMax = nombreJoueursMax;
        this.ageMinimum = ageMinimum;
        this.tempsDeJeuEnMinutes = tempsDeJeuEnMinutes;
        this.image = image;
        this.estExtension = estExtension;
        this.extensions = extensions;
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

    public Set<TypeJeu> getTypeDeJeu() {
        return typeDeJeu;
    }

    public void setTypeDeJeu(Set<TypeJeu> typeDeJeu) {
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

    @JsonIgnore
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

    public Set<JeuSociete> getExtensions() {
        return extensions;
    }

    public void setExtensions(Set<JeuSociete> extensions) {
        this.extensions = extensions;
    }

    public JeuSociete getJeuPrincipal() {
        return jeuPrincipal;
    }

    public void setJeuPrincipal(JeuSociete jeuPrincipal) {
        this.jeuPrincipal = jeuPrincipal;
    }

    public boolean isEstExtension() {
        return estExtension;
    }

    public void setEstExtension(boolean estExtension) {
        this.estExtension = estExtension;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }
}
