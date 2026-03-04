-- Création de la base de données
CREATE DATABASE IF NOT EXISTS cahier_de_texte;
USE cahier_de_texte;

-- Table utilisateurs
CREATE TABLE IF NOT EXISTS utilisateurs (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    email VARCHAR(150) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    role ENUM('CHEF_DEPARTEMENT', 'ENSEIGNANT', 'RESPONSABLE_CLASSE') NOT NULL,
    statut ENUM('ACTIF', 'EN_ATTENTE', 'DESACTIVE') DEFAULT 'EN_ATTENTE',
    departement VARCHAR(100),
    specialite VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table classes
CREATE TABLE IF NOT EXISTS classes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    filiere VARCHAR(100),
    niveau VARCHAR(50)
);

-- Table cours
CREATE TABLE IF NOT EXISTS cours (
    id INT AUTO_INCREMENT PRIMARY KEY,
    intitule VARCHAR(200) NOT NULL,
    volume_horaire INT NOT NULL,
    enseignant_id INT,
    classe_id INT,
    FOREIGN KEY (enseignant_id) REFERENCES utilisateurs(id),
    FOREIGN KEY (classe_id) REFERENCES classes(id)
);

-- Table seances
CREATE TABLE IF NOT EXISTS seances (
    id INT AUTO_INCREMENT PRIMARY KEY,
    date_seance DATE NOT NULL,
    heure TIME NOT NULL,
    duree INT NOT NULL,
    contenu TEXT NOT NULL,
    observations TEXT,
    statut ENUM('EN_ATTENTE', 'VALIDEE', 'REJETEE') DEFAULT 'EN_ATTENTE',
    commentaire_rejet TEXT,
    cours_id INT,
    FOREIGN KEY (cours_id) REFERENCES cours(id)
);