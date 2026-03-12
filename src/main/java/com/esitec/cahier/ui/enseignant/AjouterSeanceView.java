package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Seance;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.SeanceService;
import com.esitec.cahier.ui.BaseView;
import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AjouterSeanceView extends BaseView {

    private JComboBox<Cours> comboCours;
    private JTextField champDate;
    private JTextField champHeure;
    private JTextField champDuree;
    private JTextArea champContenu;
    private JTextArea champObservations;

    private CoursService coursService = new CoursService();
    private SeanceService seanceService = new SeanceService();

    public AjouterSeanceView() {
        super("Ajouter une seance");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 248));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lblTitre = new JLabel("Nouvelle seance");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(new Color(30, 30, 60));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Cours
        comboCours = new JComboBox<>();
        comboCours.setMaximumSize(new Dimension(400, 38));
        comboCours.setAlignmentX(Component.LEFT_ALIGNMENT);
        try {
            int id = Session.getUtilisateurConnecte().getId();
            List<Cours> cours = coursService.listerParEnseignant(id);
            for (Cours c : cours) comboCours.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }

        champDate         = new JTextField(LocalDate.now().toString());
        champHeure        = new JTextField("08:00");
        champDuree        = new JTextField("60");
        champContenu      = new JTextArea(4, 20);
        champObservations = new JTextArea(3, 20);
        champContenu.setLineWrap(true);
        champObservations.setLineWrap(true);

        // Style champs
        for (JTextField f : new JTextField[]{champDate, champHeure, champDuree}) {
            f.setMaximumSize(new Dimension(400, 38));
            f.setAlignmentX(Component.LEFT_ALIGNMENT);
            f.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        }

        JButton btnSauvegarder = creerBouton("Enregistrer", COULEUR_SUCCES);
        btnSauvegarder.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnSauvegarder.addActionListener(e -> sauvegarder());

        panel.add(lblTitre);
        panel.add(Box.createVerticalStrut(25));
        panel.add(creerLabel("Cours")); panel.add(Box.createVerticalStrut(5));
        panel.add(comboCours); panel.add(Box.createVerticalStrut(12));
        panel.add(creerLabel("Date (AAAA-MM-JJ)")); panel.add(Box.createVerticalStrut(5));
        panel.add(champDate); panel.add(Box.createVerticalStrut(12));
        panel.add(creerLabel("Heure (HH:MM)")); panel.add(Box.createVerticalStrut(5));
        panel.add(champHeure); panel.add(Box.createVerticalStrut(12));
        panel.add(creerLabel("Duree (minutes)")); panel.add(Box.createVerticalStrut(5));
        panel.add(champDuree); panel.add(Box.createVerticalStrut(12));
        panel.add(creerLabel("Contenu")); panel.add(Box.createVerticalStrut(5));
        JScrollPane sc1 = new JScrollPane(champContenu);
        sc1.setMaximumSize(new Dimension(400, 90));
        sc1.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sc1); panel.add(Box.createVerticalStrut(12));
        panel.add(creerLabel("Observations")); panel.add(Box.createVerticalStrut(5));
        JScrollPane sc2 = new JScrollPane(champObservations);
        sc2.setMaximumSize(new Dimension(400, 70));
        sc2.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(sc2); panel.add(Box.createVerticalStrut(20));
        panel.add(btnSauvegarder);

        return panel;
    }

    private JLabel creerLabel(String texte) {
        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(new Color(80, 80, 80));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private void sauvegarder() {
        try {
            Cours cours = (Cours) comboCours.getSelectedItem();
            if (cours == null) { afficherErreur("Aucun cours !"); return; }

            Seance s = new Seance();
            s.setCours(cours);
            s.setDate(LocalDate.parse(champDate.getText().trim()));
            s.setHeure(LocalTime.parse(champHeure.getText().trim()));
            s.setDuree(Integer.parseInt(champDuree.getText().trim()));
            s.setContenu(champContenu.getText().trim());
            s.setObservations(champObservations.getText().trim());
            s.setStatut("EN_ATTENTE");

            new SeanceService().ajouter(s);
            afficherSucces("Seance enregistree !");

            // Vider les champs
            champDate.setText(LocalDate.now().toString());
            champHeure.setText("08:00");
            champDuree.setText("60");
            champContenu.setText("");
            champObservations.setText("");

        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}