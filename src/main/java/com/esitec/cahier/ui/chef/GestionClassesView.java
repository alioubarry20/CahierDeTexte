package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Classe;
import com.esitec.cahier.service.ClasseService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class GestionClassesView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private ClasseService service = new ClasseService();

    public GestionClassesView() {
        super("Gestion des classes");
        initialiserUI();
        chargerClasses();
    }

    private void initialiserUI() {
        setLayout(new BorderLayout());
        add(creerHeader("🏫 Gestion des classes"), BorderLayout.NORTH);

        // Tableau
        String[] colonnes = {"ID", "Nom", "Filière", "Niveau"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(scroll, BorderLayout.CENTER);

        // Boutons
        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        panneauBoutons.setBackground(COULEUR_FOND);

        JButton btnAjouter = creerBouton("➕ Ajouter", COULEUR_SECONDAIRE);
        JButton btnSupprimer = creerBouton("🗑 Supprimer", COULEUR_DANGER);

        btnAjouter.addActionListener(e -> ouvrirFormulaireAjout());
        btnSupprimer.addActionListener(e -> supprimerClasse());

        panneauBoutons.add(btnAjouter);
        panneauBoutons.add(btnSupprimer);

        add(panneauBoutons, BorderLayout.SOUTH);
    }

    private void chargerClasses() {
        try {
            modeleTableau.setRowCount(0);
            List<Classe> liste = service.listerTous();
            for (Classe c : liste) {
                modeleTableau.addRow(new Object[]{
                    c.getId(), c.getNom(), c.getFiliere(), c.getNiveau()
                });
            }
        } catch (Exception e) {
            afficherErreur("Erreur chargement : " + e.getMessage());
        }
    }

    private void ouvrirFormulaireAjout() {
        // Formulaire simple avec JOptionPane
        JTextField champNom = new JTextField();
        JTextField champFiliere = new JTextField();
        JTextField champNiveau = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.add(new JLabel("Nom :")); panel.add(champNom);
        panel.add(new JLabel("Filière :")); panel.add(champFiliere);
        panel.add(new JLabel("Niveau :")); panel.add(champNiveau);

        int result = JOptionPane.showConfirmDialog(this, panel,
            "Ajouter une classe", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Classe c = new Classe();
                c.setNom(champNom.getText().trim());
                c.setFiliere(champFiliere.getText().trim());
                c.setNiveau(champNiveau.getText().trim());
                service.ajouter(c);
                afficherSucces("Classe ajoutée !");
                chargerClasses();
            } catch (Exception e) {
                afficherErreur("Erreur : " + e.getMessage());
            }
        }
    }

    private void supprimerClasse() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) {
            afficherErreur("Sélectionnez une classe !");
            return;
        }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer cette classe ?", "Confirmation",
            JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                service.supprimer(id);
                afficherSucces("Classe supprimée !");
                chargerClasses();
            } catch (Exception e) {
                afficherErreur("Erreur : " + e.getMessage());
            }
        }
    }

    public void rafraichir() {
        chargerClasses();
    }
}