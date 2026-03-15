package com.esitec.cahier.ui;

import com.esitec.cahier.service.AuthService;
import com.esitec.cahier.exception.AuthException;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.ui.chef.ChefDashboard;
import com.esitec.cahier.ui.enseignant.EnseignantDashboard;
import com.esitec.cahier.ui.responsable.ResponsableDashboard;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    private JTextField     champEmail;
    private JPasswordField champMotDePasse;
    private JButton        btnConnecter;
    private JLabel         lblMessage;
    private AuthService    authService;

    public LoginView() {
        this.authService = new AuthService();
        setTitle("ESITEC - Cahier de Texte");
        setSize(900, 540);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initialiserUI();
    }

    private void initialiserUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));

        // ── GAUCHE ──────────────────────────────────────
        JPanel gauche = new JPanel();
        gauche.setBackground(Color.WHITE);
        gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
        gauche.setBorder(BorderFactory.createEmptyBorder(50, 48, 50, 48));

        JLabel lblEsitec = new JLabel("ESITEC  ·  SUP DE CO");
        lblEsitec.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblEsitec.setForeground(new Color(0, 120, 215));
        lblEsitec.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitre = new JLabel("Bon retour !");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitre.setForeground(new Color(26, 26, 46));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Connectez-vous pour acceder a votre espace");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(160, 160, 160));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        champEmail      = new JTextField();
        champMotDePasse = new JPasswordField();
        styliserChamp(champEmail);
        styliserChamp(champMotDePasse);

        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMessage.setForeground(new Color(200, 50, 50));
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnConnecter = new JButton("Se connecter") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(0, 120, 215));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                FontMetrics fm = g2.getFontMetrics();
                String txt = "Se connecter";
                g2.drawString(txt,
                    (getWidth() - fm.stringWidth(txt)) / 2,
                    (getHeight() + fm.getAscent() - fm.getDescent()) / 2);
            }
        };
        btnConnecter.setPreferredSize(new Dimension(Integer.MAX_VALUE, 46));
        btnConnecter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        btnConnecter.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnConnecter.setBorderPainted(false);
        btnConnecter.setContentAreaFilled(false);
        btnConnecter.setFocusPainted(false);
        btnConnecter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConnecter.addActionListener(e -> connecter());
        champMotDePasse.addActionListener(e -> connecter());

        // Lien inscription CENTRE
        JPanel panelInscription = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        panelInscription.setBackground(Color.WHITE);
        panelInscription.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelInscription.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JLabel lblInscription = new JLabel(
            "<html>Pas encore de compte ? " +
            "<span style='color:#0078d7;font-weight:bold;'>S'inscrire</span></html>");
        lblInscription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInscription.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblInscription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new InscriptionView().setVisible(true);
            }
        });
        panelInscription.add(lblInscription);

        gauche.add(lblEsitec);
        gauche.add(Box.createVerticalStrut(8));
        gauche.add(lblTitre);
        gauche.add(Box.createVerticalStrut(6));
        gauche.add(lblSub);
        gauche.add(Box.createVerticalStrut(30));
        gauche.add(creerLabel("EMAIL"));
        gauche.add(Box.createVerticalStrut(6));
        gauche.add(champEmail);
        gauche.add(Box.createVerticalStrut(18));
        gauche.add(creerLabel("MOT DE PASSE"));
        gauche.add(Box.createVerticalStrut(6));
        gauche.add(champMotDePasse);
        gauche.add(Box.createVerticalStrut(6));
        gauche.add(lblMessage);
        gauche.add(Box.createVerticalStrut(20));
        gauche.add(btnConnecter);
        gauche.add(Box.createVerticalStrut(14));
        gauche.add(panelInscription);

        // ── DROITE ──────────────────────────────────────
        JPanel droite = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 15));
                g2.fillOval(getWidth() - 160, -80, 220, 220);
                g2.fillOval(-60, getHeight() - 180, 260, 260);
            }
        };
        droite.setBackground(new Color(0, 120, 215));
        droite.setLayout(new BoxLayout(droite, BoxLayout.Y_AXIS));
        droite.setBorder(BorderFactory.createEmptyBorder(48, 40, 48, 40));

        // Icone cahier dessinee
        JPanel iconeCahier = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

                // Fond carré arrondi
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, 80, 80, 20, 20);

                // Corps du cahier
                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
                // Couverture
                g2.fillRoundRect(18, 12, 44, 56, 4, 4);

                // Spirale gauche
                g2.setColor(new Color(0, 120, 215));
                g2.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND,
                    BasicStroke.JOIN_ROUND));
                for (int y = 18; y <= 58; y += 10) {
                    g2.drawOval(14, y, 8, 6);
                }

                // Lignes intérieures
                g2.setColor(new Color(0, 100, 190));
                g2.setStroke(new BasicStroke(1.2f));
                g2.drawLine(26, 28, 54, 28);
                g2.drawLine(26, 36, 54, 36);
                g2.drawLine(26, 44, 54, 44);
                g2.drawLine(26, 52, 46, 52);
            }
        };
        iconeCahier.setPreferredSize(new Dimension(80, 80));
        iconeCahier.setMaximumSize(new Dimension(80, 80));
        iconeCahier.setOpaque(false);
        iconeCahier.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblTitreDroite = new JLabel("Cahier de Texte");
        lblTitreDroite.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitreDroite.setForeground(Color.WHITE);
        lblTitreDroite.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblNumerique = new JLabel("Numerique");
        lblNumerique.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNumerique.setForeground(new Color(255, 255, 255, 180));
        lblNumerique.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel sep = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(new Color(255, 255, 255, 80));
                g.fillRect(0, 0, getWidth(), 2);
            }
        };
        sep.setMaximumSize(new Dimension(40, 2));
        sep.setOpaque(false);
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSupdeco = new JLabel("SUP DE CO");
        lblSupdeco.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblSupdeco.setForeground(new Color(255, 255, 255, 220));
        lblSupdeco.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblDakar = new JLabel("Dakar, Senegal");
        lblDakar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDakar.setForeground(new Color(255, 255, 255, 150));
        lblDakar.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel badges = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0));
        badges.setOpaque(false);
        badges.setAlignmentX(Component.CENTER_ALIGNMENT);
        badges.add(creerBadge("ISO 9001"));
        badges.add(creerBadge("CAMES"));

        droite.add(Box.createVerticalGlue());
        droite.add(iconeCahier);
        droite.add(Box.createVerticalStrut(20));
        droite.add(lblTitreDroite);
        droite.add(Box.createVerticalStrut(4));
        droite.add(lblNumerique);
        droite.add(Box.createVerticalStrut(20));
        droite.add(sep);
        droite.add(Box.createVerticalStrut(20));
        droite.add(lblSupdeco);
        droite.add(Box.createVerticalStrut(4));
        droite.add(lblDakar);
        droite.add(Box.createVerticalStrut(16));
        droite.add(badges);
        droite.add(Box.createVerticalGlue());

        root.add(gauche);
        root.add(droite);
        add(root);
    }

    private JPanel creerBadge(String texte) {
        JPanel badge = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(255, 255, 255, 40));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            }
        };
        badge.setOpaque(false);
        badge.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 0));
        badge.setBorder(BorderFactory.createEmptyBorder(4, 12, 4, 12));
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lbl.setForeground(Color.WHITE);
        badge.add(lbl);
        return badge;
    }

    private void styliserChamp(JComponent champ) {
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        champ.setBackground(Color.WHITE);
        champ.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 120, 215)),
            BorderFactory.createEmptyBorder(8, 0, 8, 0)
        ));
        champ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champ.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JLabel creerLabel(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(85, 85, 85));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void connecter() {
        String email = champEmail.getText().trim();
        String mdp   = new String(champMotDePasse.getPassword());
        btnConnecter.setEnabled(false);
        try {
            Utilisateur utilisateur = authService.connecter(email, mdp);
            switch (utilisateur.getRole()) {
                case "CHEF_DEPARTEMENT":
                    new ChefDashboard().setVisible(true);
                    break;
                case "ENSEIGNANT":
                    new EnseignantDashboard().setVisible(true);
                    break;
                case "RESPONSABLE_CLASSE":
                    new ResponsableDashboard().setVisible(true);
                    break;
            }
            dispose();
        } catch (AuthException ex) {
            lblMessage.setText("  " + ex.getMessage());
            btnConnecter.setEnabled(true);
            champMotDePasse.setText("");
        }
    }
}