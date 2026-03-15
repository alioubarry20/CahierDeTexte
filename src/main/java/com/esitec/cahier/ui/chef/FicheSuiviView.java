package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.FicheSuivi;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.FicheSuiviService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class FicheSuiviView extends BaseView {

    private CoursService     coursService     = new CoursService();
    private FicheSuiviService ficheSuiviService = new FicheSuiviService();
    private JComboBox<Cours> comboCours;
    private JPanel           panelMessage;

    public FicheSuiviView() {
        super("Fiche de Suivi");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel();
        panel.setBackground(COULEUR_FOND);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        // Carte principale
        JPanel carte = new JPanel();
        carte.setBackground(Color.WHITE);
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(24, 24, 24, 24)
        ));
        carte.setMaximumSize(new Dimension(600, 400));
        carte.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel titre = new JLabel("Generer une fiche de suivi");
        titre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titre.setForeground(new Color(50, 50, 50));
        titre.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label cours
        JLabel lblCours = new JLabel("Selectionner un cours");
        lblCours.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblCours.setForeground(new Color(85, 85, 85));
        lblCours.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Combo cours
        comboCours = new JComboBox<>();
        comboCours.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        comboCours.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        comboCours.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            List<Cours> cours = coursService.listerTous();
            for (Cours c : cours) comboCours.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur chargement cours : " + e.getMessage());
        }

        // Boutons export
        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        boutons.setBackground(Color.WHITE);
        boutons.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnPdf = creerBouton("Exporter PDF", new Color(229, 57, 53));
        JButton btnExcel = creerBouton("Exporter Excel", new Color(27, 94, 32));

        btnPdf.addActionListener(e -> exporter("PDF"));
        btnExcel.addActionListener(e -> exporter("EXCEL"));

        boutons.add(btnPdf);
        boutons.add(btnExcel);

        // Panel message succès
        panelMessage = new JPanel(new BorderLayout(12, 0));
        panelMessage.setBackground(new Color(232, 249, 242));
        panelMessage.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(178, 223, 219), 1),
            BorderFactory.createEmptyBorder(12, 14, 12, 14)
        ));
        panelMessage.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));
        panelMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        panelMessage.setVisible(false);

        carte.add(titre);
        carte.add(Box.createVerticalStrut(20));
        carte.add(lblCours);
        carte.add(Box.createVerticalStrut(6));
        carte.add(comboCours);
        carte.add(Box.createVerticalStrut(20));
        carte.add(boutons);
        carte.add(Box.createVerticalStrut(20));
        carte.add(panelMessage);

        panel.add(carte);
        return panel;
    }

    private void exporter(String type) {
        Cours cours = (Cours) comboCours.getSelectedItem();
        if (cours == null) {
            afficherErreur("Selectionnez un cours !");
            return;
        }

        try {
            String chemin;
            if ("PDF".equals(type)) {
                chemin = "exports/fiche_" + cours.getIntitule()
                    .replaceAll(" ", "_") + ".pdf";
                ficheSuiviService.exporterPDF(cours.getId(), chemin);
            } else {
                chemin = "exports/fiche_" + cours.getIntitule()
                    .replaceAll(" ", "_") + ".xlsx";
                ficheSuiviService.exporterExcel(cours.getId(), chemin);
            }
            afficherMessageSucces(chemin);
        } catch (Exception e) {
            afficherErreur("Erreur export : " + e.getMessage());
        }
    }
    private void afficherMessageSucces(String chemin) {
        panelMessage.removeAll();

        // Icone + texte
        JPanel gauche = new JPanel();
        gauche.setBackground(new Color(232, 249, 242));
        gauche.setLayout(new BoxLayout(gauche, BoxLayout.Y_AXIS));

        JLabel lblSucces = new JLabel("Fichier exporte avec succes !");
        lblSucces.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblSucces.setForeground(new Color(0, 180, 120));

        JLabel lblChemin = new JLabel(new File(chemin).getName() + " · exports/");
        lblChemin.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblChemin.setForeground(new Color(136, 136, 136));

        gauche.add(lblSucces);
        gauche.add(lblChemin);

        // Bouton ouvrir dossier
        JButton btnOuvrir = creerBouton("Ouvrir le dossier", new Color(0, 120, 215));
        btnOuvrir.addActionListener(e -> {
            try {
                File dossier = new File("exports").getAbsoluteFile();
                if (!dossier.exists()) dossier.mkdirs();
                Desktop.getDesktop().open(dossier);
            } catch (Exception ex) {
                afficherErreur("Impossible d'ouvrir le dossier : " + ex.getMessage());
            }
        });

        panelMessage.add(gauche, BorderLayout.CENTER);
        panelMessage.add(btnOuvrir, BorderLayout.EAST);
        panelMessage.setVisible(true);
        panelMessage.revalidate();
        panelMessage.repaint();
    }
}