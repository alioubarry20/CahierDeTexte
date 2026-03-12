package com.esitec.cahier.service;

import com.esitec.cahier.dao.ClasseDAO;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.exception.ValidationException;
import com.esitec.cahier.model.Classe;
import java.util.List;

public class ClasseService {

    private final ClasseDAO dao = new ClasseDAO();

    // Lister toutes les classes
    public List<Classe> listerTous() throws DatabaseException {
        return dao.findAll();
    }

    // Ajouter une classe
    public void ajouter(Classe c) throws DatabaseException, ValidationException {
        if (c.getNom() == null || c.getNom().trim().isEmpty())
            throw new ValidationException("Le nom de la classe est obligatoire.");
        if (c.getFiliere() == null || c.getFiliere().trim().isEmpty())
            throw new ValidationException("La filière est obligatoire.");
        dao.save(c);
    }

    // Modifier une classe
    public void modifier(Classe c) throws DatabaseException, ValidationException {
        if (c.getNom() == null || c.getNom().trim().isEmpty())
            throw new ValidationException("Le nom de la classe est obligatoire.");
        dao.update(c);
    }

    // Supprimer une classe
    public void supprimer(int id) throws DatabaseException {
        dao.delete(id);
    }

    // Trouver une classe par ID
    public Classe trouverParId(int id) throws DatabaseException {
        return dao.findById(id);
    }
}