package com.esitec.cahier.service;

import com.esitec.cahier.dao.CoursDAO;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.exception.ValidationException;
import com.esitec.cahier.model.Cours;
import java.util.List;

public class CoursService {

    private final CoursDAO dao = new CoursDAO();

    // Lister tous les cours
    public List<Cours> listerTous() throws DatabaseException {
        return dao.findAll();
    }

    // Cours d'un enseignant
    public List<Cours> listerParEnseignant(int enseignantId) throws DatabaseException {
        return dao.findByEnseignant(enseignantId);
    }

    // Cours d'une classe
    public List<Cours> listerParClasse(int classeId) throws DatabaseException {
        return dao.findByClasse(classeId);
    }

    // Ajouter un cours
    public void ajouter(Cours c) throws DatabaseException, ValidationException {
        if (c.getIntitule() == null || c.getIntitule().trim().isEmpty())
            throw new ValidationException("L'intitulé du cours est obligatoire.");
        if (c.getVolumeHoraire() <= 0)
            throw new ValidationException("Le volume horaire doit être supérieur à 0.");
        if (c.getEnseignant() == null)
            throw new ValidationException("L'enseignant est obligatoire.");
        if (c.getClasse() == null)
            throw new ValidationException("La classe est obligatoire.");
        dao.save(c);
    }

    // Modifier un cours
    public void modifier(Cours c) throws DatabaseException, ValidationException {
        if (c.getIntitule() == null || c.getIntitule().trim().isEmpty())
            throw new ValidationException("L'intitulé du cours est obligatoire.");
        dao.update(c);
    }

    // Supprimer un cours
    public void supprimer(int id) throws DatabaseException {
        dao.delete(id);
    }
}