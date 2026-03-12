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
        setSize(860, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initialiserUI();
    }

    private void initialiserUI() {
        JPanel root = new JPanel(new GridLayout(1, 2));

        // ── GAUCHE : formulaire ──────────────────────────
        JPanel gauche = new JPanel();
        gauche.setBackground(new Color(245, 245, 245));
        gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));
        gauche.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel lblTitre = new JLabel("Login");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitre.setForeground(new Color(30, 30, 30));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        champEmail = creerChampTexte("Email");
        champMotDePasse = new JPasswordField();
        styliserChamp(champMotDePasse);

        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMessage.setForeground(new Color(200, 50, 50));
        lblMessage.setAlignmentX(Component.LEFT_ALIGNMENT);

        btnConnecter = new JButton("Login");
        btnConnecter.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnConnecter.setBackground(new Color(245, 245, 245));
        btnConnecter.setForeground(new Color(0, 120, 215));
        btnConnecter.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 120, 215), 2),
            BorderFactory.createEmptyBorder(10, 0, 10, 0)
        ));
        btnConnecter.setFocusPainted(false);
        btnConnecter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConnecter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnConnecter.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnConnecter.addActionListener(e -> connecter());
        champMotDePasse.addActionListener(e -> connecter());

        gauche.add(lblTitre);
        gauche.add(Box.createVerticalStrut(30));
        gauche.add(creerLabel("Email"));
        gauche.add(Box.createVerticalStrut(4));
        gauche.add(champEmail);
        gauche.add(Box.createVerticalStrut(12));
        gauche.add(creerLabel("Password"));
        gauche.add(Box.createVerticalStrut(4));
        gauche.add(champMotDePasse);
        gauche.add(Box.createVerticalStrut(8));
        gauche.add(lblMessage);
        gauche.add(Box.createVerticalStrut(20));
        gauche.add(btnConnecter);

        // ── DROITE : branding ────────────────────────────
        JPanel droite = new JPanel();
        droite.setBackground(new Color(0, 120, 215));
        droite.setLayout(new BoxLayout(droite, BoxLayout.Y_AXIS));
        droite.setBorder(BorderFactory.createEmptyBorder(60, 40, 60, 40));

        JLabel lblWelcome = new JLabel("<html><center>Welcome to the<br>application</center></html>");
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel("login to continue");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSub.setForeground(new Color(200, 230, 255));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrand = new JLabel("ESITEC");
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBrand.setForeground(new Color(176, 212, 255));
        lblBrand.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblBrandSub = new JLabel("Cahier de Texte Numerique");
        lblBrandSub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBrandSub.setForeground(new Color(200, 230, 255));
        lblBrandSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        droite.add(Box.createVerticalGlue());
        droite.add(lblWelcome);
        droite.add(Box.createVerticalStrut(10));
        droite.add(lblSub);
        droite.add(Box.createVerticalStrut(30));
        droite.add(lblBrand);
        droite.add(Box.createVerticalStrut(4));
        droite.add(lblBrandSub);
        droite.add(Box.createVerticalGlue());

        root.add(gauche);
        root.add(droite);
        add(root);
    }

    private JTextField creerChampTexte(String placeholder) {
        JTextField champ = new JTextField();
        styliserChamp(champ);
        return champ;
    }

    private void styliserChamp(JComponent champ) {
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 15));
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
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
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