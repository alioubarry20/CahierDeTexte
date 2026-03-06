package com.esitec.cahier.ui.enseignant;

import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class HistoriqueSeancesView extends BaseView {

    public HistoriqueSeancesView() {
        super("Historique des séances");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("📅 Historique des séances"), BorderLayout.NORTH);

        JLabel lblEnCours = new JLabel("En cours de développement...");
        lblEnCours.setHorizontalAlignment(SwingConstants.CENTER);
        lblEnCours.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblEnCours, BorderLayout.CENTER);
    }
}