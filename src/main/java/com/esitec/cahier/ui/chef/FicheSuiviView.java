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
        super("Fiche de suivi");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 248));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JLabel lblTitre = new JLabel("Generer une fiche de suivi");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(new Color(30, 30, 60));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblCours = new JLabel("Selectionner un cours :");
        lblCours.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblCours.setForeground(new Color(100, 100, 100));
        lblCours.setAlignmentX(Component.LEFT_ALIGNMENT);

        comboCours = new JComboBox<>();
        comboCours.setMaximumSize(new Dimension(400, 38));
        comboCours.setAlignmentX(Component.LEFT_ALIGNMENT);
        comboCours.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        try {
            List<Cours> cours = coursService.listerTous();
            for (Cours c : cours) comboCours.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur chargement cours : " + e.getMessage());
        }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnPanel.setBackground(new Color(240, 242, 248));
        btnPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnPDF   = creerBouton("Exporter PDF",   new Color(211, 84, 0));
        JButton btnExcel = creerBouton("Exporter Excel", new Color(39, 174, 96));

        btnPDF.addActionListener(e -> exporterPDF());
        btnExcel.addActionListener(e -> exporterExcel());

        btnPanel.add(btnPDF);
        btnPanel.add(Box.createHorizontalStrut(15));
        btnPanel.add(btnExcel);

        panel.add(lblTitre);
        panel.add(Box.createVerticalStrut(30));
        panel.add(lblCours);
        panel.add(Box.createVerticalStrut(8));
        panel.add(comboCours);
        panel.add(Box.createVerticalStrut(25));
        panel.add(btnPanel);

        return panel;
    }

    private void exporterPDF() {
        Cours cours = (Cours) comboCours.getSelectedItem();
        if (cours == null) { afficherErreur("Selectionnez un cours !"); return; }
        try {
            new java.io.File("exports").mkdirs();
            String chemin = "exports/fiche_" + cours.getId() + ".pdf";
            ficheSuiviService.exporterPDF(cours.getId(), chemin);
            afficherSucces("PDF genere : " + chemin);
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void exporterExcel() {
        Cours cours = (Cours) comboCours.getSelectedItem();
        if (cours == null) { afficherErreur("Selectionnez un cours !"); return; }
        try {
            new java.io.File("exports").mkdirs();
            String chemin = "exports/fiche_" + cours.getId() + ".xlsx";
            ficheSuiviService.exporterExcel(cours.getId(), chemin);
            afficherSucces("Excel genere : " + chemin);
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}