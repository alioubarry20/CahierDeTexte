package com.esitec.cahier.model;

public class ChefDepartement extends Utilisateur {

    // Attribut spécifique au chef
    private String departement;

    // Constructeur vide
    public ChefDepartement() {
        super();
    }

    // Constructeur avec paramètres
    public ChefDepartement(int id, String nom, String prenom, String email, String departement) {
        super(id, nom, prenom, email, "CHEF_DEPARTEMENT");
        this.departement = departement;
    }

    // Getter & Setter
    public String getDepartement() { return departement; }
    public void setDepartement(String departement) { this.departement = departement; }

    @Override
    public String toString() {
        return "Chef: " + getNomComplet() + " - Département: " + departement;
    }
}