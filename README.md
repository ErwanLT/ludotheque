# Ludothèque - Gestion des Jeux de Société
Ce projet est une application de gestion de jeux de société permettant de répertorier, visualiser et gérer des jeux, y compris leurs extensions. L'application est construite avec Spring Boot et Vaadin pour une interface utilisateur fluide.

## Fonctionnalités
- **Gestion des utilisateurs** : Les utilisateurs peuvent se connecter à l'application avec des rôles spécifiques (ROLE_USER, ROLE_ADMIN). Chaque utilisateur voit et gère uniquement sa propre liste de jeux.
- **Liste des jeux de société** : Visualisez tous les jeux de société associés à l'utilisateur connecté, avec des filtres pour une recherche facile.
- **Ajout, modification et suppression de jeux** : Gérez les jeux de société directement depuis l'interface.
- **Gestion des types de jeux** : Chaque jeu peut être catégorisé selon plusieurs types.
- **Gestion des extensions** : Les jeux peuvent avoir des extensions, qui sont des ajouts spécifiques à un jeu principal.
- **Affichage en arborescence** : Les jeux et leurs extensions sont affichés sous forme d'une hiérarchie pour une meilleure visualisation.
- **Téléchargement d'images** : Associez des images aux jeux pour les rendre plus reconnaissables dans l'interface.
- **Création de compte** : Depuis la page de login, vous avez la possibilité de créer un compte USER.
- **Gestion des utilisateurs par l'administrateur** : Un utilisateur avec le rôle ADMIN peut gérer les utilisateurs (ajouter, modifier ou supprimer) via une interface dédiée.
- **Sécurité** : L'application intègre une gestion de la sécurité avec authentification et autorisation basée sur les rôles des utilisateurs.

## Prérequis
Avant de pouvoir exécuter ce projet, assurez-vous d'avoir les éléments suivants installés :

* Java 17 ou plus récent
* Maven 3.6 ou plus récent
* Un serveur de base de données compatible (H2, MySQL, PostgreSQL, etc.)

## Installation
1. Clonez ce dépôt sur votre machine locale :
```bash
git clone https://github.com/votre-utilisateur/ludotheque.git
cd ludotheque
```
2. Configurez la base de données dans le fichier application.properties situé dans le répertoire src/main/resources/. Voici un exemple de configuration pour H2 :
```properties
spring.datasource.url=jdbc:h2:file:~/data/ludotheque;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.username=sa
spring.datasource.password=
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
### Authentification
Lors de la connexion, un utilisateur avec le rôle ADMIN a accès à des fonctionnalités supplémentaires, telles que la gestion des utilisateurs. Les utilisateurs avec le rôle USER ne peuvent voir et gérer que leurs propres jeux.

### Liste des Jeux
La page principale affiche une liste de tous les jeux de société associés à l'utilisateur connecté. Vous pouvez filtrer cette liste en utilisant le champ de recherche en haut de la page.

### Ajouter/Modifier un Jeu
Pour ajouter un nouveau jeu, cliquez sur le bouton "**Ajouter jeu**". Pour modifier un jeu existant, cliquez sur une ligne de la liste. Un formulaire s'affiche alors, vous permettant de remplir ou de modifier les informations suivantes :

* Nom du jeu
* Note du jeu
* Types de jeu (vous pouvez en sélectionner plusieurs)
* Nombre minimum et maximum de joueurs
* Âge minimum requis
* Temps de jeu moyen
* Image du jeu

### Gestion des Extensions
Si un jeu n'est pas une extension, un bouton "**Ajouter une extension**" apparaît dans le formulaire. En cliquant dessus, vous pouvez ajouter une nouvelle extension associée au jeu sélectionné. Les extensions existantes sont affichées sous forme de liste avec la possibilité de les supprimer.

### Suppression d'un Jeu
Vous pouvez supprimer un jeu ou une extension en cliquant sur le bouton "**Supprimer**" dans le formulaire d'édition.

### Gestion des Utilisateurs (Admin)
L'utilisateur avec le rôle ADMIN peut accéder à la gestion des utilisateurs via un menu dédié. Il peut ajouter de nouveaux utilisateurs, modifier leurs informations (nom d'utilisateur, mot de passe, rôle) ou les supprimer. Cependant, un contrôle est en place pour empêcher la suppression du dernier administrateur.

### Contribution
Les contributions sont les bienvenues ! Pour commencer :

* Fork le projet.
* Crée une branche pour votre fonctionnalité (git checkout -b feature/AmazingFeature).
* Commitez vos modifications (git commit -m 'Add some AmazingFeature').
* Poussez sur la branche (git push origin feature/AmazingFeature).
* Ouvrez une Pull Request.

## Licence
Ce projet est sous licence Apache-2.0 license. Voir le fichier [LICENSE](LICENSE) pour plus de détails.
