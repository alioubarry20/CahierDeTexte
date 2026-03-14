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
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 248));

        String[] colonnes = {"ID", "Nom", "Prenom", "Email", "Role"};
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

        JButton btnValider = creerBouton("Valider", COULEUR_SUCCES);
        JButton btnRefuser = creerBouton("Refuser", COULEUR_DANGER);

        btnValider.addActionListener(e -> validerCompte());
        btnRefuser.addActionListener(e -> refuserCompte());

        boutons.add(btnValider);
        boutons.add(btnRefuser);
        panel.add(boutons, BorderLayout.SOUTH);

        chargerComptes();
        return panel;
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
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void validerCompte() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez un compte !"); return; }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        try {
            service.validerCompte(id);
            afficherSucces("Compte valide !");
            chargerComptes();
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void refuserCompte() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez un compte !"); return; }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        try {
            service.supprimer(id);
            afficherSucces("Compte refuse et supprime !");
            chargerComptes();
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}