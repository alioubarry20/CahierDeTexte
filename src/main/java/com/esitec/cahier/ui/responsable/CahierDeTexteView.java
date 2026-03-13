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

public class CahierDeTexteView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private CoursService coursService = new CoursService();
    private SeanceService seanceService = new SeanceService();

    public CahierDeTexteView() {
        super("Cahier de texte");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 248, 244));

        String[] colonnes = {"Date", "Heure", "Duree", "Cours", "Contenu", "Statut"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableau.setGridColor(new Color(220, 220, 220));
        tableau.getColumnModel().getColumn(4).setPreferredWidth(250);

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel bas = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        bas.setBackground(new Color(240, 248, 244));
        JButton btnRefresh = creerBouton("Actualiser", COULEUR_SECONDAIRE);
        btnRefresh.addActionListener(e -> chargerSeances());
        bas.add(btnRefresh);
        panel.add(bas, BorderLayout.SOUTH);

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
                    modeleTableau.addRow(new Object[]{
                        s.getDate(), s.getHeure(),
                        s.getDuree() + " min",
                        c.getIntitule(),
                        s.getContenu(),
                        s.getStatut()
                    });
                }
            }

            if (modeleTableau.getRowCount() == 0) {
                afficherErreur("Aucune seance pour votre classe !");
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}