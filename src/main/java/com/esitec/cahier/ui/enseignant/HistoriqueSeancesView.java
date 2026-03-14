package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.SeanceService;
import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.List;

public class HistoriqueSeancesView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private CoursService coursService = new CoursService();
    private SeanceService seanceService = new SeanceService();

    public HistoriqueSeancesView() {
        super("Mes Seances");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COULEUR_FOND);

        String[] colonnes = {"Date", "Heure", "Duree", "Cours", "Contenu", "Statut", "Motif rejet"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau) {
            // Colorier les lignes selon statut
            public Component prepareRenderer(
                    javax.swing.table.TableCellRenderer r, int row, int col) {
                Component c = super.prepareRenderer(r, row, col);
                String statut = (String) getValueAt(row, 5);
                if ("REJETE".equals(statut)) {
                    c.setBackground(new Color(255, 235, 235));
                    c.setForeground(new Color(180, 0, 0));
                } else if ("VALIDE".equals(statut)) {
                    c.setBackground(new Color(235, 255, 240));
                    c.setForeground(new Color(0, 140, 70));
                } else {
                    c.setBackground(Color.WHITE);
                    c.setForeground(Color.BLACK);
                }
                return c;
            }
        };
        tableau.setRowHeight(30);
        tableau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableau.setGridColor(new Color(220, 220, 220));
        tableau.getColumnModel().getColumn(4).setPreferredWidth(200);
        tableau.getColumnModel().getColumn(6).setPreferredWidth(200);

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bas.setBackground(COULEUR_FOND);
        JButton btnRefresh = creerBouton("Actualiser", COULEUR_PRIMAIRE);
        btnRefresh.addActionListener(e -> chargerSeances());
        bas.add(btnRefresh);
        panel.add(bas, BorderLayout.SOUTH);

        chargerSeances();
        return panel;
    }

    private void chargerSeances() {
        try {
            modeleTableau.setRowCount(0);
            Enseignant enseignant = (Enseignant) Session.getUtilisateurConnecte();
            List<Cours> cours = coursService.listerParEnseignant(enseignant.getId());
            for (Cours c : cours) {
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    modeleTableau.addRow(new Object[]{
                        s.getDate(), s.getHeure(),
                        s.getDuree() + " min",
                        c.getIntitule(),
                        s.getContenu(),
                        s.getStatut(),
                        s.getCommentaireRejet() != null ? s.getCommentaireRejet() : ""
                    });
                }
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}