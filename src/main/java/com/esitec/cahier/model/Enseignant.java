package com.esitec.cahier.model;

import java.util.List;
import java.util.ArrayList;

public class Enseignant extends Utilisateur {

    // Attributs spécifiques à l'enseignant
    private String specialite;
    private List<Cours> cours;

    // Constructeur vide
    public Enseignant() {
        super();
        this.cours = new ArrayList<>();
    }

    // Constructeur avec paramètres
    public Enseignant(int id, String nom, String prenom, String email, String specialite) {
        super(id, nom, prenom, email, "ENSEIGNANT");
        this.specialite = specialite;
        this.cours = new ArrayList<>();
    }

    // Getters & Setters
    public String getSpecialite() { return specialite; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }

    public List<Cours> getCours() { return cours; }
    public void setCours(List<Cours> cours) { this.cours = cours; }

    // Méthodes utilitaires
    public void ajouterCours(Cours c) {
        this.cours.add(c);
    }

    public int getNombreCours() {
        return this.cours.size();
    }

    @Override
    public String toString() {
        return "Enseignant: " + getNomComplet() + " - Spécialité: " + specialite;
    }
}