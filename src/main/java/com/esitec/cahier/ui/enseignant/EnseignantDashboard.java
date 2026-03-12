package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.SeanceService;
import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class EnseignantDashboard extends BaseView {

    private CoursService coursService = new CoursService();
    private SeanceService seanceService = new SeanceService();

    public EnseignantDashboard() {
        super("Tableau de bord — Enseignant");
        SIDEBAR_BG     = new Color(25, 60, 95);
        SIDEBAR_TOP    = new Color(15, 45, 75);
        SIDEBAR_ACTIVE = new Color(0, 120, 215);
        initialiserUI();
    }

    private void initialiserUI() {
        String[][] menu = {
            {"", "Accueil"},
            {"", "Ajouter Seance"},
            {"", "Mes Seances"},
            {"", "Mes Cours"}
        };
        JPanel sidebar = creerSidebar("Espace Enseignant", menu);

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(COULEUR_FOND);
        contenu.add(creerHeader("Mon Tableau de bord"), BorderLayout.NORTH);
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

        JLabel lblSub = new JLabel("Bienvenue dans votre espace enseignant");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 17));
        lblSub.setForeground(new Color(136, 136, 136));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        cards.setBackground(COULEUR_FOND);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            int enseignantId = Session.getUtilisateurConnecte().getId();
            List<Cours> cours = coursService.listerParEnseignant(enseignantId);
            int nbCours = cours.size();
            int nbSeances = 0, nbValidees = 0, nbAttente = 0;

            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                nbSeances += seances.size();
                for (Seance s : seances) {
                    if (s.getStatut().equals("VALIDEE"))    nbValidees++;
                    if (s.getStatut().equals("EN_ATTENTE")) nbAttente++;
                }
            }

            cards.add(creerCarteStats("Mes Cours",  String.valueOf(nbCours),   new Color(0, 120, 215)));
            cards.add(creerCarteStats("Seances",    String.valueOf(nbSeances),  new Color(0, 180, 120)));
            cards.add(creerCarteStats("En attente", String.valueOf(nbAttente),  new Color(255, 140, 0)));
            cards.add(creerCarteStats("Validees",   String.valueOf(nbValidees), new Color(150, 50, 200)));
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
                changerContenu("Ajouter une seance", new AjouterSeanceView().creerPanneau());
            }
        });
        if (items.length >= 3) items[2].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Mes seances", new HistoriqueSeancesView().creerPanneau());
            }
        });
        if (items.length >= 4) items[3].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Mes cours", new MesCoursView().creerPanneau());
            }
        });
    }
}