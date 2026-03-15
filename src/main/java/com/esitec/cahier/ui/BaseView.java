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

    protected static Font fontAwesome;

    static {
        try {
            fontAwesome = Font.createFont(Font.TRUETYPE_FONT,
                BaseView.class.getResourceAsStream("/fontawesome.ttf"))
                .deriveFont(14f);
            GraphicsEnvironment.getLocalGraphicsEnvironment()
                .registerFont(fontAwesome);
        } catch (Exception e) {
            fontAwesome = new Font("Segoe UI", Font.PLAIN, 14);
        }
    }

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
        sidebar.setPreferredSize(new Dimension(230, 0));

        // ── TOP ──────────────────────────────────────────
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
            sidebarItems.add(creerMenuItem(item[0], item[1]));
        }

        sidebar.add(sidebarItems, BorderLayout.CENTER);

        // ── BAS : user + deconnexion ──────────────────────
        JPanel bas = new JPanel();
        bas.setBackground(SIDEBAR_TOP);
        bas.setLayout(new BoxLayout(bas, BoxLayout.Y_AXIS));
        bas.setBorder(BorderFactory.createEmptyBorder(12, 14, 14, 14));

        // Avatar + nom
        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(SIDEBAR_TOP);
        userPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JPanel avatar = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(SIDEBAR_ACTIVE);
                g2.fillOval(0, 0, 36, 36);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
                FontMetrics fm = g2.getFontMetrics();
                String initiales = getInitiales();
                int x = (36 - fm.stringWidth(initiales)) / 2;
                int y = (36 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(initiales, x, y);
            }
            private String getInitiales() {
                if (Session.getUtilisateurConnecte() == null) return "?";
                String p = Session.getUtilisateurConnecte().getPrenom();
                String n = Session.getUtilisateurConnecte().getNom();
                return (p.isEmpty() ? "" : String.valueOf(p.charAt(0)).toUpperCase())
                     + (n.isEmpty() ? "" : String.valueOf(n.charAt(0)).toUpperCase());
            }
        };
        avatar.setPreferredSize(new Dimension(36, 36));
        avatar.setBackground(SIDEBAR_TOP);

        JPanel infos = new JPanel();
        infos.setBackground(SIDEBAR_TOP);
        infos.setLayout(new BoxLayout(infos, BoxLayout.Y_AXIS));
        infos.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        String nomUser = Session.getUtilisateurConnecte() != null
            ? Session.getUtilisateurConnecte().getPrenom()
              + " " + Session.getUtilisateurConnecte().getNom()
            : "";
        String roleUser = Session.getUtilisateurConnecte() != null
            ? Session.getUtilisateurConnecte().getRole().replace("_", " ")
            : "";

        JLabel lblUser = new JLabel(nomUser);
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblUser.setForeground(Color.WHITE);

        JLabel lblRole = new JLabel(roleUser);
        lblRole.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblRole.setForeground(new Color(160, 160, 200));

        infos.add(lblUser);
        infos.add(Box.createVerticalStrut(2));
        infos.add(lblRole);

        userPanel.add(avatar, BorderLayout.WEST);
        userPanel.add(infos, BorderLayout.CENTER);

        // Séparateur
        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 255, 255, 30));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Bouton déconnexion option 2
        JPanel btnDeco = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 0)) {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(new Color(192, 57, 43, 60));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(new Color(192, 57, 43));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
            }
        };
        btnDeco.setOpaque(false);
        btnDeco.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        btnDeco.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDeco.setBorder(BorderFactory.createEmptyBorder(8, 0, 8, 0));

        JLabel iconDeco = new JLabel("\uf08b");
        iconDeco.setFont(fontAwesome.deriveFont(13f));
        iconDeco.setForeground(new Color(224, 96, 96));

        JLabel lblDeco = new JLabel("Deconnexion");
        lblDeco.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDeco.setForeground(new Color(224, 96, 96));

        btnDeco.add(iconDeco);
        btnDeco.add(lblDeco);

        btnDeco.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    BaseView.this,
                    "Voulez-vous vous deconnecter ?",
                    "Deconnexion",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) deconnecter();
            }
            public void mouseEntered(java.awt.event.MouseEvent e) { btnDeco.repaint(); }
            public void mouseExited(java.awt.event.MouseEvent e)  { btnDeco.repaint(); }
        });

        bas.add(userPanel);
        bas.add(Box.createVerticalStrut(10));
        bas.add(sep);
        bas.add(Box.createVerticalStrut(10));
        bas.add(btnDeco);

        sidebar.add(bas, BorderLayout.SOUTH);
        return sidebar;
    }

    private JPanel creerMenuItem(String icone, String texte) {
        JPanel item = new JPanel(new BorderLayout());
        item.setBackground(SIDEBAR_BG);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 46));
        item.setCursor(new Cursor(Cursor.HAND_CURSOR));
        item.setBorder(BorderFactory.createEmptyBorder(6, 12, 6, 12));

        Color[] couleurs = {
            new Color(74, 143, 196), new Color(30, 110, 74),
            new Color(122, 74, 30),  new Color(106, 30, 122),
            new Color(30, 110, 90),  new Color(122, 30, 30),
            new Color(30, 122, 90),  new Color(138, 90, 30)
        };
        int idx = Math.abs(texte.hashCode()) % couleurs.length;
        Color couleurIcone = couleurs[idx];

        JPanel iconBox = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2.setColor(couleurIcone);
                g2.fillRoundRect(0, 0, 30, 30, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(fontAwesome.deriveFont(13f));
                FontMetrics fm = g2.getFontMetrics();
                int x = (30 - fm.stringWidth(icone)) / 2;
                int y = (30 + fm.getAscent() - fm.getDescent()) / 2;
                g2.drawString(icone, x, y);
            }
        };
        iconBox.setPreferredSize(new Dimension(30, 30));
        iconBox.setOpaque(false);

        JLabel lbl = new JLabel(texte);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setForeground(new Color(200, 200, 230));
        lbl.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        item.add(iconBox, BorderLayout.WEST);
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