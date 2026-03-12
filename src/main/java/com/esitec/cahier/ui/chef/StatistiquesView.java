package com.esitec.cahier.ui.chef;

import com.esitec.cahier.service.StatistiquesService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class StatistiquesView extends BaseView {

    private StatistiquesService service = new StatistiquesService();

    public StatistiquesView() {
        super("Statistiques");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel();
        panel.setBackground(new Color(240, 242, 248));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        JPanel cards = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 0));
        cards.setBackground(new Color(240, 242, 248));
        cards.setAlignmentX(Component.LEFT_ALIGNMENT);

        try {
            int nbEns   = service.getNombreEnseignants();
            int nbCours = service.getNombreCours();
            int nbVal   = service.getNombreSeancesValidees();
            int nbAtt   = service.getNombreSeancesEnAttente();

            cards.add(creerCarteStats("Enseignants",  String.valueOf(nbEns),   new Color(0, 120, 215)));
            cards.add(creerCarteStats("Cours",        String.valueOf(nbCours), new Color(0, 180, 120)));
            cards.add(creerCarteStats("Seances val.", String.valueOf(nbVal),   new Color(255, 140, 0)));
            cards.add(creerCarteStats("En attente",   String.valueOf(nbAtt),   new Color(150, 50, 200)));
        } catch (Exception e) {
            cards.add(new JLabel("Erreur chargement stats"));
        }

        JLabel lblTitre = new JLabel("Vue d'ensemble");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitre.setForeground(new Color(30, 30, 60));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(lblTitre);
        panel.add(Box.createVerticalStrut(25));
        panel.add(cards);

        return panel;
    }
}