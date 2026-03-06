package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class AjouterSeanceView extends BaseView {

    public AjouterSeanceView() {
        super("Ajouter une séance");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("➕ Ajouter une séance"), BorderLayout.NORTH);

        JLabel lblEnCours = new JLabel("En cours de développement...");
        lblEnCours.setHorizontalAlignment(SwingConstants.CENTER);
        lblEnCours.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblEnCours, BorderLayout.CENTER);
    }
}