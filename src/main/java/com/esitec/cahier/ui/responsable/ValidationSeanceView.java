package com.esitec.cahier.ui.responsable;

import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class ValidationSeanceView extends BaseView {

    public ValidationSeanceView() {
        super("Validation des séances");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("✅ Validation des séances"), BorderLayout.NORTH);

        JLabel lblEnCours = new JLabel("En cours de développement...");
        lblEnCours.setHorizontalAlignment(SwingConstants.CENTER);
        lblEnCours.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblEnCours, BorderLayout.CENTER);
    }
}