package com.esitec.cahier.service;

import com.esitec.cahier.dao.CoursDAO;
import com.esitec.cahier.dao.SeanceDAO;
import com.esitec.cahier.dao.UtilisateurDAO;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.exception.ValidationException;
import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.FicheSuivi;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.util.PdfExportUtil;
import com.esitec.cahier.util.ExcelExportUtil;
import java.util.List;

public class FicheSuiviService {

    private final SeanceDAO seanceDAO = new SeanceDAO();
    private final CoursDAO coursDAO = new CoursDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    // Générer une fiche de suivi
    public FicheSuivi genererFiche(int coursId) throws DatabaseException, ValidationException {
        // Récupérer le cours
        List<Cours> tousLesCours = coursDAO.findAll();
        Cours cours = tousLesCours.stream()
                .filter(c -> c.getId() == coursId)
                .findFirst()
                .orElse(null);

        if (cours == null)
            throw new ValidationException("Cours introuvable.");

        // Récupérer l'enseignant
        Enseignant enseignant = (Enseignant) utilisateurDAO
                .findByEmailAndPassword(
                    cours.getEnseignant().getEmail(), null
                );

        // Récupérer les séances validées
        List<Seance> seances = seanceDAO.findByCours(coursId);
        seances.removeIf(s -> !s.getStatut().equals("VALIDEE"));

        return new FicheSuivi(cours, cours.getEnseignant(), seances);
    }

    // Exporter en PDF
    public void exporterPDF(int coursId, String cheminFichier)
            throws DatabaseException, ValidationException {
        FicheSuivi fiche = genererFiche(coursId);
        PdfExportUtil.exporter(fiche, cheminFichier);
    }

    // Exporter en Excel
    public void exporterExcel(int coursId, String cheminFichier)
            throws DatabaseException, ValidationException {
        FicheSuivi fiche = genererFiche(coursId);
        ExcelExportUtil.exporter(fiche, cheminFichier);
    }
}