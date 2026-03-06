package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;

/**
 * Tableau de bord de l'Enseignant.
 */
public class EnseignantDashboard extends BaseView {

    public EnseignantDashboard() {
        super("Tableau de bord — Enseignant");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());

        add(creerHeader("👨‍🏫 Espace Enseignant"), BorderLayout.NORTH);

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(COULEUR_FOND);
        contenu.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblBienvenue = new JLabel(
                "Bonjour, " + Session.getUtilisateurConnecte().getNomComplet()
        );
        lblBienvenue.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenue.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        // 4 actions pour l'enseignant
        JPanel grilleActions = new JPanel(new GridLayout(2, 2, 20, 20));
        grilleActions.setOpaque(false);

        grilleActions.add(creerCarte(
                "📋", "Mes cours",
                "Voir la liste de vos cours assignés",
                COULEUR_SECONDAIRE, e -> ouvrirMesCours()));

        grilleActions.add(creerCarte(
                "➕", "Ajouter une séance",
                "Enregistrer une nouvelle séance de cours",
                COULEUR_SUCCES, e -> ouvrirAjouterSeance()));

        grilleActions.add(creerCarte(
                "📅", "Historique des séances",
                "Consulter toutes vos séances enregistrées",
                new Color(142, 68, 173), e -> ouvrirHistorique()));

        grilleActions.add(creerCarte(
                "📄", "Ma fiche de suivi",
                "Générer votre fiche pédagogique PDF",
                new Color(211, 84, 0), e -> ouvrirFicheSuivi()));

        contenu.add(lblBienvenue, BorderLayout.NORTH);
        contenu.add(grilleActions, BorderLayout.CENTER);

        add(contenu, BorderLayout.CENTER);
    }

    private JPanel creerCarte(String icone, String titre,
                               String description, Color couleur,
                               java.awt.event.ActionListener action) {
        JPanel carte = new JPanel(new BorderLayout());
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JLabel lblIcone = new JLabel(icone);
        lblIcone.setFont(new Font("Arial", Font.PLAIN, 40));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 15));
        lblTitre.setForeground(couleur);

        JLabel lblDesc = new JLabel("<html>" + description + "</html>");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

        JButton btn = creerBouton("Accéder", couleur);
        btn.addActionListener(action);

        JPanel textes = new JPanel();
        textes.setLayout(new BoxLayout(textes, BoxLayout.Y_AXIS));
        textes.setOpaque(false);
        textes.add(lblTitre);
        textes.add(lblDesc);
        textes.add(btn);

        carte.add(lblIcone, BorderLayout.NORTH);
        carte.add(textes, BorderLayout.CENTER);

        return carte;
    }

    private void ouvrirMesCours() {
        afficherSucces("Fonctionnalité en cours de développement !");
    }

    private void ouvrirAjouterSeance() {
        new AjouterSeanceView().setVisible(true);
    }

    private void ouvrirHistorique() {
        new HistoriqueSeancesView().setVisible(true);
    }

    private void ouvrirFicheSuivi() {
        afficherSucces("Fonctionnalité en cours de développement !");
    }
}