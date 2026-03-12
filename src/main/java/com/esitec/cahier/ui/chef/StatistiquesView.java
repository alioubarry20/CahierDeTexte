package com.esitec.cahier.ui.chef;

import com.esitec.cahier.service.StatistiquesService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class StatistiquesView extends BaseView {

    private StatistiquesService service = new StatistiquesService();

    public StatistiquesView() {
        super("Statistiques");
        initialiserUI();
    }

    private void initialiserUI() {
        setSize(600, 400);
        setLayout(new BorderLayout());
        add(creerHeader("📊 Statistiques globales"), BorderLayout.NORTH);

        JPanel contenu = new JPanel(new GridLayout(2, 2, 20, 20));
        contenu.setBackground(COULEUR_FOND);
        contenu.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        try {
            int nbEnseignants = service.getNombreEnseignants();
            int nbCours = service.getNombreCours();
            int nbValidees = service.getNombreSeancesValidees();
            int nbAttente = service.getNombreSeancesEnAttente();

            contenu.add(creerCarte("👨‍🏫 Enseignants",
                String.valueOf(nbEnseignants), COULEUR_SECONDAIRE));
            contenu.add(creerCarte("📚 Cours",
                String.valueOf(nbCours), new Color(142, 68, 173)));
            contenu.add(creerCarte("✅ Séances validées",
                String.valueOf(nbValidees), COULEUR_SUCCES));
            contenu.add(creerCarte("⏳ Séances en attente",
                String.valueOf(nbAttente), new Color(211, 84, 0)));

        } catch (Exception e) {
            afficherErreur("Erreur chargement : " + e.getMessage());
        }

        add(contenu, BorderLayout.CENTER);
    }

    private JPanel creerCarte(String titre, String valeur, Color couleur) {
        JPanel carte = new JPanel(new BorderLayout());
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitre.setForeground(couleur);

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("Arial", Font.BOLD, 48));
        lblValeur.setForeground(couleur);
        lblValeur.setHorizontalAlignment(SwingConstants.CENTER);

        carte.add(lblTitre, BorderLayout.NORTH);
        carte.add(lblValeur, BorderLayout.CENTER);

        return carte;
    }
}