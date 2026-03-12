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
        super("Ajouter une séance");
        initialiserUI();
    }

    private void initialiserUI() {
        setSize(500, 550);
        setLayout(new BorderLayout());
        add(creerHeader("➕ Ajouter une séance"), BorderLayout.NORTH);

        JPanel formulaire = new JPanel();
        formulaire.setLayout(new BoxLayout(formulaire, BoxLayout.Y_AXIS));
        formulaire.setBackground(Color.WHITE);
        formulaire.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        // Cours
        comboCours = new JComboBox<>();
        try {
            int enseignantId = Session.getUtilisateurConnecte().getId();
            List<Cours> cours = coursService.listerParEnseignant(enseignantId);
            for (Cours c : cours) comboCours.addItem(c);
        } catch (Exception e) {
            afficherErreur("Erreur chargement cours : " + e.getMessage());
        }

        // Champs
        champDate = new JTextField(LocalDate.now().toString());
        champHeure = new JTextField("08:00");
        champDuree = new JTextField("60");
        champContenu = new JTextArea(4, 20);
        champContenu.setLineWrap(true);
        champObservations = new JTextArea(3, 20);
        champObservations.setLineWrap(true);

        ajouterChamp(formulaire, "Cours", comboCours);
        ajouterChamp(formulaire, "Date (AAAA-MM-JJ)", champDate);
        ajouterChamp(formulaire, "Heure (HH:MM)", champHeure);
        ajouterChamp(formulaire, "Durée (minutes)", champDuree);
        ajouterChamp(formulaire, "Contenu du cours", new JScrollPane(champContenu));
        ajouterChamp(formulaire, "Observations", new JScrollPane(champObservations));

        JButton btnSauvegarder = creerBouton("💾 Enregistrer", COULEUR_SUCCES);
        btnSauvegarder.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnSauvegarder.addActionListener(e -> sauvegarder());

        formulaire.add(Box.createVerticalStrut(15));
        formulaire.add(btnSauvegarder);

        add(new JScrollPane(formulaire), BorderLayout.CENTER);
    }

    private void ajouterChamp(JPanel panel, String label, JComponent champ) {
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 12));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        champ.setMaximumSize(new Dimension(Integer.MAX_VALUE, champ instanceof JScrollPane ? 80 : 35));
        champ.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(lbl);
        panel.add(Box.createVerticalStrut(4));
        panel.add(champ);
        panel.add(Box.createVerticalStrut(12));
    }

    private void sauvegarder() {
        try {
            Cours cours = (Cours) comboCours.getSelectedItem();
            if (cours == null) {
                afficherErreur("Aucun cours disponible !");
                return;
            }

            Seance s = new Seance();
            s.setCours(cours);
            s.setDate(LocalDate.parse(champDate.getText().trim()));
            s.setHeure(LocalTime.parse(champHeure.getText().trim()));
            s.setDuree(Integer.parseInt(champDuree.getText().trim()));
            s.setContenu(champContenu.getText().trim());
            s.setObservations(champObservations.getText().trim());
            s.setStatut("EN_ATTENTE");

            seanceService.ajouter(s);
            afficherSucces("Séance enregistrée avec succès !");
            dispose();

        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }
}