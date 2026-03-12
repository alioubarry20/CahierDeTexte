package com.esitec.cahier.service;

import com.esitec.cahier.dao.CoursDAO;
import com.esitec.cahier.dao.SeanceDAO;
import com.esitec.cahier.dao.UtilisateurDAO;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.model.Cours;
import java.util.List;

public class StatistiquesService {

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final CoursDAO coursDAO = new CoursDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    // Nombre total d'enseignants
    public int getNombreEnseignants() throws DatabaseException {
        List<Utilisateur> tous = utilisateurDAO.findAll();
        return (int) tous.stream()
                .filter(u -> u.getRole().equals("ENSEIGNANT"))
                .count();
    }

    // Nombre total de cours
    public int getNombreCours() throws DatabaseException {
        return coursDAO.findAll().size();
    }

    // Nombre de séances validées
    public int getNombreSeancesValidees() throws DatabaseException {
        List<Cours> cours = coursDAO.findAll();
        int total = 0;
        for (Cours c : cours) {
            List<Seance> seances = seanceDAO.findByCours(c.getId());
            total += seances.stream()
                    .filter(s -> s.getStatut().equals("VALIDEE"))
                    .count();
        }
        return total;
    }

    // Nombre de séances en attente
    public int getNombreSeancesEnAttente() throws DatabaseException {
        List<Cours> cours = coursDAO.findAll();
        int total = 0;
        for (Cours c : cours) {
            List<Seance> seances = seanceDAO.findByCours(c.getId());
            total += seances.stream()
                    .filter(s -> s.getStatut().equals("EN_ATTENTE"))
                    .count();
        }
        return total;
    }

    // Taux d'avancement d'un cours
    public double getTauxAvancement(int coursId) throws DatabaseException {
        Cours cours = coursDAO.findAll().stream()
                .filter(c -> c.getId() == coursId)
                .findFirst()
                .orElse(null);

        if (cours == null) return 0;

        List<Seance> seances = seanceDAO.findByCours(coursId);
        int minutesValidees = seances.stream()
                .filter(s -> s.getStatut().equals("VALIDEE"))
                .mapToInt(Seance::getDuree)
                .sum();

        int heuresValidees = minutesValidees / 60;
        if (cours.getVolumeHoraire() == 0) return 0;

        return ((double) heuresValidees / cours.getVolumeHoraire()) * 100;
    }
}