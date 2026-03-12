package com.esitec.cahier.ui.enseignant;

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

public class HistoriqueSeancesView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private JComboBox<Cours> comboCours;
    private CoursService coursService = new CoursService();
    private SeanceService seanceService = new SeanceService();

    public HistoriqueSeancesView() {
        super("Historique des seances");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 248));

        // Filtre
        JPanel filtre = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        filtre.setBackground(new Color(240, 242, 248));
        filtre.add(new JLabel("Filtrer par cours :"));

        comboCours = new JComboBox<>();
        comboCours.setPreferredSize(new Dimension(250, 30));
        comboCours.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        try {
            int id = Session.getUtilisateurConnecte().getId();
            List<Cours> cours = coursService.listerParEnseignant(id);
            comboCours.addItem(null);
            for (Cours c : cours) comboCours.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }

        JButton btnFiltrer = creerBouton("Filtrer", COULEUR_SECONDAIRE);
        btnFiltrer.setPreferredSize(new Dimension(100, 30));
        btnFiltrer.addActionListener(e -> chargerSeances());

        filtre.add(comboCours);
        filtre.add(btnFiltrer);
        panel.add(filtre, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Date", "Heure", "Duree", "Cours", "Statut"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableau.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        panel.add(scroll, BorderLayout.CENTER);

        chargerSeances();
        return panel;
    }

    private void chargerSeances() {
        try {
            modeleTableau.setRowCount(0);
            int id = Session.getUtilisateurConnecte().getId();
            List<Cours> cours = coursService.listerParEnseignant(id);
            Cours filtre = (Cours) comboCours.getSelectedItem();

            for (Cours c : cours) {
                if (filtre != null && c.getId() != filtre.getId()) continue;
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    modeleTableau.addRow(new Object[]{
                        s.getId(), s.getDate(), s.getHeure(),
                        s.getDuree() + " min", c.getIntitule(), s.getStatut()
                    });
                }
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}