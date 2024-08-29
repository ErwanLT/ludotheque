# Ludothèque - Gestion des Jeux de Société
Ce projet est une application de gestion de jeux de société permettant de répertorier, visualiser et gérer des jeux, y compris leurs extensions. L'application est construite avec Spring Boot et Vaadin pour une interface utilisateur fluide.

## Fonctionnalités
* **Liste des jeux de société** : Visualisez tous les jeux de société disponibles dans la base de données, avec des filtres pour une recherche facile.
* **Ajout, modification et suppression de jeux** : Gérez les jeux de société directement depuis l'interface.
* **Gestion des types de jeux** : Chaque jeu peut être catégorisé selon plusieurs types.
* **Gestion des extensions** : Les jeux peuvent avoir des extensions, qui sont des ajouts spécifiques à un jeu principal.
* **Affichage en arborescence** : Les jeux et leurs extensions sont affichés sous forme d'une hiérarchie pour une meilleure visualisation.
* **Téléchargement d'images** : Associez des images aux jeux pour les rendre plus reconnaissables dans l'interface.

## Prérequis
Avant de pouvoir exécuter ce projet, assurez-vous d'avoir les éléments suivants installés :

* Java 17 ou plus récent
* Maven 3.6 ou plus récent
* Un serveur de base de données compatible (MySQL, PostgreSQL, etc.)

## Installation
1. Clonez ce dépôt sur votre machine locale :
```bash
git clone https://github.com/votre-utilisateur/ludotheque.git
cd ludotheque
```
2. Configurez la base de données dans le fichier application.properties situé dans le répertoire src/main/resources/. Voici un exemple de configuration pour MySQL :
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/ludotheque
spring.datasource.username=root
spring.datasource.password=motdepasse
spring.jpa.hibernate.ddl-auto=update
```
3. Compilez le projet et démarrez l'application :
```bash
mvn clean install
mvn spring-boot:run
```
4. Accédez à l'application via votre navigateur à l'adresse suivante :
```arduino
http://localhost:8080
```

## Utilisation
### Liste des Jeux
La page principale affiche une liste de tous les jeux de société. Vous pouvez filtrer cette liste en utilisant le champ de recherche en haut de la page.

### Ajouter/Modifier un Jeu
Pour ajouter un nouveau jeu, cliquez sur le bouton "**Ajouter jeu**". Pour modifier un jeu existant, cliquez sur une ligne de la liste. Un formulaire s'affiche alors, vous permettant de remplir ou de modifier les informations suivantes :

* Nom du jeu
* Types de jeu (vous pouvez en sélectionner plusieurs)
* Nombre minimum et maximum de joueurs
* Âge minimum requis
* Temps de jeu moyen
* Image du jeu

### Gestion des Extensions
Si un jeu n'est pas une extension, un bouton "**Ajouter une extension**" apparaît dans le formulaire. En cliquant dessus, vous pouvez ajouter une nouvelle extension associée au jeu sélectionné. Les extensions existantes sont affichées sous forme de liste avec la possibilité de les supprimer.

### Suppression d'un Jeu
Vous pouvez supprimer un jeu ou une extension en cliquant sur le bouton "**Supprimer**" dans le formulaire d'édition.

## Contribution
Les contributions sont les bienvenues ! Pour commencer :

* Fork le projet.
* Crée une branche pour votre fonctionnalité (git checkout -b feature/AmazingFeature).
* Commitez vos modifications (git commit -m 'Add some AmazingFeature').
* Poussez sur la branche (git push origin feature/AmazingFeature).
* Ouvrez une Pull Request.

## Licence
Ce projet est sous licence Apache-2.0 license. Voir le fichier [LICENSE](LICENSE) pour plus de détails.
