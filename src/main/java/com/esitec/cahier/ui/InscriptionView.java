package com.esitec.cahier.ui;

import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.ResponsableClasse;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.service.UtilisateurService;
import javax.swing.*;
import java.awt.*;

public class InscriptionView extends JFrame {

    private JTextField     champNom;
    private JTextField     champPrenom;
    private JTextField     champEmail;
    private JPasswordField champMotDePasse;
    private JComboBox<String> comboRole;
    private JLabel         lblErreur;

    private UtilisateurService service = new UtilisateurService();

    public InscriptionView() {
        setTitle("ESITEC - Creer un compte");
        setSize(500, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initialiserUI();
    }

    private void initialiserUI() {
        JPanel root = new JPanel();
        root.setBackground(new Color(248, 249, 252));
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        root.setBorder(BorderFactory.createEmptyBorder(35, 50, 35, 50));

        // Titre
        JLabel lblTitre = new JLabel("Creer un compte");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(new Color(20, 20, 50));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblSub = new JLabel("Votre compte sera active par le chef de departement");
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(150, 150, 180));
        lblSub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Champs
        champNom        = new JTextField();
        champPrenom     = new JTextField();
        champEmail      = new JTextField();
        champMotDePasse = new JPasswordField();
        comboRole       = new JComboBox<>(new String[]{
            "ENSEIGNANT", "RESPONSABLE_CLASSE"
        });

        // Erreur
        lblErreur = new JLabel(" ");
        lblErreur.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblErreur.setForeground(new Color(200, 50, 50));
        lblErreur.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Bouton
        JButton btnCreer = new JButton("CREER MON COMPTE");
        btnCreer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnCreer.setBackground(new Color(0, 90, 180));
        btnCreer.setForeground(Color.WHITE);
        btnCreer.setFocusPainted(false);
        btnCreer.setBorderPainted(false);
        btnCreer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCreer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btnCreer.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCreer.addActionListener(e -> inscrire());

        // Assemblage
        root.add(lblTitre);
        root.add(Box.createVerticalStrut(4));
        root.add(lblSub);
        root.add(Box.createVerticalStrut(22));
        root.add(creerLabel("Nom"));
        root.add(Box.createVerticalStrut(5));
        root.add(styliserChamp(champNom));
        root.add(Box.createVerticalStrut(14));
        root.add(creerLabel("Prenom"));
        root.add(Box.createVerticalStrut(5));
        root.add(styliserChamp(champPrenom));
        root.add(Box.createVerticalStrut(14));
        root.add(creerLabel("Email"));
        root.add(Box.createVerticalStrut(5));
        root.add(styliserChamp(champEmail));
        root.add(Box.createVerticalStrut(14));
        root.add(creerLabel("Mot de passe"));
        root.add(Box.createVerticalStrut(5));
        root.add(styliserChamp(champMotDePasse));
        root.add(Box.createVerticalStrut(14));
        root.add(creerLabel("Role"));
        root.add(Box.createVerticalStrut(5));
        comboRole.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        comboRole.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboRole.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        root.add(comboRole);
        root.add(Box.createVerticalStrut(10));
        root.add(lblErreur);
        root.add(Box.createVerticalStrut(16));
        root.add(btnCreer);

        add(new JScrollPane(root));
    }

    private JLabel creerLabel(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lbl.setForeground(new Color(60, 60, 90));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JComponent styliserChamp(JComponent champ) {
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        champ.setBackground(new Color(248, 249, 252));
        champ.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(0, 90, 180)),
            BorderFactory.createEmptyBorder(6, 0, 6, 0)
        ));
        champ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        champ.setAlignmentX(Component.LEFT_ALIGNMENT);
        return champ;
    }

    private void inscrire() {
        String nom    = champNom.getText().trim();
        String prenom = champPrenom.getText().trim();
        String email  = champEmail.getText().trim();
        String mdp    = new String(champMotDePasse.getPassword());
        String role   = (String) comboRole.getSelectedItem();

        // Validation
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || mdp.isEmpty()) {
            lblErreur.setText("Tous les champs sont obligatoires !");
            return;
        }
        if (!email.contains("@")) {
            lblErreur.setText("Email invalide !");
            return;
        }
        if (mdp.length() < 6) {
            lblErreur.setText("Mot de passe trop court (6 caracteres min) !");
            return;
        }

        try {
            Utilisateur u;
            if ("ENSEIGNANT".equals(role)) {
                u = new Enseignant();
            } else {
                u = new ResponsableClasse();
            }

            u.setNom(nom);
            u.setPrenom(prenom);
            u.setEmail(email);
            u.setMotDePasse(mdp);
            u.setRole(role);
            u.setStatut("EN_ATTENTE");

            service.ajouter(u);

            JOptionPane.showMessageDialog(this,
                "Compte cree ! En attente de validation par le chef de departement.",
                "Succes", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (Exception e) {
            lblErreur.setText("Erreur : " + e.getMessage());
        }
    }
}