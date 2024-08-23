# Gestion de Ludothèque - Application Web

Cette application est conçue pour la gestion d'une ludothèque, permettant de gérer une collection de jeux de société. Elle permet de visualiser, ajouter, modifier et supprimer des jeux, ainsi que de voir la répartition des jeux en fonction de leur type.

## Fonctionnalités

- **Visualisation des jeux** : Affichage des jeux de société disponibles dans la ludothèque avec leurs détails.
- **Ajout de jeux** : Possibilité d'ajouter de nouveaux jeux dans la ludothèque via un formulaire.
- **Modification de jeux** : Modification des informations existantes sur un jeu.
- **Suppression de jeux** : Suppression d'un jeu de la ludothèque.
- **Dashboard** : Visualisation de la répartition des jeux par type sous forme de graphique circulaire.

## Technologies Utilisées

- **Java 17**
- **Spring Boot 3.x**
- **Vaadin 24.x**
- **H2 Database** : Base de données en mémoire pour le développement et les tests.
- **Maven** : Gestionnaire de dépendances et de construction du projet.
- **Chart.js** : Bibliothèque JavaScript pour la création de graphiques.

## Installation et Démarrage

1. **Cloner le projet :**

    ```bash
    git clone https://github.com/votre-repo/ludotheque.git
    cd ludotheque
    ```

2. **Compiler et exécuter l'application :**

   Vous pouvez exécuter l'application directement depuis votre IDE (comme IntelliJ IDEA ou Eclipse) ou utiliser Maven.

    ```bash
    mvn clean install
    mvn spring-boot:run
    ```

3. **Accéder à l'application :**

   L'application sera disponible à l'adresse suivante :
    ```
    http://localhost:8080/
    ```

## Structure du Projet

- **`src/main/java/`** : Contient le code source de l'application.
    - **`fr.eletutour.ludotheque/`** : Package principal contenant les classes du projet.
        - **`LudothequeApplication`** : Classe principale démarrant l'application Spring Boot.
        - **`dao/`**
          - **`bean/`**
            - **`JeuSociete.java`** : Entité représentant un jeu de société.
            - **`TypeJeu.java`** : Enumération définissant les types de jeux.
          - **`repository/JeuSocieteRepository.java`** : Interface pour les opérations CRUD sur les jeux.
        - **`service/GamesService.java`** : Service d'accès à la BDD.
        - **`view/`** : Contient les vues Vaadin pour l'interface utilisateur.
            - **`MainLayout.java`** : Vue principale.
            - **`form/`**
              - **`GameForm.java`** : Formulaire pour ajouter/modifier un jeu.
            - **`GameListView.java`** : Vue affichant la liste des jeux.
            - **`DashboardView.java`** : Vue affichant la répartition des jeux par type.

- **`src/main/resources/`** : Contient les ressources statiques et les fichiers de configuration.
    - **`application.properties`** : Fichier de configuration de Spring Boot.
    - **`schema.sql`** : Script SQL pour l'initialisation de la base de données.

- **`pom.xml`** : Fichier de configuration Maven contenant les dépendances du projet.

## Gestion des Données

- **Base de données :** H2 Database est utilisé pour stocker les données en mémoire. Les données sont initialisées au démarrage à partir du fichier `data.sql`.

## Utilisation de l'application

### Ajout/Modification d'un Jeu

- Remplissez le formulaire avec les informations du jeu.
- **Image** : Vous pouvez téléverser une image pour représenter le jeu.
- Cliquez sur **Sauvegarder** pour enregistrer les modifications.

### Visualisation du Dashboard

- Accédez à la vue Dashboard pour voir la répartition des jeux en fonction de leur type sous forme de graphique circulaire.

## Déploiement

Pour déployer l'application sur un serveur, assurez-vous de configurer une base de données persistante (par exemple MySQL, PostgreSQL) et de mettre à jour le fichier `application.properties` en conséquence.

## Contribuer

Les contributions sont les bienvenues ! Veuillez créer une branche pour votre fonctionnalité ou correction de bug, puis soumettez une pull request.

## License

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.
