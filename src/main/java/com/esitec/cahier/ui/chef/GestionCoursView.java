package com.esitec.cahier.ui.chef;

import com.esitec.cahier.model.Classe;
import com.esitec.cahier.model.Cours;
import com.esitec.cahier.model.Enseignant;
import com.esitec.cahier.model.Utilisateur;
import com.esitec.cahier.service.ClasseService;
import com.esitec.cahier.service.CoursService;
import com.esitec.cahier.service.UtilisateurService;
import com.esitec.cahier.ui.BaseView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class GestionCoursView extends BaseView {

    private JTable tableau;
    private DefaultTableModel modeleTableau;
    private CoursService coursService = new CoursService();
    private UtilisateurService utilisateurService = new UtilisateurService();
    private ClasseService classeService = new ClasseService();

    public GestionCoursView() {
        super("Gestion des cours");
    }

    public JPanel creerPanneau() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(240, 242, 248));

        String[] colonnes = {"ID", "Intitule", "Volume horaire", "Enseignant", "Classe"};
        modeleTableau = new DefaultTableModel(colonnes, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tableau = new JTable(modeleTableau);
        tableau.setRowHeight(30);
        tableau.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tableau.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableau.setGridColor(new Color(220, 220, 220));

        JScrollPane scroll = new JScrollPane(tableau);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        panel.add(scroll, BorderLayout.CENTER);

        JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        boutons.setBackground(new Color(240, 242, 248));

        JButton btnAjouter   = creerBouton("+ Ajouter",  COULEUR_SECONDAIRE);
        JButton btnSupprimer = creerBouton("Supprimer",  COULEUR_DANGER);

        btnAjouter.addActionListener(e -> ouvrirFormulaireAjout());
        btnSupprimer.addActionListener(e -> supprimerCours());

        boutons.add(btnAjouter);
        boutons.add(btnSupprimer);
        panel.add(boutons, BorderLayout.SOUTH);

        chargerCours();
        return panel;
    }

    private void chargerCours() {
        try {
            modeleTableau.setRowCount(0);
            List<Cours> liste = coursService.listerTous();
            for (Cours c : liste) {
                modeleTableau.addRow(new Object[]{
                    c.getId(),
                    c.getIntitule(),
                    c.getVolumeHoraire() + "h",
                    c.getEnseignant().getNomComplet(),
                    c.getClasse().getNom()
                });
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void ouvrirFormulaireAjout() {
        try {
            List<Utilisateur> tous = utilisateurService.listerTous();
            List<Classe> classes   = classeService.listerTous();

            List<Utilisateur> enseignants = tous.stream()
                .filter(u -> u.getRole().equals("ENSEIGNANT"))
                .collect(Collectors.toList());

            if (enseignants.isEmpty()) { afficherErreur("Aucun enseignant !"); return; }
            if (classes.isEmpty())     { afficherErreur("Aucune classe !"); return; }

            JTextField champIntitule = new JTextField();
            JTextField champVolume   = new JTextField();
            JComboBox<Utilisateur> comboEns   = new JComboBox<>(enseignants.toArray(new Utilisateur[0]));
            JComboBox<Classe>      comboClasse = new JComboBox<>(classes.toArray(new Classe[0]));

            JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
            panel.add(new JLabel("Intitule :"));         panel.add(champIntitule);
            panel.add(new JLabel("Volume horaire (h) :")); panel.add(champVolume);
            panel.add(new JLabel("Enseignant :"));       panel.add(comboEns);
            panel.add(new JLabel("Classe :"));           panel.add(comboClasse);

            int result = JOptionPane.showConfirmDialog(this, panel,
                "Ajouter un cours", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                Cours c = new Cours();
                c.setIntitule(champIntitule.getText().trim());
                c.setVolumeHoraire(Integer.parseInt(champVolume.getText().trim()));
                c.setEnseignant((Enseignant) comboEns.getSelectedItem());
                c.setClasse((Classe) comboClasse.getSelectedItem());
                coursService.ajouter(c);
                afficherSucces("Cours ajoute !");
                chargerCours();
            }
        } catch (Exception e) {
            afficherErreur("Erreur : " + e.getMessage());
        }
    }

    private void supprimerCours() {
        int ligne = tableau.getSelectedRow();
        if (ligne == -1) { afficherErreur("Selectionnez un cours !"); return; }
        int id = (int) modeleTableau.getValueAt(ligne, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Supprimer ce cours ?", "Confirmation", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                coursService.supprimer(id);
                afficherSucces("Cours supprime !");
                chargerCours();
            } catch (Exception e) {
                afficherErreur("Erreur : " + e.getMessage());
            }
        }
    }
}