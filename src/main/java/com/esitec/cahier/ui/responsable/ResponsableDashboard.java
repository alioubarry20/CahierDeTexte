package com.esitec.cahier.ui.responsable;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.ResponsableClasse;
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

public class ResponsableDashboard extends BaseView {

    private StatistiquesService statsService  = new StatistiquesService();
    private CoursService        coursService  = new CoursService();
    private SeanceService       seanceService = new SeanceService();

    public ResponsableDashboard() {
        super("Tableau de bord — Responsable de classe");
        SIDEBAR_BG     = new Color(25, 70, 45);
        SIDEBAR_TOP    = new Color(15, 55, 32);
        SIDEBAR_ACTIVE = new Color(0, 140, 80);
        initialiserUI();
    }

    private void initialiserUI() {
        String[][] menu = {
            {"\uf015", "Accueil"},
            {"\uf00c", "Validation Seances"},
            {"\uf02d", "Cahier de Texte"},
            {"\uf201", "Avancement"}
        };
        JPanel sidebar = creerSidebar("Espace Responsable", menu);

        JPanel contenu = new JPanel(new BorderLayout());
        contenu.setBackground(COULEUR_FOND);
        contenu.add(creerHeader("Tableau de bord"), BorderLayout.NORTH);
        contenu.add(new JScrollPane(creerContenuAccueil()), BorderLayout.CENTER);

        construireLayout(sidebar, contenu);
        relierMenu();
    }

    private JPanel creerContenuAccueil() {
        JPanel panel = new JPanel();
        panel.setBackground(COULEUR_FOND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        ResponsableClasse responsable =
            (ResponsableClasse) Session.getUtilisateurConnecte();

        JLabel lblHello = new JLabel("Hello, " + responsable.getPrenom() + " !");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblHello.setForeground(new Color(0, 160, 90));
        lblHello.setAlignmentX(Component.LEFT_ALIGNMENT);

        String nomClasse = responsable.getClasse() != null
            ? responsable.getClasse().getNom() : "Non assignee";
        JLabel lblClasse = new JLabel("Classe : " + nomClasse);
        lblClasse.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblClasse.setForeground(new Color(0, 140, 80));
        lblClasse.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Bienvenue dans votre espace responsable de classe");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(new Color(136, 136, 136));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setBackground(COULEUR_FOND);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        try {
            List<Cours> cours = responsable.getClasse() != null
                ? coursService.listerParClasse(responsable.getClasse().getId())
                : coursService.listerTous();

            int nbCours = cours.size();
            int nbEnAttente = 0, nbValidees = 0, nbRejetes = 0;
            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    if ("EN_ATTENTE".equals(s.getStatut())) nbEnAttente++;
                    if ("VALIDE".equals(s.getStatut()))     nbValidees++;
                    if ("REJETE".equals(s.getStatut()))     nbRejetes++;
                }
            }
            cards.add(creerCarteStats("Cours",      String.valueOf(nbCours),     new Color(0, 120, 215)));
            cards.add(creerCarteStats("En attente", String.valueOf(nbEnAttente), new Color(255, 140, 0)));
            cards.add(creerCarteStats("Validees",   String.valueOf(nbValidees),  new Color(0, 180, 120)));
            cards.add(creerCarteStats("Rejetees",   String.valueOf(nbRejetes),   new Color(220, 50, 50)));
        } catch (Exception e) {
            cards.add(new JLabel("Erreur stats"));
        }

        // Bas
        JPanel bas = new JPanel(new GridLayout(1, 2, 16, 0));
        bas.setBackground(COULEUR_FOND);
        bas.setAlignmentX(Component.LEFT_ALIGNMENT);
        bas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));

        bas.add(creerSeancesEnAttente(responsable));
        bas.add(creerAvancementCours(responsable));

        panel.add(lblHello);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblClasse);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(24));
        panel.add(cards);
        panel.add(Box.createVerticalStrut(24));
        panel.add(bas);

        return panel;
    }

    private JPanel creerSeancesEnAttente(ResponsableClasse responsable) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titre = new JLabel("Seances en attente de validation");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(new Color(50, 50, 50));
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titre);
        panel.add(Box.createVerticalStrut(12));

        try {
            List<Cours> cours = responsable.getClasse() != null
                ? coursService.listerParClasse(responsable.getClasse().getId())
                : coursService.listerTous();

            List<Seance> enAttente = new ArrayList<>();
            List<String> nomsCours = new ArrayList<>();

            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    if ("EN_ATTENTE".equals(s.getStatut())) {
                        enAttente.add(s);
                        nomsCours.add(c.getIntitule()
                            + (c.getEnseignant() != null
                                ? " — " + c.getEnseignant().getPrenom()
                                  + " " + c.getEnseignant().getNom()
                                : ""));
                    }
                    if (enAttente.size() >= 4) break;
                }
                if (enAttente.size() >= 4) break;
            }

            if (enAttente.isEmpty()) {
                JLabel vide = new JLabel("Aucune seance en attente !");
                vide.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                vide.setForeground(new Color(0, 180, 120));
                vide.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(vide);
            } else {
                for (int i = 0; i < enAttente.size(); i++) {
                    final Seance seance = enAttente.get(i);
                    final String nomCours = nomsCours.get(i);
                    panel.add(creerLigneValidation(seance, nomCours));
                    panel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) { /* silencieux */ }

        return panel;
    }

    private JPanel creerLigneValidation(Seance seance, String nomCours) {
        JPanel ligne = new JPanel(new BorderLayout(10, 0));
        ligne.setBackground(Color.WHITE);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Icone
        JPanel iconBox = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 140, 0, 30));
                g2.fillRoundRect(0, 0, 34, 34, 8, 8);
                g2.setColor(new Color(255, 140, 0));
                g2.setFont(fontAwesome.deriveFont(13f));
                String ic = "\uf017";
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(ic,
                    (34 - fm.stringWidth(ic)) / 2,
                    (34 + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        iconBox.setPreferredSize(new Dimension(34, 34));
        iconBox.setOpaque(false);

        // Infos
        JPanel infos = new JPanel();
        infos.setBackground(Color.WHITE);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        JLabel lblNom = new JLabel(nomCours);
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNom.setForeground(new Color(50, 50, 50));

        JLabel lblDate = new JLabel(
            (seance.getDate() != null ? seance.getDate().toString() : "")
            + " · " + (seance.getHeure() != null ? seance.getHeure().toString() : "")
            + " · " + seance.getDuree() + "min");
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblDate.setForeground(new Color(170, 170, 170));

        infos.add(lblNom);
        infos.add(lblDate);

        // Boutons
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        boutons.setBackground(Color.WHITE);

        JButton btnValider = new JButton("V") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(232, 249, 242));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(new Color(0, 180, 120));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("V",
                    (getWidth() - fm.stringWidth("V")) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btnValider.setPreferredSize(new Dimension(34, 28));
        btnValider.setBorderPainted(false);
        btnValider.setContentAreaFilled(false);
        btnValider.setFocusPainted(false);
        btnValider.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnValider.addActionListener(e -> {
            try {
                seanceService.valider(seance.getId());
                afficherSucces("Seance validee !");
                changerContenu("Tableau de bord", creerContenuAccueil());
            } catch (Exception ex) {
                afficherErreur("Erreur : " + ex.getMessage());
            }
        });

        JButton btnRejeter = new JButton("X") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(252, 232, 232));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                g2.setColor(new Color(220, 50, 50));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString("X",
                    (getWidth() - fm.stringWidth("X")) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btnRejeter.setPreferredSize(new Dimension(34, 28));
        btnRejeter.setBorderPainted(false);
        btnRejeter.setContentAreaFilled(false);
        btnRejeter.setFocusPainted(false);
        btnRejeter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRejeter.addActionListener(e -> {
            String motif = JOptionPane.showInputDialog(
                ResponsableDashboard.this,
                "Motif du rejet :", "Rejeter",
                JOptionPane.QUESTION_MESSAGE);
            if (motif != null && !motif.trim().isEmpty()) {
                try {
                    seanceService.rejeter(seance.getId(), motif);
                    afficherSucces("Seance rejetee !");
                    changerContenu("Tableau de bord", creerContenuAccueil());
                } catch (Exception ex) {
                    afficherErreur("Erreur : " + ex.getMessage());
                }
            }
        });

        boutons.add(btnValider);
        boutons.add(btnRejeter);

        ligne.add(iconBox, BorderLayout.WEST);
        ligne.add(infos, BorderLayout.CENTER);
        ligne.add(boutons, BorderLayout.EAST);

        return ligne;
    }

    private JPanel creerAvancementCours(ResponsableClasse responsable) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titre = new JLabel("Avancement des cours");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(new Color(50, 50, 50));
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titre);
        panel.add(Box.createVerticalStrut(12));

        Color[] couleurs = {
            new Color(0, 120, 215), new Color(0, 180, 120),
            new Color(255, 140, 0), new Color(150, 50, 200)
        };

        try {
            List<Cours> cours = responsable.getClasse() != null
                ? coursService.listerParClasse(responsable.getClasse().getId())
                : coursService.listerTous();

            if (cours.isEmpty()) {
                JLabel vide = new JLabel("Aucun cours pour cette classe");
                vide.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                vide.setForeground(new Color(180, 180, 180));
                vide.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(vide);
            } else {
                for (int i = 0; i < Math.min(4, cours.size()); i++) {
                    Cours c = cours.get(i);
                    List<Seance> seances = seanceService.listerParCours(c.getId());
                    int nbVal = 0;
                    for (Seance s : seances) {
                        if ("VALIDE".equals(s.getStatut())) nbVal++;
                    }
                    int total = c.getVolumeHoraire() > 0 ? c.getVolumeHoraire() : 1;
                    int pct = Math.min(100, nbVal * 100 / total);
                    Color col = couleurs[i % couleurs.length];
                    panel.add(creerLigneAvancement(c.getIntitule(), pct, col));
                    panel.add(Box.createVerticalStrut(16));
                }
            }
        } catch (Exception e) { /* silencieux */ }

        return panel;
    }

    private JPanel creerLigneAvancement(String nom, int pct, Color couleur) {
        JPanel ligne = new JPanel();
        ligne.setBackground(Color.WHITE);
        ligne.setLayout(new BoxLayout(ligne, BoxLayout.Y_AXIS));
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);

        JLabel lblNom = new JLabel(nom);
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNom.setForeground(new Color(50, 50, 50));

        JLabel lblPct = new JLabel(pct + "%");
        lblPct.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPct.setForeground(couleur);

        header.add(lblNom, BorderLayout.WEST);
        header.add(lblPct, BorderLayout.EAST);

        final int finalPct = pct;
        JPanel barre = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 230, 230));
                g2.fillRoundRect(0, 0, getWidth(), 6, 4, 4);
                int w = (int) (getWidth() * finalPct / 100.0);
                g2.setColor(couleur);
                g2.fillRoundRect(0, 0, w, 6, 4, 4);
            }
        };
        barre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 6));
        barre.setOpaque(false);

        ligne.add(header);
        ligne.add(Box.createVerticalStrut(6));
        ligne.add(barre);

        return ligne;
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
                changerContenu("Validation des seances",
                    new ValidationSeanceView().creerPanneau());
            }
        });
        if (items.length >= 3) items[2].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Cahier de texte",
                    new CahierDeTexteView().creerPanneau());
            }
        });
        if (items.length >= 4) items[3].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Avancement",
                    new AvancementView().creerPanneau());
            }
        });
    }
}