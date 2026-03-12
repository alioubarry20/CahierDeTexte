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
        initialiserUI();
        chargerUtilisateurs();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("👥 Gestion des utilisateurs"), BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Rôle", "Statut"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);

        // Boutons
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panneauBoutons.setBackground(COULEUR_FOND);

        JButton btnAjouter = creerBouton("➕ Ajouter", COULEUR_SECONDAIRE);
        JButton btnSupprimer = creerBouton("🗑 Supprimer", COULEUR_DANGER);
        JButton btnValider = creerBouton("✅ Valider compte", COULEUR_SUCCES);

        btnAjouter.addActionListener(e -> ouvrirFormulaireAjout());
        btnSupprimer.addActionListener(e -> supprimerUtilisateur());
        btnValider.addActionListener(e -> validerCompte());

        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnSupprimer);
        panneauBoutons.add(btnValider);

        add(panneauBoutons, BorderLayout.SOUTH);
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
            afficherErreur("Erreur chargement : " + e.getMessage());
        }
    }

    private void ouvrirFormulaireAjout() {
        new FormulaireUtilisateurView(this).setVisible(true);
    }

    private void supprimerUtilisateur() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            afficherErreur("Sélectionnez un utilisateur !");
            return;
        }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer cet utilisateur ?", "Confirmation",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.supprimer(id);
                afficherSucces("Utilisateur supprimé !");
                chargerUtilisateurs();
            } catch (Exception e) {
                afficherErreur("Erreur : " + e.getMessage());
            }
        }
    }

    private void validerCompte() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            afficherErreur("Sélectionnez un utilisateur !");
            return;
        }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        try {
            service.validerCompte(id);
            afficherSucces("Compte validé !");
            chargerUtilisateurs();
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    // Appelée depuis FormulaireUtilisateurView pour rafraîchir
    public void rafraichir() {
        chargerUtilisateurs();
    }
}