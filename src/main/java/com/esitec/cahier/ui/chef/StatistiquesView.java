package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.SeanceService;
import com.esitec.cahier.service.StatistiquesService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StatistiquesView extends BaseView {

    private StatistiquesService statsService  = new StatistiquesService();
    private CoursService        coursService  = new CoursService();
    private SeanceService       seanceService = new SeanceService();

    public StatistiquesView() {
        super("Statistiques");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel();
        panel.setBackground(COULEUR_FOND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(24, 30, 24, 30));

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

        // Graphiques
        JPanel graphiques = new JPanel(new GridLayout(1, 2, 16, 0));
        graphiques.setBackground(COULEUR_FOND);
        graphiques.setAlignmentX(Component.LEFT_ALIGNMENT);
        graphiques.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

        graphiques.add(creerGraphiqueCourbe());
        graphiques.add(creerGraphiqueDonut());

        panel.add(cards);
        panel.add(Box.createVerticalStrut(24));
        panel.add(graphiques);

        return panel;
    }

    // ── COURBE séances par mois ───────────────────────────
    private JPanel creerGraphiqueCourbe() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titre = new JLabel("Seances par mois");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(new Color(50, 50, 50));
        panel.add(titre, BorderLayout.NORTH);

        // Calculer données par mois
        int[] data = new int[7];
        String[] mois = {"Sep", "Oct", "Nov", "Dec", "Jan", "Fev", "Mar"};
        int[] numMois = {9, 10, 11, 12, 1, 2, 3};

            try {
                List<Cours> cours = coursService.listerTous();
                for (Cours c : cours) {
                    List<Seance> seances = seanceService.listerParCours(c.getId());
                    for (Seance s : seances) {
                        if (s.getDate() != null) {
                            int m = s.getDate().getMonthValue();
                            for (int i = 0; i < numMois.length; i++) {
                                if (numMois[i] == m) { data[i]++; break; }
                            }
                        }
                    }
                }
            } catch (Exception e) { /* silencieux */ }

            
        JPanel courbe = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int padL = 40, padR = 20, padT = 20, padB = 40;
                int graphW = w - padL - padR;
                int graphH = h - padT - padB;

                int max = 1;
                for (int d : data) if (d > max) max = d;

                // Grid
                g2.setColor(new Color(240, 240, 240));
                for (int i = 0; i <= 4; i++) {
                    int y = padT + (graphH * i / 4);
                    g2.drawLine(padL, y, w - padR, y);
                    g2.setColor(new Color(180, 180, 180));
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                    g2.drawString(String.valueOf(max - max * i / 4), 2, y + 4);
                    g2.setColor(new Color(240, 240, 240));
                }

                // Points
                int[] px = new int[7], py = new int[7];
                for (int i = 0; i < 7; i++) {
                    px[i] = padL + (i * graphW / 6);
                    py[i] = padT + graphH - (data[i] * graphH / max);
                }

                // Zone remplie
                int[] fillX = new int[9], fillY = new int[9];
                for (int i = 0; i < 7; i++) { fillX[i] = px[i]; fillY[i] = py[i]; }
                fillX[7] = px[6]; fillY[7] = padT + graphH;
                fillX[8] = px[0]; fillY[8] = padT + graphH;
                g2.setColor(new Color(0, 120, 215, 25));
                g2.fillPolygon(fillX, fillY, 9);

                // Ligne
                g2.setColor(new Color(0, 120, 215));
                g2.setStroke(new BasicStroke(2.5f,
                    BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                for (int i = 0; i < 6; i++) {
                    g2.drawLine(px[i], py[i], px[i+1], py[i+1]);
                }

                // Points + labels mois
                for (int i = 0; i < 7; i++) {
                    g2.setColor(Color.WHITE);
                    g2.fillOval(px[i] - 4, py[i] - 4, 8, 8);
                    g2.setColor(new Color(0, 120, 215));
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawOval(px[i] - 4, py[i] - 4, 8, 8);

                    g2.setColor(new Color(160, 160, 160));
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(mois[i],
                        px[i] - fm.stringWidth(mois[i]) / 2,
                        h - padB + 15);
                }
            }
        };
        courbe.setBackground(Color.WHITE);

        panel.add(courbe, BorderLayout.CENTER);
        return panel;
    }

    // ── DONUT statut séances ──────────────────────────────
    private JPanel creerGraphiqueDonut() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        JLabel titre = new JLabel("Statut des seances");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 13));
        titre.setForeground(new Color(50, 50, 50));
        panel.add(titre, BorderLayout.NORTH);

        // Calculer données
        int[] counts = new int[3]; // validees, attente, rejetees
        try {
            counts[0] = statsService.getNombreSeancesValidees();
            counts[1] = statsService.getNombreSeancesEnAttente();
            List<Cours> cours = coursService.listerTous();
            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    if ("REJETE".equals(s.getStatut())) counts[2]++;
                }
            }
        } catch (Exception e) { /* silencieux */ }

        final int[] finalCounts = counts;
        int total = counts[0] + counts[1] + counts[2];

        JPanel donut = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                int w = getWidth(), h = getHeight();
                int size = Math.min(w / 2, h - 40);
                int x = w / 4 - size / 2;
                int y = (h - size) / 2;

                Color[] couleurs = {
                    new Color(0, 180, 120),
                    new Color(255, 140, 0),
                    new Color(220, 50, 50)
                };
                String[] labels = {"Validees", "En attente", "Rejetees"};

                int tot = finalCounts[0] + finalCounts[1] + finalCounts[2];
                if (tot == 0) tot = 1;

                // Dessin donut
                int startAngle = 90;
                for (int i = 0; i < 3; i++) {
                    int arc = (int) (finalCounts[i] * 360.0 / tot);
                    g2.setColor(couleurs[i]);
                    g2.setStroke(new BasicStroke(20f,
                        BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER));
                    g2.drawArc(x + 10, y + 10, size - 20, size - 20,
                        startAngle, -arc);
                    startAngle -= arc;
                }

                // Centre texte
                int pct = tot > 0
                    ? (int) (finalCounts[0] * 100.0 / tot) : 0;
                g2.setColor(new Color(50, 50, 50));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 18));
                FontMetrics fm = g2.getFontMetrics();
                String txt = pct + "%";
                g2.drawString(txt,
                    x + size / 2 - fm.stringWidth(txt) / 2,
                    y + size / 2 + 6);

                // Légende
                int ly = y + 10;
                int lx = w / 2 + 10;
                for (int i = 0; i < 3; i++) {
                    g2.setColor(couleurs[i]);
                    g2.fillOval(lx, ly + i * 30, 10, 10);
                    g2.setColor(new Color(80, 80, 80));
                    g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    g2.drawString(labels[i], lx + 16, ly + i * 30 + 9);
                    g2.setColor(new Color(150, 150, 150));
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 11));
                    g2.drawString(String.valueOf(finalCounts[i]),
                        lx + 16, ly + i * 30 + 22);
                }
            }
        };
        donut.setBackground(Color.WHITE);

        panel.add(donut, BorderLayout.CENTER);
        return panel;
    }
}