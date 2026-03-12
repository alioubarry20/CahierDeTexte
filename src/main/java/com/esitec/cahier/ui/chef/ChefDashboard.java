package com.esitec.cahier.ui.chef;

import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;

public class ChefDashboard extends BaseView {

    public ChefDashboard() {
        super("Tableau de bord — Chef de département");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());

        add(creerHeader("👨‍💼 Chef de département"), BorderLayout.NORTH);

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(COULEUR_FOND);
        contenu.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblBienvenue = new JLabel(
                "Bienvenue, " + Session.getUtilisateurConnecte().getNomComplet() + " !"
        );
        lblBienvenue.setFont(new Font("Arial", Font.BOLD, 18));
        lblBienvenue.setBorder(BorderFactory.createEmptyBorder(0, 0, 25, 0));

        JPanel grilleActions = new JPanel(new GridLayout(2, 3, 20, 20));
        grilleActions.setOpaque(false);

        grilleActions.add(creerCarteAction(
                "👥", "Gérer les utilisateurs",
                "Ajouter enseignants et responsables",
                COULEUR_SECONDAIRE, e -> ouvrirGestionUtilisateurs()));

        grilleActions.add(creerCarteAction(
                "📚", "Gérer les cours",
                "Assigner des cours aux enseignants",
                new Color(142, 68, 173), e -> ouvrirGestionCours()));

        grilleActions.add(creerCarteAction(
                "✅", "Valider les comptes",
                "Approuver les nouvelles inscriptions",
                COULEUR_SUCCES, e -> ouvrirValidationComptes()));

        grilleActions.add(creerCarteAction(
                "📄", "Fiche de suivi",
                "Générer les fiches pédagogiques PDF",
                new Color(211, 84, 0), e -> ouvrirFicheSuivi()));

        grilleActions.add(creerCarteAction(
                "📊", "Statistiques",
                "Voir les statistiques globales",
                new Color(22, 160, 133), e -> ouvrirStatistiques()));

        grilleActions.add(creerCarteAction(
                "🏫", "Gérer les classes",
                "Ajouter et gérer les classes",
                new Color(39, 60, 117), e -> ouvrirGestionClasses()));

        contenu.add(lblBienvenue, BorderLayout.NORTH);
        contenu.add(grilleActions, BorderLayout.CENTER);

        add(contenu, BorderLayout.CENTER);
    }

    private JPanel creerCarteAction(String icone, String titre,
                                     String description, Color couleur,
                                     java.awt.event.ActionListener action) {
        JPanel carte = new JPanel(new BorderLayout());
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        carte.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel haut = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        haut.setOpaque(false);

        JLabel lblIcone = new JLabel(icone + " ");
        lblIcone.setFont(new Font("Arial", Font.PLAIN, 28));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitre.setForeground(couleur);

        haut.add(lblIcone);
        haut.add(lblTitre);

        JLabel lblDesc = new JLabel("<html>" + description + "</html>");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 12));
        lblDesc.setForeground(Color.GRAY);

        JButton btn = creerBouton("Ouvrir", couleur);
        btn.setPreferredSize(new Dimension(100, 32));
        btn.addActionListener(action);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bas.setOpaque(false);
        bas.add(btn);

        carte.add(haut, BorderLayout.NORTH);
        carte.add(lblDesc, BorderLayout.CENTER);
        carte.add(bas, BorderLayout.SOUTH);

        return carte;
    }

    // ── Actions ────────────────────────────────────────
    private void ouvrirGestionUtilisateurs() {
        new GestionUtilisateursView().setVisible(true);
    }

    private void ouvrirGestionCours() {
        new GestionCoursView().setVisible(true);
    }

    private void ouvrirValidationComptes() {
        new ValidationComptesView().setVisible(true);
    }

    private void ouvrirFicheSuivi() {
        new FicheSuiviView().setVisible(true);
    }

    private void ouvrirStatistiques() {
        new StatistiquesView().setVisible(true);
    }

    private void ouvrirGestionClasses() {
        new GestionClassesView().setVisible(true);
    }
}