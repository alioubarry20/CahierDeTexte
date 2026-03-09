package com.esitec.cahier.util;

import com.esitec.cahier.model.Utilisateur;

public class Session {

    private static Utilisateur utilisateurConnecte = null;

    private Session() {}

    public static void setUtilisateurConnecte(Utilisateur u) {
        utilisateurConnecte = u;
    }

    public static Utilisateur getUtilisateurConnecte() {
        return utilisateurConnecte;
    }

    public static boolean isConnecte() {
        return utilisateurConnecte != null;
    }

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

    public static void clear() {
        utilisateurConnecte = null;
    }

    // =============================================
    // METHODES ALIASES - compatibilité binôme
    // =============================================
    public static void connecter(Utilisateur u) {
        setUtilisateurConnecte(u);
    }

    public static void deconnecter() {
        clear();
    }

    public static boolean estConnecte() {
        return isConnecte();
    }
}