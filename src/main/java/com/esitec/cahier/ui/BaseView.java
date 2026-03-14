package com.esitec.cahier.ui;

import com.esitec.cahier.util.Session;
import javax.swing.*;
import java.awt.*;

public abstract class BaseView extends JFrame {

    protected Color SIDEBAR_BG     = new Color(45, 45, 80);
    protected Color SIDEBAR_TOP    = new Color(35, 35, 65);
    protected Color SIDEBAR_ACTIVE = new Color(70, 70, 120);

    protected static final Color COULEUR_FOND       = new Color(245, 247, 250);
    protected static final Color COULEUR_PRIMAIRE   = new Color(0, 120, 215);
    protected static final Color COULEUR_SECONDAIRE = new Color(0, 120, 215);
    protected static final Color COULEUR_SUCCES     = new Color(0, 180, 120);
    protected static final Color COULEUR_DANGER     = new Color(220, 50, 50);
    protected static final Color COULEUR_TEXTE      = new Color(50, 50, 50);

    protected JPanel mainContent;
    protected JPanel sidebarItems;

    public BaseView(String titre) {
        setTitle("ESITEC - " + titre);
        setSize(1100, 680);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
    }

    protected void construireLayout(JPanel sidebar, JPanel contenu) {
        JPanel root = new JPanel(new BorderLayout());
        root.add(sidebar, BorderLayout.WEST);
        root.add(contenu, BorderLayout.CENTER);
        mainContent = contenu;
        add(root);
    }

    protected void changerContenu(String titre, JPanel panel) {
        mainContent.removeAll();
        mainContent.add(creerHeader(titre), BorderLayout.NORTH);
        mainContent.add(new JScrollPane(panel), BorderLayout.CENTER);
        mainContent.revalidate();
        mainContent.repaint();
    }

    protected JPanel creerSidebar(String sousTitre, String[][] menuItems) {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_BG);
        sidebar.setPreferredSize(new Dimension(220, 0));

        // ── TOP : titre ──────────────────────────────────
        JPanel top = new JPanel();
        top.setBackground(SIDEBAR_TOP);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JLabel lblNom = new JLabel("ESITEC");
        lblNom.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblNom.setForeground(Color.WHITE);
        lblNom.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblSub = new JLabel(sousTitre);
        lblSub.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblSub.setForeground(new Color(180, 180, 220));
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);

        top.add(lblNom);
        top.add(Box.createVerticalStrut(4));
        top.add(lblSub);

        sidebar.add(top, BorderLayout.NORTH);

        // ── MENU ─────────────────────────────────────────
        sidebarItems = new JPanel();
        sidebarItems.setBackground(SIDEBAR_BG);
        sidebarItems.setLayout(new BoxLayout(sidebarItems, BoxLayout.Y_AXIS));
        sidebarItems.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        for (String[] item : menuItems) {
            sidebarItems.add(creerMenuItem(item[1]));
        }

        sidebar.add(sidebarItems, BorderLayout.CENTER);

        // ── BAS : user + deconnexion ─────────────────────
        JPanel bas = new JPanel();
        bas.setBackground(SIDEBAR_TOP);
        bas.setLayout(new BoxLayout(bas, BoxLayout.Y_AXIS));
        bas.setBorder(BorderFactory.createEmptyBorder(12, 15, 12, 15));

        String nomUser = "";
        if (Session.getUtilisateurConnecte() != null) {
            nomUser = Session.getUtilisateurConnecte().getPrenom()
                + " " + Session.getUtilisateurConnecte().getNom();
        }

        JLabel lblUser = new JLabel(nomUser);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(Color.WHITE);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblDeconnexion = new JLabel("Deconnexion");
        lblDeconnexion.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        lblDeconnexion.setForeground(new Color(200, 100, 100));
        lblDeconnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblDeconnexion.setAlignmentX(Component.LEFT_ALIGNMENT);
        lblDeconnexion.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                deconnecter();
            }
        });

        bas.add(lblUser);
        bas.add(Box.createVerticalStrut(4));
        bas.add(lblDeconnexion);

        sidebar.add(bas, BorderLayout.SOUTH);

        return sidebar;
    }

    private JPanel creerMenuItem(String texte) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(SIDEBAR_BG);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 10));

        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(200, 200, 230));
        item.add(lbl, BorderLayout.CENTER);

        item.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                item.setBackground(SIDEBAR_ACTIVE);
                lbl.setForeground(Color.WHITE);
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                item.setBackground(SIDEBAR_BG);
                lbl.setForeground(new Color(200, 200, 230));
            }
        });

        return item;
    }

    protected JPanel creerHeader(String titre) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(16, 30, 16, 30)
        ));

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setForeground(COULEUR_TEXTE);

        String date = new java.text.SimpleDateFormat("EEEE dd MMMM yyyy",
            java.util.Locale.FRENCH).format(new java.util.Date());
        JLabel lblDate = new JLabel(date);
        lblDate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDate.setForeground(new Color(150, 150, 150));

        header.add(lblTitre, BorderLayout.WEST);
        header.add(lblDate, BorderLayout.EAST);

        return header;
    }

    protected JPanel creerCarteStats(String titre, String valeur, Color couleur) {
        JPanel carte = new JPanel();
        carte.setLayout(new BoxLayout(carte, BoxLayout.Y_AXIS));
        carte.setBackground(Color.WHITE);
        carte.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        carte.setPreferredSize(new Dimension(160, 100));

        JLabel lblValeur = new JLabel(valeur);
        lblValeur.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblValeur.setForeground(couleur);
        lblValeur.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lblTitre = new JLabel(titre);
        lblTitre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblTitre.setForeground(new Color(130, 130, 130));
        lblTitre.setAlignmentX(Component.LEFT_ALIGNMENT);

        carte.add(lblValeur);
        carte.add(Box.createVerticalStrut(5));
        carte.add(lblTitre);

        return carte;
    }

    protected JButton creerBouton(String texte, Color couleur) {
        JButton btn = new JButton(texte);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(couleur);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        return btn;
    }

    protected void afficherSucces(String message) {
        JOptionPane.showMessageDialog(this, message, "Succes",
            JOptionPane.INFORMATION_MESSAGE);
    }

    protected void afficherErreur(String message) {
        JOptionPane.showMessageDialog(this, message, "Erreur",
            JOptionPane.ERROR_MESSAGE);
    }

    protected void deconnecter() {
        Session.clear();
        new com.esitec.cahier.ui.LoginView().setVisible(true);
        dispose();
    }
}