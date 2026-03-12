package com.esitec.cahier.ui.responsable;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.StatistiquesService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AvancementView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private CoursService coursService = new CoursService();
    private StatistiquesService statsService = new StatistiquesService();

    public AvancementView() {
        super("Avancement du programme");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 244));

        String[] colonnes = {"Cours", "Volume horaire", "Heures validees", "Taux d'avancement"};
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

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bas.setBackground(new Color(240, 248, 244));
        JButton btnRefresh = creerBouton("Actualiser", COULEUR_SECONDAIRE);
        btnRefresh.addActionListener(e -> chargerAvancement());
        bas.add(btnRefresh);
        panel.add(bas, BorderLayout.SOUTH);

        chargerAvancement();
        return panel;
    }

    private void chargerAvancement() {
        try {
            modeleTableau.setRowCount(0);
            List<Cours> cours = coursService.listerTous();
            for (Cours c : cours) {
                double taux = statsService.getTauxAvancement(c.getId());
                modeleTableau.addRow(new Object[]{
                    c.getIntitule(),
                    c.getVolumeHoraire() + "h",
                    String.format("%.1f", taux * c.getVolumeHoraire() / 100) + "h",
                    String.format("%.1f%%", taux)
                });
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}