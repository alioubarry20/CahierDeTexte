package com.esitec.cahier.util;

import com.esitec.cahier.model.Utilisateur;

public class Session {

    private static Utilisateur utilisateurConnecte = null;

    private Session() {}

    public static void connecter(Utilisateur utilisateur) {
        utilisateurConnecte = utilisateur;
    }

    public static void deconnecter() {
        utilisateurConnecte = null;
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static boolean estConnecte() {
        return utilisateurConnecte != null;
    }

    public static boolean estChef() {
        return estConnecte() &&
               utilisateurConnecte.getRole().equals("CHEF_DEPARTEMENT");
    }

    public static boolean estEnseignant() {
        return estConnecte() &&
               utilisateurConnecte.getRole().equals("ENSEIGNANT");
    }

    public static boolean estResponsable() {
        return estConnecte() &&
               utilisateurConnecte.getRole().equals("RESPONSABLE_CLASSE");
    }
}