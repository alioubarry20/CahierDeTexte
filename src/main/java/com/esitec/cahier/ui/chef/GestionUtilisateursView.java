package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Classe;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.ResponsableClasse;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.service.ClasseService;
import com.esitec.cahier.service.UtilisateurService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionUtilisateursView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private UtilisateurService service      = new UtilisateurService();
    private ClasseService      classeService = new ClasseService();

    public GestionUtilisateursView() {
        super("Gestion des utilisateurs");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 248));

        String[] colonnes = {"ID", "Nom", "Prenom", "Email", "Role", "Statut"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableau.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        boutons.setBackground(new Color(240, 242, 248));

        JButton btnAjouter   = creerBouton("+ Ajouter",      COULEUR_SECONDAIRE);
        JButton btnModifier  = creerBouton("Modifier",        new Color(255, 140, 0));
        JButton btnSupprimer = creerBouton("Supprimer",       COULEUR_DANGER);
        JButton btnValider   = creerBouton("Valider compte",  COULEUR_SUCCES);

        btnAjouter.addActionListener(e  -> ouvrirFormulaireAjout());
        btnModifier.addActionListener(e -> modifierUtilisateur());
        btnSupprimer.addActionListener(e -> supprimerUtilisateur());
        btnValider.addActionListener(e  -> validerCompte());

        boutons.add(btnAjouter);
        boutons.add(btnModifier);
        boutons.add(btnSupprimer);
        boutons.add(btnValider);
        panel.add(boutons, BorderLayout.SOUTH);

        chargerUtilisateurs();
        return panel;
    }

    private void chargerUtilisateurs() {
        try {
            modeleTableau.setRowCount(0);
            List<Utilisateur> liste = service.listerTous();
            for (Utilisateur u : liste) {
                modeleTableau.addRow(new Object[]{
                    u.getId(), u.getNom(), u.getPrenom(),
                    u.getEmail(), u.getRole(), u.getStatut()
                });
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void modifierUtilisateur() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez un utilisateur !"); return; }

        int    id    = (int)    modeleTableau.getValueAt(ligne, 0);
        String role  = (String) modeleTableau.getValueAt(ligne, 4);
        String nom   = (String) modeleTableau.getValueAt(ligne, 1);
        String prenom= (String) modeleTableau.getValueAt(ligne, 2);
        String email = (String) modeleTableau.getValueAt(ligne, 3);

        JDialog dialog = new JDialog((JFrame) null, "Modifier utilisateur", true);
        dialog.setSize(420, role.equals("RESPONSABLE_CLASSE") ? 380 : 320);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel form = new JPanel();
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBackground(Color.WHITE);
        form.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        JTextField champNom    = new JTextField(nom);
        JTextField champPrenom = new JTextField(prenom);
        JTextField champEmail  = new JTextField(email);

        styliserChampDialog(champNom);
        styliserChampDialog(champPrenom);
        styliserChampDialog(champEmail);

        form.add(creerLabelDialog("Nom"));
        form.add(Box.createVerticalStrut(4));
        form.add(champNom);
        form.add(Box.createVerticalStrut(12));
        form.add(creerLabelDialog("Prenom"));
        form.add(Box.createVerticalStrut(4));
        form.add(champPrenom);
        form.add(Box.createVerticalStrut(12));
        form.add(creerLabelDialog("Email"));
        form.add(Box.createVerticalStrut(4));
        form.add(champEmail);
        form.add(Box.createVerticalStrut(12));

        // Si responsable → combo classe
        JComboBox<Classe> comboClasse = new JComboBox<>();
        if ("RESPONSABLE_CLASSE".equals(role)) {
            try {
                List<Classe> classes = classeService.listerTous();
                comboClasse.addItem(null);
                for (Classe c : classes) comboClasse.addItem(c);
            } catch (Exception e) { /* silencieux */ }
            comboClasse.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            comboClasse.setAlignmentX(Component.LEFT_ALIGNMENT);
            form.add(creerLabelDialog("Classe assignee"));
            form.add(Box.createVerticalStrut(4));
            form.add(comboClasse);
            form.add(Box.createVerticalStrut(12));
        }

        // Bouton sauvegarder
        JButton btnSauvegarder = creerBouton("Sauvegarder", COULEUR_SUCCES);
        btnSauvegarder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSauvegarder.addActionListener(e -> {
            try {
                // Charger l'utilisateur existant
                List<Utilisateur> tous = service.listerTous();
                Utilisateur u = null;
                for (Utilisateur ut : tous) {
                    if (ut.getId() == id) { u = ut; break; }
                }
                if (u == null) { afficherErreur("Utilisateur introuvable !"); return; }

                u.setNom(champNom.getText().trim());
                u.setPrenom(champPrenom.getText().trim());
                u.setEmail(champEmail.getText().trim());

                // Si responsable → assigner classe
                if ("RESPONSABLE_CLASSE".equals(role) && u instanceof ResponsableClasse) {
                    Classe classeChoisie = (Classe) comboClasse.getSelectedItem();
                    ((ResponsableClasse) u).setClasse(classeChoisie);

                    // Envoyer mail si classe assignée
                    if (classeChoisie != null) {
                        try {
                            com.esitec.cahier.util.MailService.getInstance()
                                .mailClasseAssignee(
                                    u.getEmail(),
                                    u.getPrenom() + " " + u.getNom(),
                                    classeChoisie.getNom()
                                );
                        } catch (Exception ex) { /* silencieux */ }
                    }
                }

                service.modifier(u);
                afficherSucces("Utilisateur modifie !");
                chargerUtilisateurs();
                dialog.dispose();

            } catch (Exception ex) {
                afficherErreur("Erreur : " + ex.getMessage());
            }
        });

        form.add(btnSauvegarder);
        dialog.add(form, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void styliserChampDialog(JTextField champ) {
        champ.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        champ.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        champ.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        champ.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JLabel creerLabelDialog(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lbl.setForeground(new Color(85, 85, 85));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void ouvrirFormulaireAjout() {
        new FormulaireUtilisateurView(this).setVisible(true);
    }

    private void supprimerUtilisateur() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez un utilisateur !"); return; }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer cet utilisateur ?", "Confirmation",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.supprimer(id);
                afficherSucces("Utilisateur supprime !");
                chargerUtilisateurs();
            } catch (Exception e) {
                afficherErreur("Erreur : " + e.getMessage());
            }
        }
    }

    private void validerCompte() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez un utilisateur !"); return; }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        try {
            service.validerCompte(id);
            afficherSucces("Compte valide !");
            chargerUtilisateurs();
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    public void rafraichir() {
        chargerUtilisateurs();
    }
}