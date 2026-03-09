package com.esitec.cahier.ui;

import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;

public abstract class BaseView extends JFrame {

    protected static final Color COULEUR_PRIMAIRE   = new Color(33, 97, 140);
    protected static final Color COULEUR_SECONDAIRE = new Color(52, 152, 219);
    protected static final Color COULEUR_FOND       = new Color(236, 240, 241);
    protected static final Color COULEUR_TEXTE      = Color.WHITE;
    protected static final Color COULEUR_SUCCES     = new Color(39, 174, 96);
    protected static final Color COULEUR_DANGER     = new Color(192, 57, 43);

    protected static final Font POLICE_TITRE  = new Font("Arial", Font.BOLD, 22);
    protected static final Font POLICE_NORMAL = new Font("Arial", Font.PLAIN, 14);
    protected static final Font POLICE_BOUTON = new Font("Arial", Font.BOLD, 13);

    public BaseView(String titre) {
        setTitle("ESITEC — " + titre);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setBackground(COULEUR_FOND);
    }

    protected JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setFont(POLICE_BOUTON);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(180, 40));
        return btn;
    }

    protected JPanel creerHeader(String titrePage) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COULEUR_PRIMAIRE);
        header.setPreferredSize(new Dimension(getWidth(), 70));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblTitre = new JLabel(titrePage);
        lblTitre.setFont(POLICE_TITRE);
        lblTitre.setForeground(COULEUR_TEXTE);

        JPanel droite = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        droite.setOpaque(false);

        String nomUser = Session.estConnecte()
                ? Session.getUtilisateurConnecte().getNomComplet()
                : "";
        JLabel lblUser = new JLabel("👤 " + nomUser);
        lblUser.setFont(POLICE_NORMAL);
        lblUser.setForeground(COULEUR_TEXTE);

        JButton btnDeconnexion = creerBouton("Déconnexion", COULEUR_DANGER);
        btnDeconnexion.setPreferredSize(new Dimension(120, 35));
        btnDeconnexion.addActionListener(e -> deconnecter());

        droite.add(lblUser);
        droite.add(Box.createHorizontalStrut(15));
        droite.add(btnDeconnexion);

        header.add(lblTitre, BorderLayout.WEST);
        header.add(droite, BorderLayout.EAST);

        return header;
    }

    protected void deconnecter() {
        int choix = JOptionPane.showConfirmDialog(
                this,
                "Voulez-vous vraiment vous déconnecter ?",
                "Déconnexion",
                JOptionPane.YES_NO_OPTION
        );
        if (choix == JOptionPane.YES_OPTION) {
            Session.deconnecter();
            dispose();
            new LoginView().setVisible(true);
        }
    }

    protected void afficherSucces(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Succès ✅", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Erreur ❌", JOptionPane.ERROR_MESSAGE);
    }
}
