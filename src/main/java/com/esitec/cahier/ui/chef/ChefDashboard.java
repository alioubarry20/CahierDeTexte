package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.SeanceService;
import com.esitec.cahier.service.StatistiquesService;
import com.esitec.cahier.service.UtilisateurService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChefDashboard extends BaseView {

    private StatistiquesService statsService      = new StatistiquesService();
    private UtilisateurService  utilisateurService = new UtilisateurService();
    private CoursService        coursService       = new CoursService();
    private SeanceService       seanceService      = new SeanceService();

    public ChefDashboard() {
        super("Tableau de bord — Chef de departement");
        SIDEBAR_BG     = new Color(45, 45, 80);
        SIDEBAR_TOP    = new Color(35, 35, 65);
        SIDEBAR_ACTIVE = new Color(70, 70, 120);
        initialiserUI();
    }

    private void initialiserUI() {
        String[][] menu = {
            {"\uf015", "Accueil"},
            {"\uf0c0", "Enseignants"},
            {"\uf19c", "Classes"},
            {"\uf02d", "Cours"},
            {"\uf080", "Statistiques"},
            {"\uf0f6", "Fiche de Suivi"},
            {"\uf00c", "Valider Comptes"}
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
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Hello
        JLabel lblHello = new JLabel("Hello !");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 46));
        lblHello.setForeground(new Color(0, 120, 215));
        lblHello.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Bienvenue dans votre espace chef de departement");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(new Color(136, 136, 136));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cards stats
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setBackground(COULEUR_FOND);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        try {
            cards.add(creerCarteStats("Enseignants",
                String.valueOf(statsService.getNombreEnseignants()),
                new Color(0, 120, 215)));
            cards.add(creerCarteStats("Cours",
                String.valueOf(statsService.getNombreCours()),
                new Color(0, 180, 120)));
            cards.add(creerCarteStats("Seances val.",
                String.valueOf(statsService.getNombreSeancesValidees()),
                new Color(255, 140, 0)));
            cards.add(creerCarteStats("En attente",
                String.valueOf(statsService.getNombreSeancesEnAttente()),
                new Color(150, 50, 200)));
        } catch (Exception e) {
            cards.add(new JLabel("Erreur stats"));
        }

        // Notification comptes en attente
        JPanel notifPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        notifPanel.setBackground(COULEUR_FOND);
        notifPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        try {
            List<Utilisateur> enAttente = utilisateurService.listerEnAttente();
            if (!enAttente.isEmpty()) {
                JLabel lblNotif = new JLabel(
                    "  \uf0f3  " + enAttente.size() + " compte(s) en attente de validation !");
                lblNotif.setFont(new Font("Segoe UI", Font.BOLD, 12));
                lblNotif.setForeground(Color.WHITE);
                lblNotif.setBackground(new Color(200, 50, 50));
                lblNotif.setOpaque(true);
                lblNotif.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                lblNotif.setCursor(new Cursor(Cursor.HAND_CURSOR));
                lblNotif.addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        changerContenu("Validation des comptes",
                            new ValidationComptesView().creerPanneau());
                    }
                });
                notifPanel.add(lblNotif);
            }
        } catch (Exception e) { /* silencieux */ }

        // Ligne du bas : activité récente + top enseignants
        JPanel bas = new JPanel(new GridLayout(1, 2, 16, 0));
        bas.setBackground(COULEUR_FOND);
        bas.setAlignmentX(Component.LEFT_ALIGNMENT);
        bas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        bas.add(creerActiviteRecente());
        bas.add(creerTopEnseignants());

        panel.add(lblHello);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(24));
        panel.add(cards);
        panel.add(Box.createVerticalStrut(12));
        panel.add(notifPanel);
        panel.add(Box.createVerticalStrut(24));
        panel.add(bas);

        return panel;
    }

    private JPanel creerActiviteRecente() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titre = new JLabel("Activite recente");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(new Color(50, 50, 50));
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titre);
        panel.add(Box.createVerticalStrut(12));

        try {
            // Charger les dernières séances
            List<Cours> cours = coursService.listerTous();
            List<String[]> activites = new ArrayList<>();

            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    String statut = s.getStatut();
                    String ensNom = c.getEnseignant() != null
                        ? c.getEnseignant().getPrenom() + " " + c.getEnseignant().getNom()
                        : "Inconnu";
                    activites.add(new String[]{statut, c.getIntitule(), ensNom, s.getDate().toString()});
                    if (activites.size() >= 4) break;
                }
                if (activites.size() >= 4) break;
            }

            if (activites.isEmpty()) {
                JLabel vide = new JLabel("Aucune activite recente");
                vide.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                vide.setForeground(new Color(180, 180, 180));
                vide.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(vide);
            } else {
                for (String[] act : activites) {
                    panel.add(creerLigneActivite(act[0], act[1], act[2], act[3]));
                    panel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) {
            JLabel err = new JLabel("Erreur chargement");
            err.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            err.setForeground(Color.RED);
            panel.add(err);
        }

        return panel;
    }

    private JPanel creerLigneActivite(String statut, String cours,
                                       String enseignant, String date) {
        JPanel ligne = new JPanel(new BorderLayout(10, 0));
        ligne.setBackground(Color.WHITE);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Icone cercle
        Color couleur;
        String icone;
        switch (statut) {
            case "VALIDE":    couleur = new Color(0, 180, 120);  icone = "\uf00c"; break;
            case "REJETE":    couleur = new Color(220, 50, 50);  icone = "\uf00d"; break;
            default:          couleur = new Color(255, 140, 0);  icone = "\uf017"; break;
        }

        JPanel cercle = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                Color bg = new Color(couleur.getRed(), couleur.getGreen(),
                    couleur.getBlue(), 30);
                g2.setColor(bg);
                g2.fillOval(0, 0, 34, 34);
                g2.setColor(couleur);
                g2.setFont(fontAwesome.deriveFont(13f));
                FontMetrics fm = g2.getFontMetrics();
                int x = (34 - fm.stringWidth(icone)) / 2;
                int y = (34 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(icone, x, y);
            }
        };
        cercle.setPreferredSize(new Dimension(34, 34));
        cercle.setOpaque(false);

        JPanel infos = new JPanel();
        infos.setBackground(Color.WHITE);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        JLabel lblCours = new JLabel("Seance — " + cours);
        lblCours.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCours.setForeground(new Color(50, 50, 50));

        JLabel lblEns = new JLabel(enseignant + " · " + date);
        lblEns.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblEns.setForeground(new Color(170, 170, 170));

        infos.add(lblCours);
        infos.add(lblEns);

        // Badge statut
        JLabel badge = new JLabel(statut);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(couleur);
        badge.setBackground(new Color(couleur.getRed(), couleur.getGreen(),
            couleur.getBlue(), 25));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        ligne.add(cercle, BorderLayout.WEST);
        ligne.add(infos, BorderLayout.CENTER);
        ligne.add(badge, BorderLayout.EAST);

        return ligne;
    }

    private JPanel creerTopEnseignants() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titre = new JLabel("Top enseignants");
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
            List<Utilisateur> tous = utilisateurService.listerTous();
            List<int[]> scores = new ArrayList<>();
            List<Enseignant> enseignants = new ArrayList<>();

            for (Utilisateur u : tous) {
                if (u instanceof Enseignant) {
                    Enseignant ens = (Enseignant) u;
                    int nbSeances = 0;
                    List<Cours> cours = coursService.listerParEnseignant(ens.getId());
                    for (Cours c : cours) {
                        List<Seance> seances = seanceService.listerParCours(c.getId());
                        for (Seance s : seances) {
                            if ("VALIDE".equals(s.getStatut())) nbSeances++;
                        }
                    }
                    enseignants.add(ens);
                    scores.add(new int[]{enseignants.size() - 1, nbSeances});
                }
            }

            // Trier par score
            scores.sort((a, b) -> b[1] - a[1]);

            if (scores.isEmpty()) {
                JLabel vide = new JLabel("Aucun enseignant");
                vide.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                vide.setForeground(new Color(180, 180, 180));
                vide.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(vide);
            } else {
                int max = scores.get(0)[1] > 0 ? scores.get(0)[1] : 1;
                int limit = Math.min(4, scores.size());

                for (int i = 0; i < limit; i++) {
                    Enseignant ens = enseignants.get(scores.get(i)[0]);
                    int nb = scores.get(i)[1];
                    Color c = couleurs[i % couleurs.length];
                    panel.add(creerLigneEnseignant(ens, nb, max, c));
                    panel.add(Box.createVerticalStrut(14));
                }
            }
        } catch (Exception e) {
            JLabel err = new JLabel("Erreur chargement");
            err.setFont(new Font("Segoe UI", Font.ITALIC, 12));
            err.setForeground(Color.RED);
            panel.add(err);
        }

        return panel;
    }

    private JPanel creerLigneEnseignant(Enseignant ens, int nb,
                                         int max, Color couleur) {
        JPanel ligne = new JPanel(new BorderLayout(10, 0));
        ligne.setBackground(Color.WHITE);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Avatar
        String initiales = String.valueOf(ens.getPrenom().charAt(0)).toUpperCase()
            + String.valueOf(ens.getNom().charAt(0)).toUpperCase();
        JPanel avatar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(couleur);
                g2.fillOval(0, 0, 32, 32);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                FontMetrics fm = g2.getFontMetrics();
                int x = (32 - fm.stringWidth(initiales)) / 2;
                int y = (32 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initiales, x, y);
            }
        };
        avatar.setPreferredSize(new Dimension(32, 32));
        avatar.setOpaque(false);

        // Nom + barre
        JPanel infos = new JPanel();
        infos.setBackground(Color.WHITE);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        JLabel lblNom = new JLabel(ens.getPrenom() + " " + ens.getNom());
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblNom.setForeground(new Color(50, 50, 50));

        // Barre progression
        int largeur = max > 0 ? (int) ((nb * 1.0 / max) * 150) : 0;
        JPanel barre = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(230, 230, 230));
                g2.fillRoundRect(0, 0, getWidth(), 5, 4, 4);
                g2.setColor(couleur);
                g2.fillRoundRect(0, 0, largeur, 5, 4, 4);
            }
        };
        barre.setPreferredSize(new Dimension(150, 5));
        barre.setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
        barre.setOpaque(false);

        infos.add(lblNom);
        infos.add(Box.createVerticalStrut(4));
        infos.add(barre);

        // Score
        JLabel lblScore = new JLabel(String.valueOf(nb));
        lblScore.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblScore.setForeground(new Color(150, 150, 150));

        ligne.add(avatar, BorderLayout.WEST);
        ligne.add(infos, BorderLayout.CENTER);
        ligne.add(lblScore, BorderLayout.EAST);

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
                changerContenu("Gestion des utilisateurs",
                    new GestionUtilisateursView().creerPanneau());
            }
        });
        if (items.length >= 3) items[2].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Gestion des classes",
                    new GestionClassesView().creerPanneau());
            }
        });
        if (items.length >= 4) items[3].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Gestion des cours",
                    new GestionCoursView().creerPanneau());
            }
        });
        if (items.length >= 5) items[4].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Statistiques",
                    new StatistiquesView().creerPanneau());
            }
        });
        if (items.length >= 6) items[5].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Fiche de suivi",
                    new FicheSuiviView().creerPanneau());
            }
        });
        if (items.length >= 7) items[6].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Validation des comptes",
                    new ValidationComptesView().creerPanneau());
            }
        });
    }
}