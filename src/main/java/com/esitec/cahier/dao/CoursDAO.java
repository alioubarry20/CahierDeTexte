package com.esitec.cahier.dao;

import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.model.Classe;
import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Enseignant;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/*
by Aliou barry
Cette classe CoursDAO est responsable de la gestion des opérations CRUD (Create, Read, Update, Delete)
sur les cours de l'application. Elle utilise JDBC pour interagir avec la base de données MySQL.
Elle fournit des méthodes pour trouver les cours d'un enseignant, d'une classe, lister
tous les cours, ajouter, modifier et supprimer un cours.
update : 2024-06-15

*/
public class CoursDAO {

    // =============================================
    // TROUVER LES COURS D'UN ENSEIGNANT
    // =============================================
    public List<Cours> findByEnseignant(int enseignantId) throws DatabaseException {
        List<Cours> liste = new ArrayList<>();
        String sql = "SELECT c.*, u.nom, u.prenom, u.specialite, " +
                     "cl.nom as classe_nom, cl.filiere, cl.niveau " +
                     "FROM cours c " +
                     "JOIN utilisateurs u ON c.enseignant_id = u.id " +
                     "JOIN classes cl ON c.classe_id = cl.id " +
                     "WHERE c.enseignant_id = ?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, enseignantId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(construireCours(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur récupération cours : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // TROUVER LES COURS D'UNE CLASSE
    // =============================================
    public List<Cours> findByClasse(int classeId) throws DatabaseException {
        List<Cours> liste = new ArrayList<>();
        String sql = "SELECT c.*, u.nom, u.prenom, u.specialite, " +
                     "cl.nom as classe_nom, cl.filiere, cl.niveau " +
                     "FROM cours c " +
                     "JOIN utilisateurs u ON c.enseignant_id = u.id " +
                     "JOIN classes cl ON c.classe_id = cl.id " +
                     "WHERE c.classe_id = ?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, classeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(construireCours(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur récupération cours : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // LISTER TOUS LES COURS
    // =============================================
    public List<Cours> findAll() throws DatabaseException {
        List<Cours> liste = new ArrayList<>();
        String sql = "SELECT c.*, u.nom, u.prenom, u.specialite, " +
                     "cl.nom as classe_nom, cl.filiere, cl.niveau " +
                     "FROM cours c " +
                     "JOIN utilisateurs u ON c.enseignant_id = u.id " +
                     "JOIN classes cl ON c.classe_id = cl.id";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(construireCours(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur récupération cours : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // AJOUTER UN COURS
    // =============================================
    public void save(Cours c) throws DatabaseException {
        String sql = "INSERT INTO cours (intitule, volume_horaire, enseignant_id, classe_id) " +
                     "VALUES (?, ?, ?, ?)";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getIntitule());
            ps.setInt(2, c.getVolumeHoraire());
            ps.setInt(3, c.getEnseignant().getId());
            ps.setInt(4, c.getClasse().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur ajout cours : " + e.getMessage());
        }
    }

    // =============================================
    // MODIFIER UN COURS
    // =============================================
    public void update(Cours c) throws DatabaseException {
        String sql = "UPDATE cours SET intitule=?, volume_horaire=?, " +
                     "enseignant_id=?, classe_id=? WHERE id=?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, c.getIntitule());
            ps.setInt(2, c.getVolumeHoraire());
            ps.setInt(3, c.getEnseignant().getId());
            ps.setInt(4, c.getClasse().getId());
            ps.setInt(5, c.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur modification cours : " + e.getMessage());
        }
    }

    // =============================================
    // SUPPRIMER UN COURS
    // =============================================
    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM cours WHERE id = ?";
        try {
            Connection con = DatabaseConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur suppression cours : " + e.getMessage());
        }
    }

    // =============================================
    // MÉTHODE PRIVÉE — Transformer ResultSet en Cours
    // =============================================
    private Cours construireCours(ResultSet rs) throws SQLException {
        // Construire l'enseignant
        Enseignant enseignant = new Enseignant();
        enseignant.setId(rs.getInt("enseignant_id"));
        enseignant.setNom(rs.getString("nom"));
        enseignant.setPrenom(rs.getString("prenom"));
        enseignant.setSpecialite(rs.getString("specialite"));

        // Construire la classe
        Classe classe = new Classe();
        classe.setId(rs.getInt("classe_id"));
        classe.setNom(rs.getString("classe_nom"));
        classe.setFiliere(rs.getString("filiere"));
        classe.setNiveau(rs.getString("niveau"));

        // Construire le cours
        Cours cours = new Cours();
        cours.setId(rs.getInt("id"));
        cours.setIntitule(rs.getString("intitule"));
        cours.setVolumeHoraire(rs.getInt("volume_horaire"));
        cours.setEnseignant(enseignant);
        cours.setClasse(classe);

        return cours;
    }
}