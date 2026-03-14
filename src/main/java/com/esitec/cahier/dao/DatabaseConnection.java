package com.esitec.cahier.dao;

import com.esitec.cahier.config.AppConfig;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DatabaseConnection {

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Connection tempConn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC",
                AppConfig.DB_USER, AppConfig.DB_PASSWORD
            );
            Statement st = tempConn.createStatement();
            st.executeUpdate("CREATE DATABASE IF NOT EXISTS cahier_de_texte " +
                "CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci");
            st.close();
            tempConn.close();

            connection = DriverManager.getConnection(
                AppConfig.DB_URL + "?useSSL=false&serverTimezone=UTC",
                AppConfig.DB_USER, AppConfig.DB_PASSWORD
            );

            initialiserTables();

        } catch (Exception e) {
            throw new RuntimeException("Erreur connexion BDD : " + e.getMessage());
        }
    }

    private void initialiserTables() throws Exception {
        Statement st = connection.createStatement();

        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS utilisateurs (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "nom VARCHAR(100) NOT NULL," +
            "prenom VARCHAR(100) NOT NULL," +
            "email VARCHAR(150) UNIQUE NOT NULL," +
            "mot_de_passe VARCHAR(255) NOT NULL," +
            "role VARCHAR(50) NOT NULL," +
            "statut VARCHAR(20) DEFAULT 'EN_ATTENTE'," +
            "specialite VARCHAR(100)," +
            "departement VARCHAR(100)," +
            "classe_id INT)"
        );

        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS classes (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "nom VARCHAR(100) NOT NULL," +
            "filiere VARCHAR(100)," +
            "niveau VARCHAR(50))"
        );

        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS cours (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "intitule VARCHAR(200) NOT NULL," +
            "volume_horaire INT DEFAULT 0," +
            "enseignant_id INT," +
            "classe_id INT," +
            "FOREIGN KEY (enseignant_id) REFERENCES utilisateurs(id)," +
            "FOREIGN KEY (classe_id) REFERENCES classes(id))"
        );

        st.executeUpdate(
            "CREATE TABLE IF NOT EXISTS seances (" +
            "id INT AUTO_INCREMENT PRIMARY KEY," +
            "date DATE," +
            "heure TIME," +
            "duree INT," +
            "contenu TEXT," +
            "observations TEXT," +
            "statut VARCHAR(20) DEFAULT 'EN_ATTENTE'," +
            "commentaire_rejet TEXT," +
            "cours_id INT," +
            "FOREIGN KEY (cours_id) REFERENCES cours(id))"
        );

        st.executeUpdate(
            "INSERT IGNORE INTO utilisateurs " +
            "(nom, prenom, email, mot_de_passe, role, statut, departement) " +
            "VALUES ('Admin', 'Super', 'admin@esitec.sn', 'admin123', " +
            "'CHEF_DEPARTEMENT', 'ACTIF', 'Informatique')"
        );

        st.close();
    }

    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    // Méthode statique pour compatibilité avec les DAOs existants
    public static Connection getConnection() {
        return getInstance().connection;
    }
}