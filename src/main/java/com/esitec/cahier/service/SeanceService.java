package com.esitec.cahier.service;

import com.esitec.cahier.dao.SeanceDAO;
import com.esitec.cahier.dao.UtilisateurDAO;
import com.esitec.cahier.exception.DatabaseException;
import com.esitec.cahier.exception.ValidationException;
import com.esitec.cahier.model.ResponsableClasse;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.util.MailService;
import java.util.List;

public class SeanceService {

    private final SeanceDAO      dao            = new SeanceDAO();
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public List<Seance> listerParCours(int coursId) throws DatabaseException {
        return dao.findByCours(coursId);
    }

    public List<Seance> listerEnAttenteParClasse(int classeId) throws DatabaseException {
        return dao.findEnAttenteByClasse(classeId);
    }

    public void ajouter(Seance s) throws DatabaseException, ValidationException {
        if (s.getContenu() == null || s.getContenu().trim().isEmpty())
            throw new ValidationException("Le contenu est obligatoire.");
        if (s.getDate() == null)
            throw new ValidationException("La date est obligatoire.");
        if (s.getHeure() == null)
            throw new ValidationException("L'heure est obligatoire.");
        if (s.getDuree() <= 0)
            throw new ValidationException("La duree doit etre superieure a 0.");
        if (s.getCours() == null)
            throw new ValidationException("Le cours est obligatoire.");

        dao.save(s);

        // Envoi mail au responsable de la classe
        try {
            if (s.getCours() != null && s.getCours().getClasse() != null) {
                List<Utilisateur> tous = utilisateurDAO.findAll();
                for (Utilisateur u : tous) {
                    if (u instanceof ResponsableClasse) {
                        ResponsableClasse resp = (ResponsableClasse) u;
                        if (resp.getClasse() != null
                                && resp.getClasse().getId()
                                   == s.getCours().getClasse().getId()) {
                            String nomEns = s.getCours().getEnseignant() != null
                                ? s.getCours().getEnseignant().getPrenom()
                                  + " " + s.getCours().getEnseignant().getNom()
                                : "Inconnu";
                            MailService.getInstance().mailNouvelleSeance(
                                resp.getEmail(),
                                resp.getPrenom() + " " + resp.getNom(),
                                s.getCours().getIntitule(),
                                nomEns,
                                s.getDate().toString(),
                                s.getHeure().toString(),
                                s.getContenu()
                            );
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Mail non envoye : " + e.getMessage());
        }
    }

    public void modifier(Seance s) throws DatabaseException, ValidationException {
        if (!s.isModifiable())
            throw new ValidationException(
                "Impossible de modifier une seance deja validee ou rejetee.");
        if (s.getContenu() == null || s.getContenu().trim().isEmpty())
            throw new ValidationException("Le contenu est obligatoire.");
        dao.update(s);
    }

    public void valider(int id) throws DatabaseException {
        dao.valider(id);
    }

    public void rejeter(int id, String commentaire)
            throws DatabaseException, ValidationException {
        if (commentaire == null || commentaire.trim().isEmpty())
            throw new ValidationException(
                "Un commentaire est obligatoire pour rejeter une seance.");
        dao.rejeter(id, commentaire);
    }
}