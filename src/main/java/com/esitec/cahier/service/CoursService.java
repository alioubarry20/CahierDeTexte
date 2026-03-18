package com.esitec.cahier.service;

import com.esitec.cahier.dao.CoursDAO;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.exception.ValidationException;
import com.esitec.cahier.model.Cours;
import com.esitec.cahier.util.MailService;
import java.util.List;

public class CoursService {
    private final CoursDAO dao = new CoursDAO();

    public List<Cours> listerTous() throws DatabaseException {
        return dao.findAll();
    }

    public List<Cours> listerParEnseignant(int enseignantId) throws DatabaseException {
        return dao.findByEnseignant(enseignantId);
    }

    public List<Cours> listerParClasse(int classeId) throws DatabaseException {
        return dao.findByClasse(classeId);
    }

    public void ajouter(Cours c) throws DatabaseException, ValidationException {
        if (c.getIntitule() == null || c.getIntitule().trim().isEmpty())
            throw new ValidationException("L'intitule du cours est obligatoire.");
        if (c.getVolumeHoraire() <= 0)
            throw new ValidationException("Le volume horaire doit etre superieur a 0.");
        //if (c.getEnseignant() == null)
         //   throw new ValidationException("L'enseignant est obligatoire.");
        if (c.getClasse() == null)
            throw new ValidationException("La classe est obligatoire.");

        dao.save(c);

        // Envoi mail à l'enseignant
        try {
            String nomEns = c.getEnseignant().getPrenom()
                + " " + c.getEnseignant().getNom();
            String nomClasse = c.getClasse() != null
                ? c.getClasse().getNom() : "Non assignee";
            MailService.getInstance().mailCoursAssigne(
                c.getEnseignant().getEmail(),
                nomEns,
                c.getIntitule(),
                nomClasse,
                c.getVolumeHoraire()
            );
        } catch (Exception e) {
            System.err.println("Mail non envoye : " + e.getMessage());
        }
    }

    public void modifier(Cours c) throws DatabaseException, ValidationException {
        if (c.getIntitule() == null || c.getIntitule().trim().isEmpty())
            throw new ValidationException("L'intitule du cours est obligatoire.");
        dao.update(c);
    }

    public void supprimer(int id) throws DatabaseException {
        dao.delete(id);
    }
}