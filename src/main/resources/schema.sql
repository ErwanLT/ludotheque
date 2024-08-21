CREATE TABLE jeu_societe (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    nombre_joueurs_min INT,
    nombre_joueurs_max INT,
    age_minimum INT,
    type_de_jeu VARCHAR(255),
    temps_de_jeu_en_minutes BIGINT,
    image BLOB
);
