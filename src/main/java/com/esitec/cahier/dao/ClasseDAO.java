package com.esitec.cahier.dao;

import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.model.Classe;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/*
by Aliou barry
Cette classe ClasseDAO est responsable de la gestion des opérations CRUD (Create, Read, Update,
Delete) sur les classes de l'application. Elle utilise JDBC pour interagir avec la base de données MySQL
Elle fournit des méthodes pour lister toutes les classes, trouver une classe par ID, ajouter, modifier et supprimer une classe.
update : 2024-06-15
*/

public class ClasseDAO {

    // =============================================
    // LISTER TOUTES LES CLASSES
    // =============================================
    public List<Classe> findAll() throws DatabaseException {
        List<Classe> liste = new ArrayList<>();
        String sql = "SELECT * FROM classes";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(construireClasse(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur récupération classes : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // TROUVER UNE CLASSE PAR ID
    // =============================================
    public Classe findById(int id) throws DatabaseException {
        String sql = "SELECT * FROM classes WHERE id = ?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return construireClasse(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur recherche classe : " + e.getMessage());
        }
        return null;
    }

    // =============================================
    // AJOUTER UNE CLASSE
    // =============================================
    public void save(Classe c) throws DatabaseException {
        String sql = "INSERT INTO classes (nom, filiere, niveau) VALUES (?, ?, ?)";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNom());
            ps.setString(2, c.getFiliere());
            ps.setString(3, c.getNiveau());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur ajout classe : " + e.getMessage());
        }
    }

    // =============================================
    // MODIFIER UNE CLASSE
    // =============================================
    public void update(Classe c) throws DatabaseException {
        String sql = "UPDATE classes SET nom=?, filiere=?, niveau=? WHERE id=?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getNom());
            ps.setString(2, c.getFiliere());
            ps.setString(3, c.getNiveau());
            ps.setInt(4, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur modification classe : " + e.getMessage());
        }
    }

    // =============================================
    // SUPPRIMER UNE CLASSE
    // =============================================
    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM classes WHERE id = ?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur suppression classe : " + e.getMessage());
        }
    }

    // =============================================
    // MÉTHODE PRIVÉE — Transformer ResultSet en Classe
    // =============================================
    private Classe construireClasse(ResultSet rs) throws SQLException {
        return new Classe(
            rs.getInt("id"),
            rs.getString("nom"),
            rs.getString("filiere"),
            rs.getString("niveau")
        );
    }
}