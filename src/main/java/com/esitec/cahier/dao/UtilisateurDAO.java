package com.esitec.cahier.dao;

import com.esitec.cahier.config.AppConfig;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.model.ChefDepartement;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.ResponsableClasse;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/*
commentaire : javadoc pour la classe UtilisateurDAO
Cette classe est responsable de la gestion des opérations CRUD (Create, Read, Update, Delete)
sur les utilisateurs de l'application. Elle utilise JDBC pour interagir avec la base de données MySQL.
Elle fournit des méthodes pour trouver un utilisateur par email et mot de passe, lister tous les utilisateurs,
ajouter, modifier, supprimer et valider un compte utilisateur.
by Aliou barry
update : 2024-06-15
*/
public class UtilisateurDAO {

    // =============================================
    // TROUVER UN UTILISATEUR PAR EMAIL ET MOT DE PASSE
    // =============================================
    public Utilisateur findByEmailAndPassword(String email, String motDePasse) throws DatabaseException {
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, email);
            ps.setString(2, motDePasse);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return construireUtilisateur(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la connexion : " + e.getMessage());
        }
        return null;
    }

    // =============================================
    // LISTER TOUS LES UTILISATEURS
    // =============================================
    public List<Utilisateur> findAll() throws DatabaseException {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                liste.add(construireUtilisateur(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la récupération : " + e.getMessage());
        }
        return liste;
    }

    // =============================================
    // AJOUTER UN UTILISATEUR
    // =============================================
    public void save(Utilisateur u) throws DatabaseException {
        String sql = "INSERT INTO utilisateurs (nom, prenom, email, mot_de_passe, role, statut, departement, specialite) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getMotDePasse());
            ps.setString(5, u.getRole());
            ps.setString(6, u.getStatut());

            // Selon le rôle on remplit departement ou specialite
            if (u instanceof ChefDepartement) {
                ps.setString(7, ((ChefDepartement) u).getDepartement());
                ps.setNull(8, Types.VARCHAR);
            } else if (u instanceof Enseignant) {
                ps.setNull(7, Types.VARCHAR);
                ps.setString(8, ((Enseignant) u).getSpecialite());
            } else {
                ps.setNull(7, Types.VARCHAR);
                ps.setNull(8, Types.VARCHAR);
            }

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de l'ajout : " + e.getMessage());
        }
    }

    // =============================================
    // MODIFIER UN UTILISATEUR
    // =============================================
    public void update(Utilisateur u) throws DatabaseException {
        String sql = "UPDATE utilisateurs SET nom=?, prenom=?, email=?, role=? WHERE id=?";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, u.getNom());
            ps.setString(2, u.getPrenom());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getRole());
            ps.setInt(5, u.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la modification : " + e.getMessage());
        }
    }

    // =============================================
    // SUPPRIMER UN UTILISATEUR
    // =============================================
    public void delete(int id) throws DatabaseException {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la suppression : " + e.getMessage());
        }
    }

    // =============================================
    // VALIDER UN COMPTE
    // =============================================
    public void validerCompte(int id) throws DatabaseException {
        String sql = "UPDATE utilisateurs SET statut = 'ACTIF' WHERE id = ?";
        try {
            Connection con = DatabaseConnection.getInstance();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Erreur lors de la validation : " + e.getMessage());
        }
    }

    // =============================================
    // MÉTHODE PRIVÉE — Transformer ResultSet en objet Java
    // =============================================
    private Utilisateur construireUtilisateur(ResultSet rs) throws SQLException {
        String role = rs.getString("role");
        Utilisateur u;

        // On crée le bon type selon le rôle
        switch (role) {
            case "CHEF_DEPARTEMENT":
                ChefDepartement chef = new ChefDepartement();
                chef.setDepartement(rs.getString("departement"));
                u = chef;
                break;
            case "ENSEIGNANT":
                Enseignant enseignant = new Enseignant();
                enseignant.setSpecialite(rs.getString("specialite"));
                u = enseignant;
                break;
            default:
                u = new ResponsableClasse();
                break;
        }

        // Attributs communs à tous
        u.setId(rs.getInt("id"));
        u.setNom(rs.getString("nom"));
        u.setPrenom(rs.getString("prenom"));
        u.setEmail(rs.getString("email"));
        u.setMotDePasse(rs.getString("mot_de_passe"));
        u.setRole(role);
        u.setStatut(rs.getString("statut"));

        return u;
    }
}