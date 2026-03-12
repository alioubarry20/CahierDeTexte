package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.service.UtilisateurService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionUtilisateursView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private UtilisateurService service = new UtilisateurService();

    public GestionUtilisateursView() {
        super("Gestion des utilisateurs");
    }

    // Appelé depuis ChefDashboard pour afficher dans le contenu principal
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

        // Boutons
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        boutons.setBackground(new Color(240, 242, 248));

        JButton btnAjouter  = creerBouton("+ Ajouter",        COULEUR_SECONDAIRE);
        JButton btnSupprimer = creerBouton("Supprimer",        COULEUR_DANGER);
        JButton btnValider   = creerBouton("Valider compte",   COULEUR_SUCCES);

        btnAjouter.addActionListener(e -> ouvrirFormulaireAjout());
        btnSupprimer.addActionListener(e -> supprimerUtilisateur());
        btnValider.addActionListener(e -> validerCompte());

        boutons.add(btnAjouter);
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

    private void ouvrirFormulaireAjout() {
        new FormulaireUtilisateurView(this).setVisible(true);
    }

    private void supprimerUtilisateur() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez un utilisateur !"); return; }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer cet utilisateur ?", "Confirmation", JOptionPane.YES_NO_OPTION);
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