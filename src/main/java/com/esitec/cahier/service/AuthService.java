package com.esitec.cahier.service;

import com.esitec.cahier.dao.UtilisateurDAO;
import com.esitec.cahier.exception.AuthException;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.util.Session;

public class AuthService {

    private final UtilisateurDAO utilisateurDAO;

    public AuthService() {
        this.utilisateurDAO = new UtilisateurDAO();
    }

    public Utilisateur connecter(String email, String motDePasse)
            throws AuthException {

        // Vérifier que les champs ne sont pas vides
        if (email == null || email.trim().isEmpty())
            throw new AuthException("L'email est obligatoire.");

        if (motDePasse == null || motDePasse.trim().isEmpty())
            throw new AuthException("Le mot de passe est obligatoire.");

        try {
            // Chercher l'utilisateur en BDD
            Utilisateur utilisateur = utilisateurDAO
                    .trouverParEmailEtMotDePasse(
                            email.trim().toLowerCase(),
                            motDePasse
                    );

            if (utilisateur == null)
                throw new AuthException(
                    "Email ou mot de passe incorrect, " +
                    "ou compte non encore validé."
                );

            // Ouvrir la session
            Session.connecter(utilisateur);
            return utilisateur;

        } catch (DatabaseException e) {
            throw new AuthException("Erreur de connexion : " + e.getMessage());
        }
    }

    public void deconnecter() {
        Session.deconnecter();
    }
}