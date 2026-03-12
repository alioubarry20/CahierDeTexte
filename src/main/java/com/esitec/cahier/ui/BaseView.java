package com.esitec.cahier.ui;

import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public abstract class BaseView extends JFrame {

    protected static final Color COULEUR_PRIMAIRE   = new Color(33, 97, 140);
    protected static final Color COULEUR_SECONDAIRE = new Color(52, 152, 219);
    protected static final Color COULEUR_FOND       = new Color(240, 242, 248);
    protected static final Color COULEUR_TEXTE      = Color.WHITE;
    protected static final Color COULEUR_SUCCES     = new Color(39, 174, 96);
    protected static final Color COULEUR_DANGER     = new Color(192, 57, 43);

    protected static final Font POLICE_TITRE  = new Font("Segoe UI", Font.BOLD, 20);
    protected static final Font POLICE_NORMAL = new Font("Segoe UI", Font.PLAIN, 13);
    protected static final Font POLICE_BOUTON = new Font("Segoe UI", Font.BOLD, 13);

    protected Color SIDEBAR_BG     = new Color(45, 45, 80);
    protected Color SIDEBAR_TOP    = new Color(35, 35, 65);
    protected Color SIDEBAR_ACTIVE = new Color(70, 70, 120);

    protected JPanel sidebarItems;
    protected JPanel mainContent;

    public BaseView(String titre) {
        setTitle("ESITEC - " + titre);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setBackground(COULEUR_FOND);
    }

    protected void construireLayout(JPanel sidebar, JPanel contenu) {
        mainContent = contenu; // ← référence sauvegardée
        JPanel root = new JPanel(new BorderLayout());
        root.add(sidebar, BorderLayout.WEST);
        root.add(contenu, BorderLayout.CENTER);
        add(root);
    }

    // Changer le contenu principal sans ouvrir une nouvelle fenêtre
    protected void changerContenu(String titrePage, JPanel nouveauContenu) {
        mainContent.removeAll();
        mainContent.add(creerHeader(titrePage), BorderLayout.NORTH);
        mainContent.add(nouveauContenu, BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    protected JPanel creerSidebar(String sousTitre, String[][] menuItems) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        // Logo
        JPanel logo = new JPanel();
        logo.setBackground(SIDEBAR_TOP);
        logo.setLayout(new BoxLayout(logo, BoxLayout.Y_AXIS));
        logo.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        JLabel lblTitre = new JLabel("ESITEC");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setForeground(Color.WHITE);

        JLabel lblSub = new JLabel(sousTitre);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblSub.setForeground(new Color(180, 180, 200));

        logo.add(lblTitre);
        logo.add(lblSub);

        // Menu items
        sidebarItems = new JPanel();
        sidebarItems.setBackground(SIDEBAR_BG);
        sidebarItems.setLayout(new BoxLayout(sidebarItems, BoxLayout.Y_AXIS));
        sidebarItems.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        for (int i = 0; i < menuItems.length; i++) {
            sidebarItems.add(creerMenuItem(menuItems[i][1], i == 0));
        }

        // Bottom
        JPanel bottom = new JPanel();
        bottom.setBackground(SIDEBAR_TOP);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
        bottom.setBorder(BorderFactory.createEmptyBorder(16, 20, 16, 20));

        String nomUser = Session.estConnecte()
            ? Session.getUtilisateurConnecte().getNomComplet() : "";
        JLabel lblUser = new JLabel(nomUser);
        lblUser.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblUser.setForeground(Color.WHITE);

        JLabel lblLogout = new JLabel("Deconnexion");
        lblLogout.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblLogout.setForeground(new Color(255, 150, 150));
        lblLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblLogout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                deconnecter();
            }
        });

        bottom.add(lblUser);
        bottom.add(Box.createVerticalStrut(6));
        bottom.add(lblLogout);

        sidebar.add(logo, BorderLayout.NORTH);
        sidebar.add(sidebarItems, BorderLayout.CENTER);
        sidebar.add(bottom, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel creerMenuItem(String texte, boolean actif) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 13));
        item.setBackground(actif ? SIDEBAR_ACTIVE : SIDEBAR_BG);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel lbl = new JLabel("  " + texte);
        lbl.setFont(new Font("Segoe UI", actif ? Font.BOLD : Font.PLAIN, 13));
        lbl.setForeground(actif ? Color.WHITE : new Color(190, 190, 210));

        item.add(lbl);
        return item;
    }

    protected JPanel creerHeader(String titrePage) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setPreferredSize(new Dimension(getWidth(), 70));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(0, 30, 0, 30)
        ));

        JLabel lblTitre = new JLabel(titrePage);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitre.setForeground(new Color(30, 30, 60));

        String date = LocalDate.now().format(
            DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.FRENCH));
        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDate.setForeground(new Color(150, 150, 150));

        header.add(lblTitre, BorderLayout.WEST);
        header.add(lblDate, BorderLayout.EAST);

        return header;
    }

    protected JPanel creerCarteStats(String titre, String valeur, Color couleur) {
        JPanel carte = new JPanel(new BorderLayout());
        carte.setBackground(Color.WHITE);
        carte.setPreferredSize(new Dimension(150, 95));
        carte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, couleur),
            BorderFactory.createEmptyBorder(14, 16, 14, 16)
        ));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitre.setForeground(new Color(136, 136, 136));

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("Segoe UI", Font.BOLD, 30));
        lblValeur.setForeground(couleur);

        carte.add(lblTitre, BorderLayout.NORTH);
        carte.add(lblValeur, BorderLayout.CENTER);

        return carte;
    }

    protected JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setFont(POLICE_BOUTON);
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(160, 38));
        return btn;
    }

    protected void deconnecter() {
        int choix = JOptionPane.showConfirmDialog(this,
            "Voulez-vous vraiment vous deconnecter ?",
            "Deconnexion", JOptionPane.YES_NO_OPTION);
        if (choix == JOptionPane.YES_OPTION) {
            Session.deconnecter();
            dispose();
            new LoginView().setVisible(true);
        }
    }

    protected void afficherSucces(String message) {
        JOptionPane.showMessageDialog(this, message, "Succes",
            JOptionPane.INFORMATION_MESSAGE);
    }

    protected void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur",
            JOptionPane.ERROR_MESSAGE);
    }
}