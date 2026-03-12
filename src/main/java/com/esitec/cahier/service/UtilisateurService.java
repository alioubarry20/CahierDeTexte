package com.esitec.cahier.service;

import com.esitec.cahier.dao.UtilisateurDAO;
import com.esitec.cahier.exception.AuthException;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.exception.ValidationException;
import com.esitec.cahier.model.Utilisateur;
import java.util.List;

public class UtilisateurService {

    private final UtilisateurDAO dao = new UtilisateurDAO();

    // Lister tous les utilisateurs
    public List<Utilisateur> listerTous() throws DatabaseException {
        return dao.findAll();
    }

    // Ajouter un utilisateur
    public void ajouter(Utilisateur u) throws DatabaseException, ValidationException {
        if (u.getNom() == null || u.getNom().trim().isEmpty())
            throw new ValidationException("Le nom est obligatoire.");
        if (u.getEmail() == null || u.getEmail().trim().isEmpty())
            throw new ValidationException("L'email est obligatoire.");
        if (u.getMotDePasse() == null || u.getMotDePasse().trim().isEmpty())
            throw new ValidationException("Le mot de passe est obligatoire.");
        dao.save(u);
    }

    // Modifier un utilisateur
    public void modifier(Utilisateur u) throws DatabaseException, ValidationException {
        if (u.getNom() == null || u.getNom().trim().isEmpty())
            throw new ValidationException("Le nom est obligatoire.");
        dao.update(u);
    }

    // Supprimer un utilisateur
    public void supprimer(int id) throws DatabaseException {
        dao.delete(id);
    }

    // Valider un compte
    public void validerCompte(int id) throws DatabaseException {
        dao.validerCompte(id);
    }

    // Lister les comptes en attente
    public List<Utilisateur> listerEnAttente() throws DatabaseException {
        List<Utilisateur> tous = dao.findAll();
        tous.removeIf(u -> !u.getStatut().equals("EN_ATTENTE"));
        return tous;
    }
}