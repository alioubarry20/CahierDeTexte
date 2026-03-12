package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.ChefDepartement;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.ResponsableClasse;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.service.UtilisateurService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class FormulaireUtilisateurView extends BaseView {

    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JComboBox<String> comboRole;
    private JTextField champExtra; // département ou spécialité
    private JLabel lblExtra;

    private UtilisateurService service = new UtilisateurService();
    private GestionUtilisateursView parent;

    public FormulaireUtilisateurView(GestionUtilisateursView parent) {
        super("Ajouter un utilisateur");
        this.parent = parent;
        initialiserUI();
    }

    private void initialiserUI() {
        setSize(420, 500);
        setLayout(new BorderLayout());
        add(creerHeader("➕ Nouvel utilisateur"), BorderLayout.NORTH);

        JPanel formulaire = new JPanel();
        formulaire.setLayout(new BoxLayout(formulaire, BoxLayout.Y_AXIS));
        formulaire.setBackground(Color.WHITE);
        formulaire.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Champs
        champNom = new JTextField();
        champPrenom = new JTextField();
        champEmail = new JTextField();
        champMotDePasse = new JPasswordField();
        comboRole = new JComboBox<>(new String[]{
            "ENSEIGNANT", "RESPONSABLE_CLASSE", "CHEF_DEPARTEMENT"
        });
        champExtra = new JTextField();
        lblExtra = new JLabel("Spécialité");

        // Mise à jour du label selon le rôle
        comboRole.addActionListener(e -> {
            String role = (String) comboRole.getSelectedItem();
            if ("CHEF_DEPARTEMENT".equals(role)) {
                lblExtra.setText("Département");
            } else if ("ENSEIGNANT".equals(role)) {
                lblExtra.setText("Spécialité");
            } else {
                lblExtra.setText("Info complémentaire");
            }
        });

        ajouterChamp(formulaire, "Nom", champNom);
        ajouterChamp(formulaire, "Prénom", champPrenom);
        ajouterChamp(formulaire, "Email", champEmail);
        ajouterChamp(formulaire, "Mot de passe", champMotDePasse);
        ajouterChamp(formulaire, "Rôle", comboRole);
        ajouterChamp(formulaire, lblExtra.getText(), champExtra);

        JButton btnSauvegarder = creerBouton("💾 Sauvegarder", COULEUR_SECONDAIRE);
        btnSauvegarder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSauvegarder.addActionListener(e -> sauvegarder());

        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(btnSauvegarder);

        add(new JScrollPane(formulaire), BorderLayout.CENTER);
    }

    private void ajouterChamp(JPanel panel, String label, JComponent champ) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        champ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        champ.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));
        panel.add(champ);
        panel.add(Box.createVerticalStrut(12));
    }

    private void sauvegarder() {
        try {
            String role = (String) comboRole.getSelectedItem();
            Utilisateur u;

            switch (role) {
                case "CHEF_DEPARTEMENT":
                    ChefDepartement chef = new ChefDepartement();
                    chef.setDepartement(champExtra.getText());
                    u = chef;
                    break;
                case "ENSEIGNANT":
                    Enseignant enseignant = new Enseignant();
                    enseignant.setSpecialite(champExtra.getText());
                    u = enseignant;
                    break;
                default:
                    u = new ResponsableClasse();
                    break;
            }

            u.setNom(champNom.getText().trim());
            u.setPrenom(champPrenom.getText().trim());
            u.setEmail(champEmail.getText().trim());
            u.setMotDePasse(new String(champMotDePasse.getPassword()));
            u.setRole(role);
            u.setStatut("EN_ATTENTE");

            service.ajouter(u);
            afficherSucces("Utilisateur ajouté !");
            parent.rafraichir();
            dispose();

        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}