package com.esitec.cahier.model;

import java.time.LocalDate;
import java.util.List;

public class FicheSuivi {

    // Attributs
    private int id;
    private Cours cours;
    private Enseignant enseignant;
    private List<Seance> seances;
    private LocalDate dateGeneration;
    private int heuresEffectuees;
    private int heuresRestantes;
    private double tauxAvancement; // en pourcentage

    // Constructeur vide
    public FicheSuivi() {}

    // Constructeur avec paramètres
    public FicheSuivi(Cours cours, Enseignant enseignant, List<Seance> seances) {
        this.cours = cours;
        this.enseignant = enseignant;
        this.seances = seances;
        this.dateGeneration = LocalDate.now();
        calculerStatistiques();
    }

    // Calcul automatique des heures et du taux
    private void calculerStatistiques() {
        // Total des minutes des séances validées uniquement
        int totalMinutes = 0;
        for (Seance s : seances) {
            if (s.getStatut().equals("VALIDEE")) {
                totalMinutes += s.getDuree();
            }
        }

        this.heuresEffectuees = totalMinutes / 60;
        this.heuresRestantes = cours.getVolumeHoraire() - this.heuresEffectuees;

        // Taux d'avancement en %
        if (cours.getVolumeHoraire() > 0) {
            this.tauxAvancement = ((double) heuresEffectuees / cours.getVolumeHoraire()) * 100;
        } else {
            this.tauxAvancement = 0;
        }
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Cours getCours() { return cours; }
    public void setCours(Cours cours) { this.cours = cours; }

    public Enseignant getEnseignant() { return enseignant; }
    public void setEnseignant(Enseignant enseignant) { this.enseignant = enseignant; }

    public List<Seance> getSeances() { return seances; }
    public void setSeances(List<Seance> seances) { this.seances = seances; }

    public LocalDate getDateGeneration() { return dateGeneration; }

    public int getHeuresEffectuees() { return heuresEffectuees; }

    public int getHeuresRestantes() { return heuresRestantes; }

    public double getTauxAvancement() { return tauxAvancement; }

    // Méthodes utilitaires
    public int getNombreSeancesValidees() {
        int count = 0;
        for (Seance s : seances) {
            if (s.getStatut().equals("VALIDEE")) count++;
        }
        return count;
    }

    public int getNombreSeancesEnAttente() {
        int count = 0;
        for (Seance s : seances) {
            if (s.getStatut().equals("EN_ATTENTE")) count++;
        }
        return count;
    }

    @Override
    public String toString() {
        return "Fiche de suivi - " + cours.getIntitule() +
               " | Avancement: " + String.format("%.1f", tauxAvancement) + "%" +
               " | Heures effectuées: " + heuresEffectuees + "h" +
               " | Heures restantes: " + heuresRestantes + "h";
    }
}