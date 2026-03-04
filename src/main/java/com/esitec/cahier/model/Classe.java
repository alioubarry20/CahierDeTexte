package com.esitec.cahier.model;

public class Classe {

    // Attributs
    private int id;
    private String nom;
    private String filiere;
    private String niveau;

    // Constructeur vide
    public Classe() {}

    // Constructeur avec paramètres
    public Classe(int id, String nom, String filiere, String niveau) {
        this.id = id;
        this.nom = nom;
        this.filiere = filiere;
        this.niveau = niveau;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getFiliere() { return filiere; }
    public void setFiliere(String filiere) { this.filiere = filiere; }

    public String getNiveau() { return niveau; }
    public void setNiveau(String niveau) { this.niveau = niveau; }

    @Override
    public String toString() {
        return nom + " - " + filiere + " (" + niveau + ")";
    }
}