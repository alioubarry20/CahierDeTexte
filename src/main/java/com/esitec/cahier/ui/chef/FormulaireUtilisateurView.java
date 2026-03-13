package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.ChefDepartement;
import com.esitec.cahier.model.Classe;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.ResponsableClasse;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.service.ClasseService;
import com.esitec.cahier.service.UtilisateurService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FormulaireUtilisateurView extends BaseView {

    private JTextField champNom;
    private JTextField champPrenom;
    private JTextField champEmail;
    private JPasswordField champMotDePasse;
    private JComboBox<String> comboRole;
    private JTextField champExtra;
    private JLabel lblExtra;
    private JComboBox<Classe> comboClasse;
    private JLabel lblClasse;

    private UtilisateurService service = new UtilisateurService();
    private ClasseService classeService = new ClasseService();
    private GestionUtilisateursView parent;

    public FormulaireUtilisateurView(GestionUtilisateursView parent) {
        super("Ajouter un utilisateur");
        this.parent = parent;
        initialiserUI();
    }

    private void initialiserUI() {
        setSize(450, 580);
        setLayout(new BorderLayout());
        add(creerHeader("Nouvel utilisateur"), BorderLayout.NORTH);

        JPanel formulaire = new JPanel();
        formulaire.setLayout(new BoxLayout(formulaire, BoxLayout.Y_AXIS));
        formulaire.setBackground(Color.WHITE);
        formulaire.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        champNom        = new JTextField();
        champPrenom     = new JTextField();
        champEmail      = new JTextField();
        champMotDePasse = new JPasswordField();
        comboRole       = new JComboBox<>(new String[]{
            "ENSEIGNANT", "RESPONSABLE_CLASSE", "CHEF_DEPARTEMENT"
        });
        champExtra = new JTextField();
        lblExtra   = new JLabel("Specialite");

        // Combo classe pour responsable
        comboClasse = new JComboBox<>();
        lblClasse   = new JLabel("Classe assignee");
        lblClasse.setVisible(false);
        comboClasse.setVisible(false);

        try {
            List<Classe> classes = classeService.listerTous();
            for (Classe c : classes) comboClasse.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur chargement classes : " + e.getMessage());
        }

        // Mise à jour selon le rôle
        comboRole.addActionListener(e -> {
            String role = (String) comboRole.getSelectedItem();
            switch (role) {
                case "CHEF_DEPARTEMENT":
                    lblExtra.setText("Departement");
                    champExtra.setVisible(true);
                    lblExtra.setVisible(true);
                    lblClasse.setVisible(false);
                    comboClasse.setVisible(false);
                    break;
                case "ENSEIGNANT":
                    lblExtra.setText("Specialite");
                    champExtra.setVisible(true);
                    lblExtra.setVisible(true);
                    lblClasse.setVisible(false);
                    comboClasse.setVisible(false);
                    break;
                case "RESPONSABLE_CLASSE":
                    lblExtra.setVisible(false);
                    champExtra.setVisible(false);
                    lblClasse.setVisible(true);
                    comboClasse.setVisible(true);
                    break;
            }
            formulaire.revalidate();
            formulaire.repaint();
        });

        ajouterChamp(formulaire, "Nom", champNom);
        ajouterChamp(formulaire, "Prenom", champPrenom);
        ajouterChamp(formulaire, "Email", champEmail);
        ajouterChamp(formulaire, "Mot de passe", champMotDePasse);
        ajouterChamp(formulaire, "Role", comboRole);

        lblExtra.setFont(new Font("Arial", Font.BOLD, 12));
        lblExtra.setAlignmentX(Component.LEFT_ALIGNMENT);
        champExtra.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        champExtra.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblClasse.setFont(new Font("Arial", Font.BOLD, 12));
        lblClasse.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboClasse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboClasse.setAlignmentX(Component.LEFT_ALIGNMENT);

        formulaire.add(lblExtra);
        formulaire.add(Box.createVerticalStrut(4));
        formulaire.add(champExtra);
        formulaire.add(Box.createVerticalStrut(12));
        formulaire.add(lblClasse);
        formulaire.add(Box.createVerticalStrut(4));
        formulaire.add(comboClasse);
        formulaire.add(Box.createVerticalStrut(12));

        JButton btnSauvegarder = creerBouton("Sauvegarder", COULEUR_SECONDAIRE);
        btnSauvegarder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSauvegarder.addActionListener(e -> sauvegarder());

        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(btnSauvegarder);

        add(new JScrollPane(formulaire), BorderLayout.CENTER);
        setVisible(true);
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
                    chef.setDepartement(champExtra.getText().trim());
                    u = chef;
                    break;
                case "ENSEIGNANT":
                    Enseignant enseignant = new Enseignant();
                    enseignant.setSpecialite(champExtra.getText().trim());
                    u = enseignant;
                    break;
                default:
                    ResponsableClasse resp = new ResponsableClasse();
                    resp.setClasse((Classe) comboClasse.getSelectedItem());
                    u = resp;
                    break;
            }

            u.setNom(champNom.getText().trim());
            u.setPrenom(champPrenom.getText().trim());
            u.setEmail(champEmail.getText().trim());
            u.setMotDePasse(new String(champMotDePasse.getPassword()));
            u.setRole(role);
            u.setStatut("ACTIF");

            service.ajouter(u);
            afficherSucces("Utilisateur ajoute !");
            parent.rafraichir();
            dispose();

        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}