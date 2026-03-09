package com.esitec.cahier.util;

import com.esitec.cahier.model.FicheSuivi;
import com.esitec.cahier.model.Seance;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

public class ExcelExportUtil {

    private static final DateTimeFormatter FORMAT_DATE =
        DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // =============================================
    // EXPORTER LA FICHE DE SUIVI EN EXCEL
    // =============================================
    public static void exporter(FicheSuivi fiche, String cheminFichier) {
        try (XSSFWorkbook classeur = new XSSFWorkbook()) {

            // Créer la feuille
            XSSFSheet feuille = classeur.createSheet("Fiche de suivi");

            // Styles
            CellStyle styleEntete = creerStyleEntete(classeur);
            CellStyle styleTitre  = creerStyleTitre(classeur);
            CellStyle styleNormal = creerStyleNormal(classeur);
            CellStyle stylePaire  = creerStyleLignePaire(classeur);

            int ligne = 0;

            // 1. Titre
            ligne = ajouterTitre(feuille, styleTitre, fiche, ligne);

            // 2. Infos générales
            ligne = ajouterInfosGenerales(feuille, styleNormal, fiche, ligne);

            // 3. Tableau séances
            ligne = ajouterTableauSeances(feuille, styleEntete, 
                                          styleNormal, stylePaire, fiche, ligne);

            // 4. Résumé
            ajouterResume(feuille, styleNormal, fiche, ligne);

            // Ajuster largeur colonnes automatiquement
            for (int i = 0; i < 5; i++) {
                feuille.autoSizeColumn(i);
            }

            // Sauvegarder le fichier
            FileOutputStream fichierSortie = new FileOutputStream(cheminFichier);
            classeur.write(fichierSortie);
            fichierSortie.close();

            System.out.println("✅ Excel généré : " + cheminFichier);

        } catch (Exception e) {
            System.out.println("❌ Erreur génération Excel : " + e.getMessage());
        }
    }

    // =============================================
    // TITRE
    // =============================================
    private static int ajouterTitre(XSSFSheet feuille, CellStyle style,
        FicheSuivi fiche, int ligne) {

        Row row = feuille.createRow(ligne++);
        Cell cell = row.createCell(0);
        cell.setCellValue("FICHE DE SUIVI PÉDAGOGIQUE - ESITEC");
        cell.setCellStyle(style);

        // Fusionner les cellules A1 à E1
        feuille.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        Row row2 = feuille.createRow(ligne++);
        Cell cell2 = row2.createCell(0);
        cell2.setCellValue(fiche.getCours().getIntitule());
        cell2.setCellStyle(style);
        feuille.addMergedRegion(new CellRangeAddress(1, 1, 0, 4));

        ligne++; // ligne vide
        return ligne;
    }

    // =============================================
    // INFORMATIONS GÉNÉRALES
    // =============================================
    private static int ajouterInfosGenerales(XSSFSheet feuille, CellStyle style,
        FicheSuivi fiche, int ligne) {

        ajouterLigneInfo(feuille, style, ligne++, 
            "Enseignant", fiche.getEnseignant().getNomComplet());
        ajouterLigneInfo(feuille, style, ligne++, 
            "Cours", fiche.getCours().getIntitule());
        ajouterLigneInfo(feuille, style, ligne++, 
            "Classe", fiche.getCours().getClasse().toString());
        ajouterLigneInfo(feuille, style, ligne++, 
            "Date de génération", fiche.getDateGeneration().format(FORMAT_DATE));

        ligne++; // ligne vide
        return ligne;
    }

    // =============================================
    // TABLEAU DES SEANCES
    // =============================================
    private static int ajouterTableauSeances(XSSFSheet feuille,
        CellStyle styleEntete, CellStyle styleNormal,
        CellStyle stylePaire, FicheSuivi fiche, int ligne) {

        // En-têtes colonnes
        Row rowEntete = feuille.createRow(ligne++);
        String[] entetes = {"Date", "Heure", "Durée (min)", "Contenu", "Statut"};
        for (int i = 0; i < entetes.length; i++) {
            Cell cell = rowEntete.createCell(i);
            cell.setCellValue(entetes[i]);
            cell.setCellStyle(styleEntete);
        }

        // Lignes des séances
        int i = 0;
        for (Seance s : fiche.getSeances()) {
            Row row = feuille.createRow(ligne++);
            CellStyle style = (i % 2 == 0) ? styleNormal : stylePaire;

            creerCellule(row, 0, s.getDate().format(FORMAT_DATE), style);
            creerCellule(row, 1, s.getHeure().toString(), style);
            creerCellule(row, 2, String.valueOf(s.getDuree()), style);
            creerCellule(row, 3, s.getContenu(), style);
            creerCellule(row, 4, s.getStatut(), style);
            i++;
        }

        ligne++; // ligne vide
        return ligne;
    }

    // =============================================
    // RÉSUMÉ
    // =============================================
    private static void ajouterResume(XSSFSheet feuille, CellStyle style,
        FicheSuivi fiche, int ligne) {

        ajouterLigneInfo(feuille, style, ligne++, 
            "Volume horaire total", fiche.getCours().getVolumeHoraire() + "h");
        ajouterLigneInfo(feuille, style, ligne++, 
            "Heures effectuées", fiche.getHeuresEffectuees() + "h");
        ajouterLigneInfo(feuille, style, ligne++, 
            "Heures restantes", fiche.getHeuresRestantes() + "h");
        ajouterLigneInfo(feuille, style, ligne++, 
            "Taux d'avancement", String.format("%.1f%%", fiche.getTauxAvancement()));
    }

    // =============================================
    // STYLES
    // =============================================
    private static CellStyle creerStyleTitre(XSSFWorkbook classeur) {
        CellStyle style = classeur.createCellStyle();
        XSSFFont font = classeur.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle creerStyleEntete(XSSFWorkbook classeur) {
        CellStyle style = classeur.createCellStyle();
        XSSFFont font = classeur.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setAlignment(HorizontalAlignment.CENTER);
        return style;
    }

    private static CellStyle creerStyleNormal(XSSFWorkbook classeur) {
        CellStyle style = classeur.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private static CellStyle creerStyleLignePaire(XSSFWorkbook classeur) {
        CellStyle style = classeur.createCellStyle();
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    // =============================================
    // MÉTHODES UTILITAIRES
    // =============================================
    private static void ajouterLigneInfo(XSSFSheet feuille, CellStyle style,
        int ligne, String label, String valeur) {

        Row row = feuille.createRow(ligne);
        Cell cellLabel = row.createCell(0);
        cellLabel.setCellValue(label);
        cellLabel.setCellStyle(style);

        Cell cellValeur = row.createCell(1);
        cellValeur.setCellValue(valeur);
        cellValeur.setCellStyle(style);
    }

    private static void creerCellule(Row row, int colonne, 
        String valeur, CellStyle style) {

        Cell cell = row.createCell(colonne);
        cell.setCellValue(valeur != null ? valeur : "");
        cell.setCellStyle(style);
    }
}



// commande pour compiler dans le cmd :mvn clean install
