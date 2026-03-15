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
            {"\uf015", "Accueil"},
            {"\uf067", "Ajouter Seance"},
            {"\uf073", "Mes Seances"},
            {"\uf02d", "Mes Cours"}
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
                for (String r : rejets) msg.append(r).append("\n");
                msg.append("\nConsultez 'Mes Seances' pour plus de details.");
                JOptionPane.showMessageDialog(this, msg.toString(),
                    "Seances rejetees", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) { /* silencieux */ }
    }

    private JPanel creerContenuAccueil() {
        JPanel panel = new JPanel();
        panel.setBackground(COULEUR_FOND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        Enseignant enseignant = (Enseignant) Session.getUtilisateurConnecte();

        JLabel lblHello = new JLabel("Hello, " + enseignant.getPrenom() + " !");
        lblHello.setFont(new Font("Segoe UI", Font.BOLD, 42));
        lblHello.setForeground(new Color(0, 120, 215));
        lblHello.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Bienvenue dans votre espace enseignant");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        lblSub.setForeground(new Color(136, 136, 136));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cards
        JPanel cards = new JPanel(new GridLayout(1, 4, 16, 0));
        cards.setBackground(COULEUR_FOND);
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);
        cards.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));

        try {
            List<Cours> cours = coursService.listerParEnseignant(enseignant.getId());
            int nbCours = cours.size();
            int nbSeances = 0, nbValidees = 0, nbRejets = 0;
            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                nbSeances += seances.size();
                for (Seance s : seances) {
                    if ("VALIDE".equals(s.getStatut()))  nbValidees++;
                    if ("REJETE".equals(s.getStatut()))  nbRejets++;
                }
            }
            cards.add(creerCarteStats("Mes cours",   String.valueOf(nbCours),    new Color(0, 120, 215)));
            cards.add(creerCarteStats("Mes seances", String.valueOf(nbSeances),  new Color(0, 180, 120)));
            cards.add(creerCarteStats("Validees",    String.valueOf(nbValidees), new Color(255, 140, 0)));
            cards.add(creerCarteStats("Rejetees",    String.valueOf(nbRejets),   new Color(220, 50, 50)));
        } catch (Exception e) {
            cards.add(new JLabel("Erreur stats"));
        }

        // Bas : séances récentes + progression
        JPanel bas = new JPanel(new GridLayout(1, 2, 16, 0));
        bas.setBackground(COULEUR_FOND);
        bas.setAlignmentX(Component.LEFT_ALIGNMENT);
        bas.setMaximumSize(new Dimension(Integer.MAX_VALUE, 280));

        bas.add(creerSeancesRecentes(enseignant));
        bas.add(creerProgressionCours(enseignant));

        panel.add(lblHello);
        panel.add(Box.createVerticalStrut(6));
        panel.add(lblSub);
        panel.add(Box.createVerticalStrut(24));
        panel.add(cards);
        panel.add(Box.createVerticalStrut(24));
        panel.add(bas);

        return panel;
    }

    private JPanel creerSeancesRecentes(Enseignant enseignant) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titre = new JLabel("Mes seances recentes");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(new Color(50, 50, 50));
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(titre);
        panel.add(Box.createVerticalStrut(12));

        try {
            List<Cours> cours = coursService.listerParEnseignant(enseignant.getId());
            List<String[]> lignes = new ArrayList<>();

            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    lignes.add(new String[]{
                        s.getStatut(),
                        c.getIntitule(),
                        s.getDate() != null ? s.getDate().toString() : "",
                        s.getHeure() != null ? s.getHeure().toString() : "",
                        s.getDuree() + "min"
                    });
                    if (lignes.size() >= 4) break;
                }
                if (lignes.size() >= 4) break;
            }

            if (lignes.isEmpty()) {
                JLabel vide = new JLabel("Aucune seance");
                vide.setFont(new Font("Segoe UI", Font.ITALIC, 12));
                vide.setForeground(new Color(180, 180, 180));
                vide.setAlignmentX(Component.LEFT_ALIGNMENT);
                panel.add(vide);
            } else {
                for (String[] l : lignes) {
                    panel.add(creerLigneSeance(l[0], l[1], l[2], l[3], l[4]));
                    panel.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception e) { /* silencieux */ }

        return panel;
    }

    private JPanel creerLigneSeance(String statut, String cours,
                                     String date, String heure, String duree) {
        JPanel ligne = new JPanel(new BorderLayout(10, 0));
        ligne.setBackground(Color.WHITE);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);

        Color couleur;
        String icone;
        switch (statut) {
            case "VALIDE":   couleur = new Color(0, 180, 120); icone = "\uf00c"; break;
            case "REJETE":   couleur = new Color(220, 50, 50); icone = "\uf00d"; break;
            default:         couleur = new Color(255, 140, 0); icone = "\uf017"; break;
        }

        JPanel iconBox = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(couleur.getRed(),
                    couleur.getGreen(), couleur.getBlue(), 30));
                g2.fillRoundRect(0, 0, 34, 34, 8, 8);
                g2.setColor(couleur);
                g2.setFont(fontAwesome.deriveFont(13f));
                FontMetrics fm = g2.getFontMetrics();
                int x = (34 - fm.stringWidth(icone)) / 2;
                int y = (34 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(icone, x, y);
            }
        };
        iconBox.setPreferredSize(new Dimension(34, 34));
        iconBox.setOpaque(false);

        JPanel infos = new JPanel();
        infos.setBackground(Color.WHITE);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));

        JLabel lblCours = new JLabel(cours);
        lblCours.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblCours.setForeground(new Color(50, 50, 50));

        JLabel lblDetail = new JLabel(date + " · " + heure + " · " + duree);
        lblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblDetail.setForeground(new Color(170, 170, 170));

        infos.add(lblCours);
        infos.add(lblDetail);

        JLabel badge = new JLabel(statut);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 10));
        badge.setForeground(couleur);
        badge.setBackground(new Color(couleur.getRed(),
            couleur.getGreen(), couleur.getBlue(), 25));
        badge.setOpaque(true);
        badge.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 8));

        ligne.add(iconBox, BorderLayout.WEST);
        ligne.add(infos, BorderLayout.CENTER);
        ligne.add(badge, BorderLayout.EAST);

        return ligne;
    }

    private JPanel creerProgressionCours(Enseignant enseignant) {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titre = new JLabel("Progression par cours");
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
            List<Cours> cours = coursService.listerParEnseignant(enseignant.getId());
            if (cours.isEmpty()) {
                JLabel vide = new JLabel("Aucun cours assigne");
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

                    panel.add(creerLigneProgression(
                        c.getIntitule(), pct, nbVal, total, col));
                    panel.add(Box.createVerticalStrut(16));
                }
            }
        } catch (Exception e) { /* silencieux */ }

        return panel;
    }

    private JPanel creerLigneProgression(String nom, int pct,
                                          int nbVal, int total, Color couleur) {
        JPanel ligne = new JPanel();
        ligne.setBackground(Color.WHITE);
        ligne.setLayout(new BoxLayout(ligne, BoxLayout.Y_AXIS));
        ligne.setAlignmentX(Component.LEFT_ALIGNMENT);
        ligne.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

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

        // Barre
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

        JLabel lblDetail = new JLabel(nbVal + " seances validees / " + total + "h");
        lblDetail.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblDetail.setForeground(new Color(170, 170, 170));

        ligne.add(header);
        ligne.add(Box.createVerticalStrut(4));
        ligne.add(barre);
        ligne.add(Box.createVerticalStrut(3));
        ligne.add(lblDetail);

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
                changerContenu("Ajouter une seance",
                    new AjouterSeanceView().creerPanneau());
            }
        });
        if (items.length >= 3) items[2].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Mes Seances",
                    new HistoriqueSeancesView().creerPanneau());
            }
        });
        if (items.length >= 4) items[3].addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                changerContenu("Mes Cours",
                    new MesCoursView().creerPanneau());
            }
        });
    }
}