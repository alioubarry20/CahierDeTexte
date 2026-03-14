package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.SeanceService;
import com.esitec.cahier.service.StatistiquesService;
import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EnseignantDashboard extends BaseView {

    private StatistiquesService statsService  = new StatistiquesService();
    private CoursService        coursService  = new CoursService();
    private SeanceService       seanceService = new SeanceService();

    public EnseignantDashboard() {
        super("Tableau de bord — Enseignant");
        SIDEBAR_BG     = new Color(25, 60, 95);
        SIDEBAR_TOP    = new Color(15, 45, 75);
        SIDEBAR_ACTIVE = new Color(0, 120, 215);
        initialiserUI();
        verifierSeancesRejetees();
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
        contenu.add(creerHeader("Tableau de bord"), BorderLayout.NORTH);
        contenu.add(creerContenuAccueil(), BorderLayout.CENTER);

        construireLayout(sidebar, contenu);
        relierMenu();
    }

    private void verifierSeancesRejetees() {
        try {
            Enseignant enseignant = (Enseignant) Session.getUtilisateurConnecte();
            List<Cours> cours = coursService.listerParEnseignant(enseignant.getId());

            List<String> rejets = new ArrayList<>();
            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    if ("REJETE".equals(s.getStatut())
                            && s.getCommentaireRejet() != null
                            && !s.getCommentaireRejet().isEmpty()) {
                        rejets.add("• " + c.getIntitule()
                            + " (" + s.getDate() + ") : "
                            + s.getCommentaireRejet());
                    }
                }
            }

            if (!rejets.isEmpty()) {
                StringBuilder msg = new StringBuilder();
                msg.append("Vous avez ")
                   .append(rejets.size())
                   .append(" seance(s) rejetee(s) :\n\n");
                for (String r : rejets) {
                    msg.append(r).append("\n");
                }
                msg.append("\nConsultez 'Mes Seances' pour plus de details.");

                JOptionPane.showMessageDialog(this,
                    msg.toString(),
                    "⚠ Seances rejetees",
                    JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            // silencieux
        }
    }

    private JPanel creerContenuAccueil() {
        JPanel panel = new JPanel();
        panel.setBackground(COULEUR_FOND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        Enseignant enseignant = (Enseignant) Session.getUtilisateurConnecte();

        JLabel lblHello = new JLabel("Hello, " + enseignant.getPrenom() + " !");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblHello.setForeground(new Color(0, 120, 215));
        lblHello.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Bienvenue dans votre espace enseignant");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        lblSub.setForeground(new Color(136, 136, 136));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        cards.setBackground(COULEUR_FOND);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            List<Cours> cours = coursService.listerParEnseignant(enseignant.getId());
            int nbCours = cours.size();
            int nbSeances = 0, nbValidees = 0, nbRejets = 0;
            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                nbSeances += seances.size();
                for (Seance s : seances) {
                    if ("VALIDE".equals(s.getStatut()))   nbValidees++;
                    if ("REJETE".equals(s.getStatut()))   nbRejets++;
                }
            }
            cards.add(creerCarteStats("Mes cours",      String.valueOf(nbCours),    new Color(0, 120, 215)));
            cards.add(creerCarteStats("Mes seances",    String.valueOf(nbSeances),  new Color(0, 180, 120)));
            cards.add(creerCarteStats("Validees",       String.valueOf(nbValidees), new Color(255, 140, 0)));
            cards.add(creerCarteStats("Rejetees",       String.valueOf(nbRejets),   new Color(220, 50, 50)));
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
                changerContenu("Tableau de bord", creerContenuAccueil());
            }
        });
        if (items.length >= 2) items[1].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Ajouter une seance", new AjouterSeanceView().creerPanneau());
            }
        });
        if (items.length >= 3) items[2].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Mes Seances", new HistoriqueSeancesView().creerPanneau());
            }
        });
        if (items.length >= 4) items[3].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Mes Cours", new MesCoursView().creerPanneau());
            }
        });
    }
}