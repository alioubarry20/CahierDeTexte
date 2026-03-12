package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.service.UtilisateurService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ValidationComptesView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private UtilisateurService service = new UtilisateurService();

    public ValidationComptesView() {
        super("Validation des comptes");
        initialiserUI();
        chargerComptes();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("✅ Validation des comptes"), BorderLayout.NORTH);

        String[] colonnes = {"ID", "Nom", "Prénom", "Email", "Rôle"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);

        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panneauBoutons.setBackground(COULEUR_FOND);

        JButton btnValider = creerBouton("✅ Valider", COULEUR_SUCCES);
        JButton btnRefuser = creerBouton("❌ Refuser", COULEUR_DANGER);

        btnValider.addActionListener(e -> validerCompte());
        btnRefuser.addActionListener(e -> refuserCompte());

        panneauBoutons.add(btnValider);
        panneauBoutons.add(btnRefuser);
        add(panneauBoutons, BorderLayout.SOUTH);
    }

    private void chargerComptes() {
        try {
            modeleTableau.setRowCount(0);
            List<Utilisateur> liste = service.listerEnAttente();
            for (Utilisateur u : liste) {
                modeleTableau.addRow(new Object[]{
                    u.getId(), u.getNom(), u.getPrenom(),
                    u.getEmail(), u.getRole()
                });
            }
            if (liste.isEmpty()) {
                afficherSucces("Aucun compte en attente !");
            }
        } catch (Exception e) {
            afficherErreur("Erreur chargement : " + e.getMessage());
        }
    }

    private void validerCompte() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            afficherErreur("Sélectionnez un compte !");
            return;
        }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        try {
            service.validerCompte(id);
            afficherSucces("Compte validé !");
            chargerComptes();
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void refuserCompte() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            afficherErreur("Sélectionnez un compte !");
            return;
        }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        try {
            service.supprimer(id);
            afficherSucces("Compte refusé et supprimé !");
            chargerComptes();
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}