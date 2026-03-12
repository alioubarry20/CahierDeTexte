package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class MesCoursView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private CoursService service = new CoursService();

    public MesCoursView() {
        super("Mes cours");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 248));

        String[] colonnes = {"ID", "Intitule", "Volume horaire", "Classe"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableau.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.add(scroll, BorderLayout.CENTER);

        chargerCours();
        return panel;
    }

    private void chargerCours() {
        try {
            modeleTableau.setRowCount(0);
            int id = Session.getUtilisateurConnecte().getId();
            List<Cours> liste = service.listerParEnseignant(id);
            for (Cours c : liste) {
                modeleTableau.addRow(new Object[]{
                    c.getId(), c.getIntitule(),
                    c.getVolumeHoraire() + "h",
                    c.getClasse().getNom()
                });
            }
            if (liste.isEmpty()) {
                afficherErreur("Aucun cours assigne !");
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}