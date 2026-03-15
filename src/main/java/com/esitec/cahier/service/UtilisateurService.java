package com.esitec.cahier.service;

import com.esitec.cahier.dao.UtilisateurDAO;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.exception.ValidationException;
import com.esitec.cahier.model.ResponsableClasse;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.util.MailService;
import java.util.List;

public class UtilisateurService {

    private final UtilisateurDAO dao = new UtilisateurDAO();

    public List<Utilisateur> listerTous() throws DatabaseException {
        return dao.findAll();
    }

    public void ajouter(Utilisateur u) throws DatabaseException, ValidationException {
        if (u.getNom() == null || u.getNom().trim().isEmpty())
            throw new ValidationException("Le nom est obligatoire.");
        if (u.getEmail() == null || u.getEmail().trim().isEmpty())
            throw new ValidationException("L'email est obligatoire.");
        if (u.getMotDePasse() == null || u.getMotDePasse().trim().isEmpty())
            throw new ValidationException("Le mot de passe est obligatoire.");

        dao.save(u);

        // Mail si responsable avec classe assignee
        try {
            if (u instanceof ResponsableClasse) {
                ResponsableClasse resp = (ResponsableClasse) u;
                if (resp.getClasse() != null) {
                    MailService.getInstance().mailClasseAssignee(
                        resp.getEmail(),
                        resp.getPrenom() + " " + resp.getNom(),
                        resp.getClasse().getNom()
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Mail non envoye : " + e.getMessage());
        }
    }

    public void modifier(Utilisateur u) throws DatabaseException, ValidationException {
        if (u.getNom() == null || u.getNom().trim().isEmpty())
            throw new ValidationException("Le nom est obligatoire.");
        dao.update(u);
    }

    public void supprimer(int id) throws DatabaseException {
        dao.delete(id);
    }

    public void validerCompte(int id) throws DatabaseException {
        dao.validerCompte(id);
    }

    public List<Utilisateur> listerEnAttente() throws DatabaseException {
        List<Utilisateur> tous = dao.findAll();
        tous.removeIf(u -> !u.getStatut().equals("EN_ATTENTE"));
        return tous;
    }
}