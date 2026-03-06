package com.esitec.cahier.ui.responsable;

import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import java.awt.*;

public class CahierDeTexteView extends BaseView {

    public CahierDeTexteView() {
        super("Cahier de texte");
        initialiserUI();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("📖 Cahier de texte"), BorderLayout.NORTH);

        JLabel lblEnCours = new JLabel("En cours de développement...");
        lblEnCours.setHorizontalAlignment(SwingConstants.CENTER);
        lblEnCours.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblEnCours, BorderLayout.CENTER);
    }
}