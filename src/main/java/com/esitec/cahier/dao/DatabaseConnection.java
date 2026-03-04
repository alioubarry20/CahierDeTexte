package com.esitec.cahier.dao;

import com.esitec.cahier.config.AppConfig;
import com.esitec.cahier.exception.DatabaseException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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