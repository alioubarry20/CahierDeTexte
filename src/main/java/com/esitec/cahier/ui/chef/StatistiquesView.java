package com.esitec.cahier.ui.chef;

import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class StatistiquesView extends BaseView {

    public StatistiquesView() {
        super("Statistiques globales");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("📊 Statistiques"), BorderLayout.NORTH);

        JLabel lblEnCours = new JLabel("En cours de développement...");
        lblEnCours.setHorizontalAlignment(SwingConstants.CENTER);
        lblEnCours.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblEnCours, BorderLayout.CENTER);
    }
}
