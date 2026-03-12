package com.esitec.cahier.ui.responsable;

import com.esitec.cahier.model.Cours;
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
        super("Validation des séances");
        initialiserUI();
        chargerSeances();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("✅ Validation des séances"), BorderLayout.NORTH);

        String[] colonnes = {"ID", "Date", "Heure", "Cours", "Contenu", "Statut"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        tableau.getColumnModel().getColumn(4).setPreferredWidth(200);

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        // Boutons
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panneauBoutons.setBackground(COULEUR_FOND);

        JButton btnValider = creerBouton("✅ Valider", COULEUR_SUCCES);
        JButton btnRejeter = creerBouton("❌ Rejeter", COULEUR_DANGER);

        btnValider.addActionListener(e -> validerSeance());
        btnRejeter.addActionListener(e -> rejeterSeance());

        panneauBoutons.add(btnValider);
        panneauBoutons.add(btnRejeter);
        add(panneauBoutons, BorderLayout.SOUTH);
    }

    private void chargerSeances() {
        try {
            modeleTableau.setRowCount(0);
            List<Cours> cours = coursService.listerTous();

            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    if (s.getStatut().equals("EN_ATTENTE")) {
                        modeleTableau.addRow(new Object[]{
                            s.getId(),
                            s.getDate(),
                            s.getHeure(),
                            c.getIntitule(),
                            s.getContenu(),
                            s.getStatut()
                        });
                    }
                }
            }

            if (modeleTableau.getRowCount() == 0) {
                afficherSucces("Aucune séance en attente !");
            }
        } catch (Exception e) {
            afficherErreur("Erreur chargement : " + e.getMessage());
        }
    }

    private void validerSeance() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            afficherErreur("Sélectionnez une séance !");
            return;
        }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        try {
            seanceService.valider(id);
            afficherSucces("Séance validée !");
            chargerSeances();
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void rejeterSeance() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            afficherErreur("Sélectionnez une séance !");
            return;
        }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        String commentaire = JOptionPane.showInputDialog(this,
            "Motif du rejet :", "Rejeter la séance",
            JOptionPane.QUESTION_MESSAGE);
        if (commentaire != null && !commentaire.trim().isEmpty()) {
            try {
                seanceService.rejeter(id, commentaire);
                afficherSucces("Séance rejetée !");
                chargerSeances();
            } catch (Exception e) {
                afficherErreur("Erreur : " + e.getMessage());
            }
        }
    }
}