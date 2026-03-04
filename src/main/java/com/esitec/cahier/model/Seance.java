package com.esitec.cahier.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class Seance {

    // Attributs
    private int id;
    private LocalDate date;
    private LocalTime heure;
    private int duree; // en minutes
    private String contenu;
    private String observations;
    private String statut; // EN_ATTENTE, VALIDEE, REJETEE
    private String commentaireRejet;
    private Cours cours;

    // Constructeur vide
    public Seance() {
        this.statut = "EN_ATTENTE";
    }

    // Constructeur avec paramètres
    public Seance(int id, LocalDate date, LocalTime heure, int duree, 
                  String contenu, String observations, Cours cours) {
        this.id = id;
        this.date = date;
        this.heure = heure;
        this.duree = duree;
        this.contenu = contenu;
        this.observations = observations;
        this.cours = cours;
        this.statut = "EN_ATTENTE";
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getHeure() { return heure; }
    public void setHeure(LocalTime heure) { this.heure = heure; }

    public int getDuree() { return duree; }
    public void setDuree(int duree) { this.duree = duree; }

    public String getContenu() { return contenu; }
    public void setContenu(String contenu) { this.contenu = contenu; }

    public String getObservations() { return observations; }
    public void setObservations(String observations) { this.observations = observations; }

    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }

    public String getCommentaireRejet() { return commentaireRejet; }
    public void setCommentaireRejet(String commentaireRejet) { this.commentaireRejet = commentaireRejet; }

    public Cours getCours() { return cours; }
    public void setCours(Cours cours) { this.cours = cours; }

    // Méthodes utilitaires
    public boolean isModifiable() {
        return this.statut.equals("EN_ATTENTE");
    }

    public double getDureeEnHeures() {
        return this.duree / 60.0;
    }

    @Override
    public String toString() {
        return "Séance du " + date + " à " + heure + 
               " (" + duree + " min) - " + statut;
    }
}