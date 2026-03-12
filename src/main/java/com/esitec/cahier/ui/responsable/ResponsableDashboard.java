package com.esitec.cahier.ui.responsable;

import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.SeanceService;
import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ResponsableDashboard extends BaseView {

    private CoursService coursService = new CoursService();
    private SeanceService seanceService = new SeanceService();

    public ResponsableDashboard() {
        super("Tableau de bord — Responsable de classe");
        SIDEBAR_BG     = new Color(40, 80, 60);
        SIDEBAR_TOP    = new Color(25, 60, 45);
        SIDEBAR_ACTIVE = new Color(0, 160, 100);
        initialiserUI();
    }

    private void initialiserUI() {
        String[][] menu = {
            {"", "Accueil"},
            {"", "Valider Seances"},
            {"", "Cahier de Texte"},
            {"", "Avancement"}
        };
        JPanel sidebar = creerSidebar("Espace Responsable", menu);

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(new Color(240, 248, 244));
        contenu.add(creerHeader("Mon Tableau de bord"), BorderLayout.NORTH);
        contenu.add(creerContenuAccueil(), BorderLayout.CENTER);

        construireLayout(sidebar, contenu);
        relierMenu();
    }

    private JPanel creerContenuAccueil() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 248, 244));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel lblHello = new JLabel("Hello !");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 52));
        lblHello.setForeground(new Color(0, 160, 100));
        lblHello.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Bienvenue dans votre espace responsable");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblSub.setForeground(new Color(136, 136, 136));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        cards.setBackground(new Color(240, 248, 244));
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            List<Cours> cours = coursService.listerTous();
            int nbAttente = 0, nbValidees = 0, nbRejetees = 0;
            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    if (s.getStatut().equals("EN_ATTENTE")) nbAttente++;
                    if (s.getStatut().equals("VALIDEE"))    nbValidees++;
                    if (s.getStatut().equals("REJETEE"))    nbRejetees++;
                }
            }
            cards.add(creerCarteStats("En attente", String.valueOf(nbAttente),  new Color(255, 140, 0)));
            cards.add(creerCarteStats("Validees",   String.valueOf(nbValidees), new Color(0, 180, 120)));
            cards.add(creerCarteStats("Rejetees",   String.valueOf(nbRejetees), new Color(200, 50, 50)));
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

    private void relierMenu() {
        Component[] items = sidebarItems.getComponents();

        if (items.length >= 1) items[0].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Mon Tableau de bord", creerContenuAccueil());
            }
        });
        if (items.length >= 2) items[1].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Validation des seances", new ValidationSeanceView().creerPanneau());
            }
        });
        if (items.length >= 3) items[2].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Cahier de texte", new CahierDeTexteView().creerPanneau());
            }
        });
        if (items.length >= 4) items[3].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Avancement du programme", new AvancementView().creerPanneau());
            }
        });
    }
}