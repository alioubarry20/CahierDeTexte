package com.esitec.cahier.dao;

import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Seance;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/*
by Aliou barry
Cette classe SeanceDAO est responsable de la gestion des opérations CRUD (Create, Read, Update, Delete)
sur les séances de l'application. Elle utilise JDBC pour interagir avec la base de données MySQL.
Elle fournit des méthodes pour trouver les séances d'un cours, d'une classe en attente de validation
lister toutes les séances, ajouter, modifier, valider et rejeter une séance.
update : 2024-06-16
*/
public class SeanceDAO {

    // =============================================
    // TROUVER LES SEANCES D'UN COURS
    // =============================================
    public List<Seance> findByCours(int coursId) throws DatabaseException {
        List<Seance> liste = new ArrayList<>();
        String sql = "SELECT * FROM seances WHERE cours_id = ? ORDER BY date_seance DESC";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, coursId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(construireSeance(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur récupération séances : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // TROUVER LES SEANCES EN ATTENTE D'UNE CLASSE
    // =============================================
    public List<Seance> findEnAttenteByClasse(int classeId) throws DatabaseException {
        List<Seance> liste = new ArrayList<>();
        String sql = "SELECT s.* FROM seances s " +
                     "JOIN cours c ON s.cours_id = c.id " +
                     "WHERE c.classe_id = ? AND s.statut = 'EN_ATTENTE'";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, classeId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(construireSeance(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur récupération séances : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // AJOUTER UNE SEANCE
    // =============================================
    public void save(Seance s) throws DatabaseException {
        String sql = "INSERT INTO seances (date_seance, heure, duree, contenu, " +
                     "observations, statut, cours_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(s.getDate()));
            ps.setTime(2, Time.valueOf(s.getHeure()));
            ps.setInt(3, s.getDuree());
            ps.setString(4, s.getContenu());
            ps.setString(5, s.getObservations());
            ps.setString(6, s.getStatut());
            ps.setInt(7, s.getCours().getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur ajout séance : " + e.getMessage());
        }
    }

    // =============================================
    // MODIFIER UNE SEANCE
    // =============================================
    public void update(Seance s) throws DatabaseException {
        String sql = "UPDATE seances SET date_seance=?, heure=?, duree=?, " +
                     "contenu=?, observations=? WHERE id=? AND statut='EN_ATTENTE'";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, Date.valueOf(s.getDate()));
            ps.setTime(2, Time.valueOf(s.getHeure()));
            ps.setInt(3, s.getDuree());
            ps.setString(4, s.getContenu());
            ps.setString(5, s.getObservations());
            ps.setInt(6, s.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur modification séance : " + e.getMessage());
        }
    }

    // =============================================
    // VALIDER UNE SEANCE
    // =============================================
    public void valider(int id) throws DatabaseException {
        String sql = "UPDATE seances SET statut='VALIDEE' WHERE id=?";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur validation séance : " + e.getMessage());
        }
    }

    // =============================================
    // REJETER UNE SEANCE
    // =============================================
    public void rejeter(int id, String commentaire) throws DatabaseException {
        String sql = "UPDATE seances SET statut='REJETEE', commentaire_rejet=? WHERE id=?";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, commentaire);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur rejet séance : " + e.getMessage());
        }
    }

    // =============================================
    // MÉTHODE PRIVÉE — Transformer ResultSet en Seance
    // =============================================
    private Seance construireSeance(ResultSet rs) throws SQLException {
        Seance s = new Seance();
        s.setId(rs.getInt("id"));
        s.setDate(rs.getDate("date_seance").toLocalDate());
        s.setHeure(rs.getTime("heure").toLocalTime());
        s.setDuree(rs.getInt("duree"));
        s.setContenu(rs.getString("contenu"));
        s.setObservations(rs.getString("observations"));
        s.setStatut(rs.getString("statut"));
        s.setCommentaireRejet(rs.getString("commentaire_rejet"));

        // On crée un cours minimal avec juste l'id
        Cours cours = new Cours();
        cours.setId(rs.getInt("cours_id"));
        s.setCours(cours);

        return s;
    }
}