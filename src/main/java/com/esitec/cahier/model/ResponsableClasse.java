package com.esitec.cahier.model;

public class ResponsableClasse extends Utilisateur {

    // Attribut spécifique au responsable
    private Classe classe;

    // Constructeur vide
    public ResponsableClasse() {
        super();
    }

    // Constructeur avec paramètres
    public ResponsableClasse(int id, String nom, String prenom, String email, Classe classe) {
        super(id, nom, prenom, email, "RESPONSABLE_CLASSE");
        this.classe = classe;
    }

    // Getter & Setter
    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    @Override
    public String toString() {
        return "Responsable: " + getNomComplet() + " - Classe: " + classe;
    }
}