package com.esitec.cahier.ui.responsable;

import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;

/**
 * Tableau de bord du Responsable de classe.
 */
public class ResponsableDashboard extends BaseView {

    public ResponsableDashboard() {
        super("Tableau de bord — Responsable de classe");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());

        add(creerHeader("📋 Espace Responsable"), BorderLayout.NORTH);

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(COULEUR_FOND);
        contenu.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblBienvenue = new JLabel(
                "Bienvenue, " + Session.getUtilisateurConnecte().getNomComplet()
        );
        lblBienvenue.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenue.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        // 3 actions pour le responsable
        JPanel grilleActions = new JPanel(new GridLayout(1, 3, 20, 20));
        grilleActions.setOpaque(false);

        grilleActions.add(creerCarte(
                "📖", "Cahier de texte",
                "Consulter le cahier de texte de votre classe",
                COULEUR_SECONDAIRE, e -> ouvrirCahierDeTexte()));

        grilleActions.add(creerCarte(
                "✅", "Valider les séances",
                "Valider ou rejeter les séances en attente",
                COULEUR_SUCCES, e -> ouvrirValidation()));

        grilleActions.add(creerCarte(
                "📊", "Avancement programme",
                "Voir l'état d'avancement du programme",
                new Color(142, 68, 173), e -> ouvrirAvancement()));

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
                BorderFactory.createEmptyBorder(30, 25, 25, 25)
        ));

        JLabel lblIcone = new JLabel(icone);
        lblIcone.setFont(new Font("Arial", Font.PLAIN, 45));
        lblIcone.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 15));
        lblTitre.setForeground(couleur);
        lblTitre.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel lblDesc = new JLabel("<html><center>" + description + "</center></html>");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);
        lblDesc.setHorizontalAlignment(SwingConstants.CENTER);
        lblDesc.setBorder(BorderFactory.createEmptyBorder(8, 0, 20, 0));

        JButton btn = creerBouton("Accéder", couleur);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(action);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bas.setOpaque(false);
        bas.add(btn);

        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setOpaque(false);
        centre.add(lblTitre);
        centre.add(lblDesc);

        carte.add(lblIcone, BorderLayout.NORTH);
        carte.add(centre, BorderLayout.CENTER);
        carte.add(bas, BorderLayout.SOUTH);

        return carte;
    }

    private void ouvrirCahierDeTexte() {
        new CahierDeTexteView().setVisible(true);
    }

    private void ouvrirValidation() {
        new ValidationSeanceView().setVisible(true);
    }

    private void ouvrirAvancement() {
        afficherSucces("Fonctionnalité en cours de développement !");
    }
}
