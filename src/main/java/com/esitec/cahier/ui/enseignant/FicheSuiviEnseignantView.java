package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.FicheSuiviService;
import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class FicheSuiviEnseignantView extends BaseView {

    private JComboBox<Cours> comboCours;
    private CoursService coursService = new CoursService();
    private FicheSuiviService ficheSuiviService = new FicheSuiviService();

    public FicheSuiviEnseignantView() {
        super("Ma fiche de suivi");
        initialiserUI();
    }

    private void initialiserUI() {
        setSize(500, 300);
        setLayout(new BorderLayout());
        add(creerHeader("📄 Ma fiche de suivi"), BorderLayout.NORTH);

        JPanel contenu = new JPanel();
        contenu.setLayout(new BoxLayout(contenu, BoxLayout.Y_AXIS));
        contenu.setBackground(Color.WHITE);
        contenu.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JLabel lblCours = new JLabel("Sélectionner un cours :");
        lblCours.setFont(new Font("Arial", Font.BOLD, 13));
        lblCours.setAlignmentX(Component.LEFT_ALIGNMENT);

        comboCours = new JComboBox<>();
        comboCours.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        comboCours.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            int enseignantId = Session.getUtilisateurConnecte().getId();
            List<Cours> cours = coursService.listerParEnseignant(enseignantId);
            for (Cours c : cours) comboCours.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur chargement cours : " + e.getMessage());
        }

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
            afficherErreur("Aucun cours disponible !");
            return;
        }
        try {
            new java.io.File("exports").mkdirs();
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
            afficherErreur("Aucun cours disponible !");
            return;
        }
        try {
            new java.io.File("exports").mkdirs();
            String chemin = "exports/fiche_" + cours.getId() + ".xlsx";
            ficheSuiviService.exporterExcel(cours.getId(), chemin);
            afficherSucces("Excel généré : " + chemin);
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}