package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.FicheSuiviService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FicheSuiviView extends BaseView {

    private JComboBox<Cours> comboCours;
    private CoursService coursService = new CoursService();
    private FicheSuiviService ficheSuiviService = new FicheSuiviService();

    public FicheSuiviView() {
        super("Fiche de suivi pédagogique");
        initialiserUI();
    }

    private void initialiserUI() {
        setSize(500, 300);
        setLayout(new BorderLayout());
        add(creerHeader("📄 Fiche de suivi"), BorderLayout.NORTH);

        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Color.WHITE);
        contenu.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Sélection du cours
        JLabel lblCours = new JLabel("Sélectionner un cours :");
        lblCours.setFont(new Font("Arial", Font.BOLD, 13));
        lblCours.setAlignmentX(Component.LEFT_ALIGNMENT);

        comboCours = new JComboBox<>();
        comboCours.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboCours.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Charger les cours
        try {
            List<Cours> cours = coursService.listerTous();
            for (Cours c : cours) comboCours.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur chargement cours : " + e.getMessage());
        }

        // Boutons export
        JButton btnPDF = creerBouton("📄 Exporter en PDF", new Color(211, 84, 0));
        JButton btnExcel = creerBouton("📊 Exporter en Excel", new Color(39, 174, 96));
        btnPDF.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnExcel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnPDF.addActionListener(e -> exporterPDF());
        btnExcel.addActionListener(e -> exporterExcel());

        contenu.add(lblCours);
        contenu.add(Box.createVerticalStrut(10));
        contenu.add(comboCours);
        contenu.add(Box.createVerticalStrut(25));
        contenu.add(btnPDF);
        contenu.add(Box.createVerticalStrut(10));
        contenu.add(btnExcel);

        add(contenu, BorderLayout.CENTER);
    }

    private void exporterPDF() {
        Cours cours = (Cours) comboCours.getSelectedItem();
        if (cours == null) {
            afficherErreur("Sélectionnez un cours !");
            return;
        }
        try {
            String chemin = "exports/fiche_" + cours.getId() + ".pdf";
            ficheSuiviService.exporterPDF(cours.getId(), chemin);
            afficherSucces("PDF généré : " + chemin);
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void exporterExcel() {
        Cours cours = (Cours) comboCours.getSelectedItem();
        if (cours == null) {
            afficherErreur("Sélectionnez un cours !");
            return;
        }
        try {
            String chemin = "exports/fiche_" + cours.getId() + ".xlsx";
            ficheSuiviService.exporterExcel(cours.getId(), chemin);
            afficherSucces("Excel généré : " + chemin);
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}