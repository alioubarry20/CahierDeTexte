package com.esitec.cahier.ui.chef;

import com.esitec.cahier.service.StatistiquesService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class ChefDashboard extends BaseView {

    private StatistiquesService statsService = new StatistiquesService();

    public ChefDashboard() {
        super("Tableau de bord — Chef de departement");
        SIDEBAR_BG     = new Color(45, 45, 80);
        SIDEBAR_TOP    = new Color(35, 35, 65);
        SIDEBAR_ACTIVE = new Color(70, 70, 120);
        initialiserUI();
    }

    private void initialiserUI() {
        String[][] menu = {
            {"", "Accueil"},
            {"", "Utilisateurs"},
            {"", "Classes"},
            {"", "Cours"},
            {"", "Statistiques"},
            {"", "Fiche de Suivi"}
        };
        JPanel sidebar = creerSidebar("Espace Chef de Departement", menu);

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(COULEUR_FOND);
        contenu.add(creerHeader("Tableau de bord"), BorderLayout.NORTH);
        contenu.add(creerContenuAccueil(), BorderLayout.CENTER);

        construireLayout(sidebar, contenu);
        relierMenu();
    }

    private JPanel creerContenuAccueil() {
        JPanel panel = new JPanel();
        panel.setBackground(COULEUR_FOND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel lblHello = new JLabel("Hello !");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 52));
        lblHello.setForeground(new Color(0, 120, 215));
        lblHello.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Bienvenue dans votre espace chef de departement");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblSub.setForeground(new Color(136, 136, 136));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        cards.setBackground(COULEUR_FOND);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            int nbEns   = statsService.getNombreEnseignants();
            int nbCours = statsService.getNombreCours();
            int nbVal   = statsService.getNombreSeancesValidees();
            int nbAtt   = statsService.getNombreSeancesEnAttente();

            cards.add(creerCarteStats("Enseignants",  String.valueOf(nbEns),   new Color(0, 120, 215)));
            cards.add(creerCarteStats("Cours",        String.valueOf(nbCours), new Color(0, 180, 120)));
            cards.add(creerCarteStats("Seances val.", String.valueOf(nbVal),   new Color(255, 140, 0)));
            cards.add(creerCarteStats("En attente",   String.valueOf(nbAtt),   new Color(150, 50, 200)));
        } catch (Exception e) {
            cards.add(new JLabel("Erreur chargement stats"));
        }

        panel.add(lblHello);
        panel.add(Box.createVerticalStrut(8));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(30));
        panel.add(cards);

        return panel;
    }

    private JPanel creerPanneauUtilisateurs() {
        return new GestionUtilisateursView().creerPanneau();
    }

    private JPanel creerPanneauClasses() {
        return new GestionClassesView().creerPanneau();
    }

    private JPanel creerPanneauCours() {
        return new GestionCoursView().creerPanneau();
    }

    private JPanel creerPanneauStats() {
        return new StatistiquesView().creerPanneau();
    }

    private JPanel creerPanneauFiche() {
        return new FicheSuiviView().creerPanneau();
    }

    private void relierMenu() {
        Component[] items = sidebarItems.getComponents();

        if (items.length >= 1) items[0].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Tableau de bord", creerContenuAccueil());
            }
        });
        if (items.length >= 2) items[1].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Gestion des utilisateurs", creerPanneauUtilisateurs());
            }
        });
        if (items.length >= 3) items[2].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Gestion des classes", creerPanneauClasses());
            }
        });
        if (items.length >= 4) items[3].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Gestion des cours", creerPanneauCours());
            }
        });
        if (items.length >= 5) items[4].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Statistiques", creerPanneauStats());
            }
        });
        if (items.length >= 6) items[5].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Fiche de suivi", creerPanneauFiche());
            }
        });
    }
}