package com.esitec.cahier.service;

import com.esitec.cahier.dao.SeanceDAO;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.exception.ValidationException;
import com.esitec.cahier.model.Seance;
import java.util.List;

public class SeanceService {

    private final SeanceDAO dao = new SeanceDAO();

    // Lister les séances d'un cours
    public List<Seance> listerParCours(int coursId) throws DatabaseException {
        return dao.findByCours(coursId);
    }

    // Lister les séances en attente d'une classe
    public List<Seance> listerEnAttenteParClasse(int classeId) throws DatabaseException {
        return dao.findEnAttenteByClasse(classeId);
    }

    // Ajouter une séance
    public void ajouter(Seance s) throws DatabaseException, ValidationException {
        if (s.getContenu() == null || s.getContenu().trim().isEmpty())
            throw new ValidationException("Le contenu est obligatoire.");
        if (s.getDate() == null)
            throw new ValidationException("La date est obligatoire.");
        if (s.getHeure() == null)
            throw new ValidationException("L'heure est obligatoire.");
        if (s.getDuree() <= 0)
            throw new ValidationException("La durée doit être supérieure à 0.");
        if (s.getCours() == null)
            throw new ValidationException("Le cours est obligatoire.");
        dao.save(s);
    }

    // Modifier une séance
    public void modifier(Seance s) throws DatabaseException, ValidationException {
        if (!s.isModifiable())
            throw new ValidationException("Impossible de modifier une séance déjà validée ou rejetée.");
        if (s.getContenu() == null || s.getContenu().trim().isEmpty())
            throw new ValidationException("Le contenu est obligatoire.");
        dao.update(s);
    }

    // Valider une séance
    public void valider(int id) throws DatabaseException {
        dao.valider(id);
    }

    // Rejeter une séance
    public void rejeter(int id, String commentaire) throws DatabaseException, ValidationException {
        if (commentaire == null || commentaire.trim().isEmpty())
            throw new ValidationException("Un commentaire est obligatoire pour rejeter une séance.");
        dao.rejeter(id, commentaire);
    }
}