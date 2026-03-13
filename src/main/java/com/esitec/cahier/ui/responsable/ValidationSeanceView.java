package com.esitec.cahier.ui.responsable;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.ResponsableClasse;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.SeanceService;
import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ValidationSeanceView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private CoursService coursService = new CoursService();
    private SeanceService seanceService = new SeanceService();

    public ValidationSeanceView() {
        super("Validation des seances");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 244));

        String[] colonnes = {"ID", "Date", "Heure", "Cours", "Contenu", "Statut"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableau.setGridColor(new Color(220, 220, 220));
        tableau.getColumnModel().getColumn(4).setPreferredWidth(200);

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        boutons.setBackground(new Color(240, 248, 244));

        JButton btnValider = creerBouton("Valider", COULEUR_SUCCES);
        JButton btnRejeter = creerBouton("Rejeter", COULEUR_DANGER);

        btnValider.addActionListener(e -> validerSeance());
        btnRejeter.addActionListener(e -> rejeterSeance());

        boutons.add(btnValider);
        boutons.add(btnRejeter);
        panel.add(boutons, BorderLayout.SOUTH);

        chargerSeances();
        return panel;
    }

    private void chargerSeances() {
        try {
            modeleTableau.setRowCount(0);

            // Filtrer par classe du responsable
            ResponsableClasse responsable =
                (ResponsableClasse) Session.getUtilisateurConnecte();

            List<Cours> cours = responsable.getClasse() != null
                ? coursService.listerParClasse(responsable.getClasse().getId())
                : coursService.listerTous();

            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    if (s.getStatut().equals("EN_ATTENTE")) {
                        modeleTableau.addRow(new Object[]{
                            s.getId(), s.getDate(), s.getHeure(),
                            c.getIntitule(), s.getContenu(), s.getStatut()
                        });
                    }
                }
            }

            if (modeleTableau.getRowCount() == 0) {
                afficherErreur("Aucune seance en attente pour votre classe !");
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void validerSeance() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez une seance !"); return; }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        try {
            seanceService.valider(id);
            afficherSucces("Seance validee !");
            chargerSeances();
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void rejeterSeance() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez une seance !"); return; }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        String commentaire = JOptionPane.showInputDialog(this,
            "Motif du rejet :", "Rejeter", JOptionPane.QUESTION_MESSAGE);
        if (commentaire != null && !commentaire.trim().isEmpty()) {
            try {
                seanceService.rejeter(id, commentaire);
                afficherSucces("Seance rejetee !");
                chargerSeances();
            } catch (Exception e) {
                afficherErreur("Erreur : " + e.getMessage());
            }
        }
    }
}