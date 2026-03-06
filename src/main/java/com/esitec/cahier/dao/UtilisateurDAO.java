package com.esitec.cahier.dao;

import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurDAO {

    public boolean ajouter(Utilisateur utilisateur) throws DatabaseException {
        String sql = "INSERT INTO utilisateurs " +
                     "(nom, prenom, email, mot_de_passe, role, statut) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt =
                DatabaseConnection.getInstance().prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getPrenom());
            stmt.setString(3, utilisateur.getEmail());
            stmt.setString(4, utilisateur.getMotDePasse());
            stmt.setString(5, utilisateur.getRole());
            stmt.setString(6, utilisateur.getStatut());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Erreur ajout : " + e.getMessage());
        }
    }

    public Utilisateur trouverParEmailEtMotDePasse(String email, String motDePasse)
            throws DatabaseException {

        String sql = "SELECT * FROM utilisateurs " +
                     "WHERE email = ? AND mot_de_passe = ? AND statut = 'ACTIF'";

        try (PreparedStatement stmt =
                DatabaseConnection.getInstance().prepareStatement(sql)) {

            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return construireUtilisateur(rs);

        } catch (SQLException e) {
            throw new DatabaseException("Erreur auth : " + e.getMessage());
        }

        return null;
    }

    public Utilisateur trouverParId(int id) throws DatabaseException {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";

        try (PreparedStatement stmt =
                DatabaseConnection.getInstance().prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) return construireUtilisateur(rs);

        } catch (SQLException e) {
            throw new DatabaseException("Erreur recherche : " + e.getMessage());
        }

        return null;
    }

    public List<Utilisateur> listerParRole(String role) throws DatabaseException {
        String sql = "SELECT * FROM utilisateurs WHERE role = ? ORDER BY nom";
        List<Utilisateur> liste = new ArrayList<>();

        try (PreparedStatement stmt =
                DatabaseConnection.getInstance().prepareStatement(sql)) {

            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) liste.add(construireUtilisateur(rs));

        } catch (SQLException e) {
            throw new DatabaseException("Erreur liste : " + e.getMessage());
        }

        return liste;
    }

    public List<Utilisateur> listerEnAttente() throws DatabaseException {
        String sql = "SELECT * FROM utilisateurs " +
                     "WHERE statut = 'EN_ATTENTE' ORDER BY nom";
        List<Utilisateur> liste = new ArrayList<>();

        try (PreparedStatement stmt =
                DatabaseConnection.getInstance().prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) liste.add(construireUtilisateur(rs));

        } catch (SQLException e) {
            throw new DatabaseException("Erreur liste attente : " + e.getMessage());
        }

        return liste;
    }

    public boolean mettreAJourStatut(int id, String statut)
            throws DatabaseException {

        String sql = "UPDATE utilisateurs SET statut = ? WHERE id = ?";

        try (PreparedStatement stmt =
                DatabaseConnection.getInstance().prepareStatement(sql)) {

            stmt.setString(1, statut);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Erreur statut : " + e.getMessage());
        }
    }

    private Utilisateur construireUtilisateur(ResultSet rs) throws SQLException {
    int    id     = rs.getInt("id");
    String nom    = rs.getString("nom");
    String prenom = rs.getString("prenom");
    String email  = rs.getString("email");
    String mdp    = rs.getString("mot_de_passe");
    String role   = rs.getString("role");
    String statut = rs.getString("statut");

    Utilisateur u;

    switch (role) {
        case "CHEF_DEPARTEMENT":
            // Son constructeur : (id, nom, prenom, email, departement)
            // On met statut dans departement car pas d'autre choix
            ChefDepartement chef = new ChefDepartement();
            chef.setId(id);
            chef.setNom(nom);
            chef.setPrenom(prenom);
            chef.setEmail(email);
            chef.setMotDePasse(mdp);
            chef.setRole(role);
            chef.setStatut(statut);
            u = chef;
            break;

        case "ENSEIGNANT":
            // Son constructeur : (id, nom, prenom, email, specialite)
            Enseignant enseignant = new Enseignant();
            enseignant.setId(id);
            enseignant.setNom(nom);
            enseignant.setPrenom(prenom);
            enseignant.setEmail(email);
            enseignant.setMotDePasse(mdp);
            enseignant.setRole(role);
            enseignant.setStatut(statut);
            u = enseignant;
            break;

        case "RESPONSABLE_CLASSE":
            // Son constructeur : (id, nom, prenom, email, classe)
            ResponsableClasse responsable = new ResponsableClasse();
            responsable.setId(id);
            responsable.setNom(nom);
            responsable.setPrenom(prenom);
            responsable.setEmail(email);
            responsable.setMotDePasse(mdp);
            responsable.setRole(role);
            responsable.setStatut(statut);
            u = responsable;
            break;

        default:
            throw new SQLException("Rôle inconnu : " + role);
    }

    return u;
}
}