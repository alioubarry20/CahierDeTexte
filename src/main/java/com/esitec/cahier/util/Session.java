package com.esitec.cahier.util;

import com.esitec.cahier.model.Utilisateur;
/*
by aliou
desc: Session.java est une classe utilitaire qui gère la session de l'utilisateur connecté.
Elle stocke en mémoire l'utilisateur connecté et fournit des méthodes pour vérifier son rôle et son statut de connexion.
date: 2025-03-09
*/

public class Session {

    // L'utilisateur connecté en mémoire
    private static Utilisateur utilisateurConnecte = null;

    // Constructeur privé — personne ne peut faire new Session()
    private Session() {}

    // Connecter un utilisateur
    public static void setUtilisateurConnecte(Utilisateur u) {
        utilisateurConnecte = u;
    }

    // Récupérer l'utilisateur connecté
    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    // Vérifier si quelqu'un est connecté
    public static boolean isConnecte() {
        return utilisateurConnecte != null;
    }

    // Vérifier le rôle de l'utilisateur connecté
    public static boolean isChef() {
        return isConnecte() && 
               utilisateurConnecte.getRole().equals("CHEF_DEPARTEMENT");
    }

    public static boolean isEnseignant() {
        return isConnecte() && 
               utilisateurConnecte.getRole().equals("ENSEIGNANT");
    }

    public static boolean isResponsable() {
        return isConnecte() && 
               utilisateurConnecte.getRole().equals("RESPONSABLE_CLASSE");
    }

    // Déconnecter — vider la session
    public static void clear() {
        utilisateurConnecte = null;
    }
}