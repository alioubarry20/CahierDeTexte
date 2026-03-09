package com.esitec.cahier.ui;

import com.esitec.cahier.service.AuthService;
import com.esitec.cahier.exception.AuthException;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.ui.chef.ChefDashboard;
import com.esitec.cahier.ui.enseignant.EnseignantDashboard;
import com.esitec.cahier.ui.responsable.ResponsableDashboard;

import javax.swing.*;
import java.awt.*;

/**
 * Fenêtre de connexion — première page de l'application.
 */
public class LoginView extends JFrame {

    private JTextField     champEmail;
    private JPasswordField champMotDePasse;
    private JButton        btnConnecter;
    private JLabel         lblMessage;
    private AuthService    authService;

    public LoginView() {
        this.authService = new AuthService();
        initialiserUI();
    }

    private void initialiserUI() {
        setTitle("CahierDeTexte ESITEC — Connexion");
        setSize(450, 520);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // ── Panneau principal ──────────────────────────────
        JPanel panneauPrincipal = new JPanel(new BorderLayout());
        panneauPrincipal.setBackground(Color.WHITE);

        // ── En-tête bleu ───────────────────────────────────
        JPanel header = new JPanel();
        header.setBackground(new Color(33, 97, 140));
        header.setPreferredSize(new Dimension(450, 140));
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(25, 20, 25, 20));

        JLabel lblAppName = new JLabel("📚 Cahier de Texte");
        lblAppName.setFont(new Font("Arial", Font.BOLD, 24));
        lblAppName.setForeground(Color.WHITE);
        lblAppName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblEsitec = new JLabel("ESITEC — SUP DE CO DAKAR");
        lblEsitec.setFont(new Font("Arial", Font.PLAIN, 13));
        lblEsitec.setForeground(new Color(200, 220, 240));
        lblEsitec.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(lblAppName);
        header.add(Box.createVerticalStrut(8));
        header.add(lblEsitec);

        // ── Formulaire ─────────────────────────────────────
        JPanel formulaire = new JPanel();
        formulaire.setLayout(new BoxLayout(formulaire, BoxLayout.Y_AXIS));
        formulaire.setBackground(Color.WHITE);
        formulaire.setBorder(BorderFactory.createEmptyBorder(35, 50, 30, 50));

        // Email
        JLabel lblEmail = new JLabel("Adresse email");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 13));
        lblEmail.setAlignmentX(Component.LEFT_ALIGNMENT);

        champEmail = new JTextField();
        champEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        champEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Mot de passe
        JLabel lblMdp = new JLabel("Mot de passe");
        lblMdp.setFont(new Font("Arial", Font.BOLD, 13));
        lblMdp.setAlignmentX(Component.LEFT_ALIGNMENT);

        champMotDePasse = new JPasswordField();
        champMotDePasse.setFont(new Font("Arial", Font.PLAIN, 14));
        champMotDePasse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        champMotDePasse.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Message d'erreur (invisible au départ)
        lblMessage = new JLabel(" ");
        lblMessage.setFont(new Font("Arial", Font.ITALIC, 12));
        lblMessage.setForeground(new Color(192, 57, 43));
        lblMessage.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bouton connexion
        btnConnecter = new JButton("Se connecter");
        btnConnecter.setFont(new Font("Arial", Font.BOLD, 15));
        btnConnecter.setBackground(new Color(33, 97, 140));
        btnConnecter.setForeground(Color.WHITE);
        btnConnecter.setFocusPainted(false);
        btnConnecter.setBorderPainted(false);
        btnConnecter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnConnecter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnConnecter.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnConnecter.addActionListener(e -> connecter());

        // Appuyer sur Entrée = connexion
        champMotDePasse.addActionListener(e -> connecter());

        // Assemblage formulaire
        formulaire.add(lblEmail);
        formulaire.add(Box.createVerticalStrut(6));
        formulaire.add(champEmail);
        formulaire.add(Box.createVerticalStrut(18));
        formulaire.add(lblMdp);
        formulaire.add(Box.createVerticalStrut(6));
        formulaire.add(champMotDePasse);
        formulaire.add(Box.createVerticalStrut(10));
        formulaire.add(lblMessage);
        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(btnConnecter);

        panneauPrincipal.add(header, BorderLayout.NORTH);
        panneauPrincipal.add(formulaire, BorderLayout.CENTER);

        add(panneauPrincipal);
    }

    /**
     * Logique de connexion appelée quand on clique sur le bouton.
     */
    private void connecter() {
        String email = champEmail.getText().trim();
        String mdp   = new String(champMotDePasse.getPassword());

        // Désactiver le bouton pendant le traitement
        btnConnecter.setEnabled(false);
        btnConnecter.setText("Connexion...");

        try {
            Utilisateur utilisateur = authService.connecter(email, mdp);

            // Rediriger selon le rôle
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

            dispose(); // Ferme la fenêtre de login

        } catch (AuthException ex) {
            lblMessage.setText("❌ " + ex.getMessage());
            btnConnecter.setEnabled(true);
            btnConnecter.setText("Se connecter");
            champMotDePasse.setText("");
        }
    }
}