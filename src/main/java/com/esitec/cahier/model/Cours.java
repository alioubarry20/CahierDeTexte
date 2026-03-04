package com.esitec.cahier.model;

public class Cours {

    // Attributs
    private int id;
    private String intitule;
    private int volumeHoraire;
    private Enseignant enseignant;
    private Classe classe;

    // Constructeur vide
    public Cours() {}

    // Constructeur avec paramètres
    public Cours(int id, String intitule, int volumeHoraire, Enseignant enseignant, Classe classe) {
        this.id = id;
        this.intitule = intitule;
        this.volumeHoraire = volumeHoraire;
        this.enseignant = enseignant;
        this.classe = classe;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getIntitule() { return intitule; }
    public void setIntitule(String intitule) { this.intitule = intitule; }

    public int getVolumeHoraire() { return volumeHoraire; }
    public void setVolumeHoraire(int volumeHoraire) { this.volumeHoraire = volumeHoraire; }

    public Enseignant getEnseignant() { return enseignant; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }

    public Classe getClasse() { return classe; }
    public void setClasse(Classe classe) { this.classe = classe; }

    @Override
    public String toString() {
        return intitule + " (" + volumeHoraire + "h) - " + classe;
    }
}