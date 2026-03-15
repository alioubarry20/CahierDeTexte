package com.esitec.cahier.util;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class MailService {

    private static final String EMAIL    = "alioubarryprivate10@gmail.com";
    private static final String PASSWORD = "pvxjyujyejyzxcen";

    private static MailService instance;

    private MailService() {}

    public static MailService getInstance() {
        if (instance == null) instance = new MailService();
        return instance;
    }

private javax.mail.Session creerSession() {
    Properties props = new Properties();
    props.put("mail.smtp.auth",            "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host",            "smtp.gmail.com");
    props.put("mail.smtp.port",            "587");
    props.put("mail.smtp.ssl.trust",       "smtp.gmail.com");

    return javax.mail.Session.getInstance(props, new Authenticator() {
        protected PasswordAuthentication getPasswordAuthentication() {
            return new PasswordAuthentication(EMAIL, PASSWORD);
        }
    });
}
    public void envoyerMail(String destinataire, String sujet, String corps) {
        new Thread(() -> {
            try {
                javax.mail.Session session = creerSession();
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress(EMAIL, "ESITEC - CahierDeTexte"));
                message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(destinataire));
                message.setSubject(sujet);
                message.setContent(corps, "text/html; charset=utf-8");
                Transport.send(message);
            } catch (Exception e) {
                System.err.println("Erreur envoi mail : " + e.getMessage());
            }
        }).start();
    }

    // ── Templates mails ──────────────────────────────────

    public void mailCoursAssigne(String emailEns, String nomEns,
                                  String nomCours, String nomClasse,
                                  int volumeHoraire) {
        String sujet = "ESITEC - Nouveau cours assigne : " + nomCours;
        String corps = "<div style='font-family:Segoe UI,sans-serif;max-width:600px;margin:auto;'>"
            + "<div style='background:#2d2d50;padding:20px;text-align:center;'>"
            + "<h2 style='color:white;margin:0;'>ESITEC</h2>"
            + "<p style='color:#9898cc;margin:4px 0 0;font-size:12px;'>Cahier de Texte Numerique</p>"
            + "</div>"
            + "<div style='padding:30px;background:#f9f9f9;'>"
            + "<h3 style='color:#2d2d50;'>Bonjour " + nomEns + ",</h3>"
            + "<p>Un nouveau cours vous a ete assigne :</p>"
            + "<div style='background:white;border-left:4px solid #0078d7;"
            + "padding:16px;border-radius:4px;margin:20px 0;'>"
            + "<p style='margin:0 0 8px;'><b>Cours :</b> " + nomCours + "</p>"
            + "<p style='margin:0 0 8px;'><b>Classe :</b> " + nomClasse + "</p>"
            + "<p style='margin:0;'><b>Volume horaire :</b> " + volumeHoraire + "h</p>"
            + "</div>"
            + "<p>Connectez-vous sur l'application pour voir les details.</p>"
            + "</div>"
            + "<div style='background:#f0f0f0;padding:12px;text-align:center;"
            + "font-size:11px;color:#888;'>"
            + "ESITEC - SUP DE CO Dakar</div></div>";
        envoyerMail(emailEns, sujet, corps);
    }

    public void mailClasseAssignee(String emailResp, String nomResp,
                                    String nomClasse) {
        String sujet = "ESITEC - Classe assignee : " + nomClasse;
        String corps = "<div style='font-family:Segoe UI,sans-serif;max-width:600px;margin:auto;'>"
            + "<div style='background:#1e4a32;padding:20px;text-align:center;'>"
            + "<h2 style='color:white;margin:0;'>ESITEC</h2>"
            + "<p style='color:#7abf96;margin:4px 0 0;font-size:12px;'>Cahier de Texte Numerique</p>"
            + "</div>"
            + "<div style='padding:30px;background:#f9f9f9;'>"
            + "<h3 style='color:#1e4a32;'>Bonjour " + nomResp + ",</h3>"
            + "<p>Une classe vous a ete assignee :</p>"
            + "<div style='background:white;border-left:4px solid #00a060;"
            + "padding:16px;border-radius:4px;margin:20px 0;'>"
            + "<p style='margin:0;'><b>Classe :</b> " + nomClasse + "</p>"
            + "</div>"
            + "<p>Connectez-vous sur l'application pour voir les details.</p>"
            + "</div>"
            + "<div style='background:#f0f0f0;padding:12px;text-align:center;"
            + "font-size:11px;color:#888;'>"
            + "ESITEC - SUP DE CO Dakar</div></div>";
        envoyerMail(emailResp, sujet, corps);
    }

    public void mailNouvelleSeance(String emailResp, String nomResp,
                                    String nomCours, String nomEns,
                                    String date, String heure,
                                    String contenu) {
        String sujet = "ESITEC - Nouvelle seance ajoutee : " + nomCours;
        String corps = "<div style='font-family:Segoe UI,sans-serif;max-width:600px;margin:auto;'>"
            + "<div style='background:#1a3c5e;padding:20px;text-align:center;'>"
            + "<h2 style='color:white;margin:0;'>ESITEC</h2>"
            + "<p style='color:#7aafd4;margin:4px 0 0;font-size:12px;'>Cahier de Texte Numerique</p>"
            + "</div>"
            + "<div style='padding:30px;background:#f9f9f9;'>"
            + "<h3 style='color:#1a3c5e;'>Bonjour " + nomResp + ",</h3>"
            + "<p>Une nouvelle seance a ete ajoutee et necessite votre validation :</p>"
            + "<div style='background:white;border-left:4px solid #ff8c00;"
            + "padding:16px;border-radius:4px;margin:20px 0;'>"
            + "<p style='margin:0 0 8px;'><b>Cours :</b> " + nomCours + "</p>"
            + "<p style='margin:0 0 8px;'><b>Enseignant :</b> " + nomEns + "</p>"
            + "<p style='margin:0 0 8px;'><b>Date :</b> " + date + "</p>"
            + "<p style='margin:0 0 8px;'><b>Heure :</b> " + heure + "</p>"
            + "<p style='margin:0;'><b>Contenu :</b> " + contenu + "</p>"
            + "</div>"
            + "<p>Connectez-vous sur l'application pour valider ou rejeter cette seance.</p>"
            + "</div>"
            + "<div style='background:#f0f0f0;padding:12px;text-align:center;"
            + "font-size:11px;color:#888;'>"
            + "ESITEC - SUP DE CO Dakar</div></div>";
        envoyerMail(emailResp, sujet, corps);
    }
}