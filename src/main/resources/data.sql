USE cahier_de_texte;

-- Compte admin par défaut
INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, statut, departement)
VALUES ('Admin', 'Super', 'admin@esitec.sn', 'admin123', 'CHEF_DEPARTEMENT', 'ACTIF', 'Informatique');

-- Classe de test
INSERT INTO classes (nom, filiere, niveau)
VALUES ('L3 INFO', 'Informatique', 'Licence 3');