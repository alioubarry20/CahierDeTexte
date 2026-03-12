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
        super("Historique des séances");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("📅 Historique des séances"), BorderLayout.NORTH);

        // Filtre par cours
        JPanel panneauFiltre = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panneauFiltre.setBackground(COULEUR_FOND);
        panneauFiltre.add(new JLabel("Filtrer par cours :"));

        comboCours = new JComboBox<>();
        comboCours.setPreferredSize(new Dimension(250, 30));
        try {
            int enseignantId = Session.getUtilisateurConnecte().getId();
            List<Cours> cours = coursService.listerParEnseignant(enseignantId);
            comboCours.addItem(null); // option "Tous"
            for (Cours c : cours) comboCours.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }

        JButton btnFiltrer = creerBouton("🔍 Filtrer", COULEUR_SECONDAIRE);
        btnFiltrer.addActionListener(e -> chargerSeances());

        panneauFiltre.add(comboCours);
        panneauFiltre.add(btnFiltrer);
        add(panneauFiltre, BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Date", "Heure", "Durée", "Cours", "Statut"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(scroll, BorderLayout.CENTER);

        chargerSeances();
    }

    private void chargerSeances() {
        try {
            modeleTableau.setRowCount(0);
            int enseignantId = Session.getUtilisateurConnecte().getId();
            List<Cours> cours = coursService.listerParEnseignant(enseignantId);

            Cours filtre = (Cours) comboCours.getSelectedItem();

            for (Cours c : cours) {
                if (filtre != null && c.getId() != filtre.getId()) continue;
                List<Seance> seances = seanceService.listerParCours(c.getId());
                for (Seance s : seances) {
                    modeleTableau.addRow(new Object[]{
                        s.getId(),
                        s.getDate(),
                        s.getHeure(),
                        s.getDuree() + " min",
                        c.getIntitule(),
                        s.getStatut()
                    });
                }
            }
        } catch (Exception e) {
            afficherErreur("Erreur chargement : " + e.getMessage());
        }
    }
}