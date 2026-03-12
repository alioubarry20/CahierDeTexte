package com.esitec.cahier.util;

import com.esitec.cahier.model.FicheSuivi;
import com.esitec.cahier.model.Seance;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public class PdfExportUtil {

    // Formatteur de date
    private static final DateTimeFormatter FORMAT_DATE = 
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Couleurs
    private static final BaseColor COULEUR_HEADER = new BaseColor(31, 73, 125);
    private static final BaseColor COULEUR_LIGNE_PAIRE = new BaseColor(220, 230, 241);

    // =============================================
    // EXPORTER LA FICHE DE SUIVI EN PDF
    // =============================================
    public static void exporter(FicheSuivi fiche, String cheminFichier) {
        Document document = new Document(PageSize.A4);
        try {
            PdfWriter.getInstance(document, new FileOutputStream(cheminFichier));
            document.open();

            // 1. Titre
            ajouterTitre(document, fiche);

            // 2. Informations générales
            ajouterInfosGenerales(document, fiche);

            // 3. Tableau des séances
            ajouterTableauSeances(document, fiche);

            // 4. Résumé
            ajouterResume(document, fiche);

            document.close();
            System.out.println(" PDF généré : " + cheminFichier);

        } catch (Exception e) {
            System.out.println(" Erreur génération PDF : " + e.getMessage());
        }
    }

    // =============================================
    // TITRE DU DOCUMENT
    // =============================================
    private static void ajouterTitre(Document doc, FicheSuivi fiche) 
        throws DocumentException {

        Font fontTitre = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD, 
                                   BaseColor.WHITE);
        Font fontSousTitre = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, 
                                       BaseColor.WHITE);

        // Rectangle coloré pour le titre
        PdfPTable tableauTitre = new PdfPTable(1);
        tableauTitre.setWidthPercentage(100);

        PdfPCell cellTitre = new PdfPCell();
        cellTitre.setBackgroundColor(COULEUR_HEADER);
        cellTitre.setPadding(15);
        cellTitre.setBorder(Rectangle.NO_BORDER);

        cellTitre.addElement(new Phrase("FICHE DE SUIVI PÉDAGOGIQUE", fontTitre));
        cellTitre.addElement(new Phrase("ESITEC - " + fiche.getCours().getIntitule(), 
                                         fontSousTitre));
        tableauTitre.addCell(cellTitre);

        doc.add(tableauTitre);
        doc.add(Chunk.NEWLINE);
    }

    // =============================================
    // INFORMATIONS GÉNÉRALES
    // =============================================
    private static void ajouterInfosGenerales(Document doc, FicheSuivi fiche) 
        throws DocumentException {

        Font fontLabel = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);
        Font fontValeur = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);

        PdfPTable tableau = new PdfPTable(2);
        tableau.setWidthPercentage(100);
        tableau.setSpacingBefore(10);

        // Ligne 1 — Enseignant
        ajouterLigneInfo(tableau, "Enseignant :", 
            fiche.getEnseignant().getNomComplet(), fontLabel, fontValeur);

        // Ligne 2 — Cours
        ajouterLigneInfo(tableau, "Cours :", 
            fiche.getCours().getIntitule(), fontLabel, fontValeur);

        // Ligne 3 — Classe
        ajouterLigneInfo(tableau, "Classe :", 
            fiche.getCours().getClasse().toString(), fontLabel, fontValeur);

        // Ligne 4 — Date génération
        ajouterLigneInfo(tableau, "Date de génération :", 
            fiche.getDateGeneration().format(FORMAT_DATE), fontLabel, fontValeur);

        doc.add(tableau);
        doc.add(Chunk.NEWLINE);
    }

    // =============================================
    // TABLEAU DES SEANCES
    // =============================================
    private static void ajouterTableauSeances(Document doc, FicheSuivi fiche) 
        throws DocumentException {

        Font fontEntete = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD, 
                                    BaseColor.WHITE);
        Font fontCellule = new Font(Font.FontFamily.HELVETICA, 9, Font.NORMAL);

        // Titre section
        Font fontSection = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, 
                                     COULEUR_HEADER);
        doc.add(new Paragraph("Détail des séances", fontSection));
        doc.add(Chunk.NEWLINE);

        // Tableau avec 5 colonnes
        PdfPTable tableau = new PdfPTable(5);
        tableau.setWidthPercentage(100);
        tableau.setWidths(new float[]{2f, 1.5f, 1.5f, 4f, 2f});

        // En-têtes
        String[] entetes = {"Date", "Heure", "Durée", "Contenu", "Statut"};
        for (String entete : entetes) {
            PdfPCell cell = new PdfPCell(new Phrase(entete, fontEntete));
            cell.setBackgroundColor(COULEUR_HEADER);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tableau.addCell(cell);
        }

        // Lignes des séances
        int i = 0;
        for (Seance s : fiche.getSeances()) {
            BaseColor couleurLigne = (i % 2 == 0) ? BaseColor.WHITE : COULEUR_LIGNE_PAIRE;

            ajouterCellule(tableau, s.getDate().format(FORMAT_DATE), 
                          fontCellule, couleurLigne);
            ajouterCellule(tableau, s.getHeure().toString(), 
                          fontCellule, couleurLigne);
            ajouterCellule(tableau, s.getDuree() + " min", 
                          fontCellule, couleurLigne);
            ajouterCellule(tableau, s.getContenu(), 
                          fontCellule, couleurLigne);
            ajouterCellule(tableau, s.getStatut(), 
                          fontCellule, couleurLigne);
            i++;
        }

        doc.add(tableau);
        doc.add(Chunk.NEWLINE);
    }

    // =============================================
    // RÉSUMÉ FINAL
    // =============================================
    private static void ajouterResume(Document doc, FicheSuivi fiche) 
        throws DocumentException {

        Font fontSection = new Font(Font.FontFamily.HELVETICA, 13, Font.BOLD, 
                                     COULEUR_HEADER);
        Font fontNormal = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL);
        Font fontGras = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD);

        doc.add(new Paragraph("Résumé", fontSection));
        doc.add(Chunk.NEWLINE);

        PdfPTable tableau = new PdfPTable(2);
        tableau.setWidthPercentage(60);
        tableau.setHorizontalAlignment(Element.ALIGN_LEFT);

        ajouterLigneInfo(tableau, "Volume horaire total :", 
            fiche.getCours().getVolumeHoraire() + "h", fontGras, fontNormal);
        ajouterLigneInfo(tableau, "Heures effectuées :", 
            fiche.getHeuresEffectuees() + "h", fontGras, fontNormal);
        ajouterLigneInfo(tableau, "Heures restantes :", 
            fiche.getHeuresRestantes() + "h", fontGras, fontNormal);
        ajouterLigneInfo(tableau, "Taux d'avancement :", 
            String.format("%.1f%%", fiche.getTauxAvancement()), fontGras, fontNormal);

        doc.add(tableau);
    }

    // =============================================
    // MÉTHODES UTILITAIRES PRIVÉES
    // =============================================
    private static void ajouterLigneInfo(PdfPTable tableau, String label, 
        String valeur, Font fontLabel, Font fontValeur) {

        PdfPCell cellLabel = new PdfPCell(new Phrase(label, fontLabel));
        cellLabel.setBorder(Rectangle.NO_BORDER);
        cellLabel.setPadding(5);
        tableau.addCell(cellLabel);

        PdfPCell cellValeur = new PdfPCell(new Phrase(valeur, fontValeur));
        cellValeur.setBorder(Rectangle.NO_BORDER);
        cellValeur.setPadding(5);
        tableau.addCell(cellValeur);
    }

    private static void ajouterCellule(PdfPTable tableau, String texte, 
        Font font, BaseColor couleur) {

        PdfPCell cell = new PdfPCell(new Phrase(texte != null ? texte : "", font));
        cell.setBackgroundColor(couleur);
        cell.setPadding(6);
        tableau.addCell(cell);
    }
}