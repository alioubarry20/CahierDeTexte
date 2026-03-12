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
        initialiserUI();
        chargerCours();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("📋 Mes cours"), BorderLayout.NORTH);

        String[] colonnes = {"ID", "Intitulé", "Volume horaire", "Classe"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);
    }

    private void chargerCours() {
        try {
            modeleTableau.setRowCount(0);
            int enseignantId = Session.getUtilisateurConnecte().getId();
            List<Cours> liste = service.listerParEnseignant(enseignantId);
            for (Cours c : liste) {
                modeleTableau.addRow(new Object[]{
                    c.getId(),
                    c.getIntitule(),
                    c.getVolumeHoraire() + "h",
                    c.getClasse().getNom()
                });
            }
            if (liste.isEmpty()) {
                afficherErreur("Aucun cours assigné pour le moment !");
            }
        } catch (Exception e) {
            afficherErreur("Erreur chargement : " + e.getMessage());
        }
    }
}