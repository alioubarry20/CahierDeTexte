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
        setSize(860, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initialiserUI();
    }

    private void initialiserUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));

        // ── GAUCHE ──────────────────────────────────────
        JPanel gauche = new JPanel();
        gauche.setBackground(new Color(245, 245, 245));
        gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
        gauche.setBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60));

        JLabel lblTitre = new JLabel("Login");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitre.setForeground(new Color(20, 20, 20));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        champEmail = new JTextField();
        champMotDePasse = new JPasswordField();
        styliserChamp(champEmail);
        styliserChamp(champMotDePasse);

        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblMessage.setForeground(new Color(200, 50, 50));
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnConnecter = new JButton("Login");
        btnConnecter.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnConnecter.setBackground(new Color(245, 245, 245));
        btnConnecter.setForeground(new Color(0, 120, 215));
        btnConnecter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        btnConnecter.setFocusPainted(false);
        btnConnecter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConnecter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        btnConnecter.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnConnecter.addActionListener(e -> connecter());
        champMotDePasse.addActionListener(e -> connecter());

        JLabel lblInscription = new JLabel(
            "<html>Pas encore de compte ? " +
            "<span style='color:#0078d7;font-weight:bold;'>S'inscrire</span></html>");
        lblInscription.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblInscription.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblInscription.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblInscription.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                new InscriptionView().setVisible(true);
            }
        });

        gauche.add(lblTitre);
        gauche.add(Box.createVerticalStrut(28));
        gauche.add(creerLabel("Email"));
        gauche.add(Box.createVerticalStrut(5));
        gauche.add(champEmail);
        gauche.add(Box.createVerticalStrut(16));
        gauche.add(creerLabel("Password"));
        gauche.add(Box.createVerticalStrut(5));
        gauche.add(champMotDePasse);
        gauche.add(Box.createVerticalStrut(6));
        gauche.add(lblMessage);
        gauche.add(Box.createVerticalStrut(20));
        gauche.add(btnConnecter);
        gauche.add(Box.createVerticalStrut(14));
        gauche.add(lblInscription);

        // ── DROITE ──────────────────────────────────────
        JPanel droite = new JPanel();
        droite.setBackground(new Color(0, 120, 215));
        droite.setLayout(new BoxLayout(droite, BoxLayout.Y_AXIS));
        droite.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        JLabel lblWelcome = new JLabel(
            "<html><center>Welcome to the<br>application</center></html>");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("login to continue");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSub.setForeground(new Color(200, 230, 255));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 60));
        sep.setMaximumSize(new Dimension(180, 1));
        sep.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrand = new JLabel("SUP DE CO");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblBrand.setForeground(new Color(176, 212, 255));
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrandSub = new JLabel("Cahier de Texte Numerique");
        lblBrandSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblBrandSub.setForeground(new Color(200, 230, 255));
        lblBrandSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblCertif = new JLabel("Certifie ISO 9001 · Accredite CAMES");
        lblCertif.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblCertif.setForeground(new Color(160, 200, 240));
        lblCertif.setAlignmentX(Component.CENTER_ALIGNMENT);

        droite.add(Box.createVerticalGlue());
        droite.add(lblWelcome);
        droite.add(Box.createVerticalStrut(8));
        droite.add(lblSub);
        droite.add(Box.createVerticalStrut(18));
        droite.add(sep);
        droite.add(Box.createVerticalStrut(18));
        droite.add(lblBrand);
        droite.add(Box.createVerticalStrut(5));
        droite.add(lblBrandSub);
        droite.add(Box.createVerticalStrut(8));
        droite.add(lblCertif);
        droite.add(Box.createVerticalGlue());

        root.add(gauche);
        root.add(droite);
        add(root);
    }

    private void styliserChamp(JComponent champ) {
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        champ.setBackground(new Color(245, 245, 245));
        champ.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 120, 215)),
            BorderFactory.createEmptyBorder(6, 0, 6, 0)
        ));
        champ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        champ.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JLabel creerLabel(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(85, 85, 85));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void connecter() {
        String email = champEmail.getText().trim();
        String mdp   = new String(champMotDePasse.getPassword());

        btnConnecter.setEnabled(false);
        btnConnecter.setText("Connexion...");

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
            btnConnecter.setText("Login");
            champMotDePasse.setText("");
        }
    }
}