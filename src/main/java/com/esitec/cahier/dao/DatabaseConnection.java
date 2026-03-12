package com.esitec.cahier.dao;

import com.esitec.cahier.config.AppConfig;
import com.esitec.cahier.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
by Aliou barry
Cette classe DatabaseConnection est responsable de la gestion de la connexion à la base de données MySQL.
Elle utilise le pattern Singleton pour garantir qu'une seule connexion est établie et partagée à
travers l'application. Elle fournit une méthode pour obtenir la connexion et une méthode pour la fermer.
update : 2024-06-17
*/
public class DatabaseConnection {

    // Instance unique (pattern Singleton)
    private static Connection instance = null;

    // Constructeur privé — personne ne peut faire new DatabaseConnection()
    private DatabaseConnection() {}

    // Méthode pour obtenir la connexion
    public static Connection getInstance() throws DatabaseException {
        try {
            if (instance == null || instance.isClosed()) {
                instance = DriverManager.getConnection(
                    AppConfig.DB_URL,
                    AppConfig.DB_USER,
                    AppConfig.DB_PASSWORD
                );
                System.out.println("✅ Connexion BDD établie.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("❌ Impossible de se connecter : " + e.getMessage());
        }
        return instance;
    }

    // Fermer la connexion
    public static void closeConnection() {
        try {
            if (instance != null && !instance.isClosed()) {
                instance.close();
                System.out.println("✅ Connexion BDD fermée.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}