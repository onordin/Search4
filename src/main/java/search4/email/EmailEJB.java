package search4.email;


import search4.entities.DisplayUserEntity;

import java.util.List;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Stateless
@LocalBean
public class EmailEJB {

    private final String USERNAME = "searchfourmail@gmail.com";

    public Session setUpMail() {

        final String password = "4search4";

        Properties properties = new Properties();
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USERNAME, password);
            }
        });
        return session;
    }

    public void sendNotificationMail(DisplayUserEntity user, String link, String movieTitle) {
        String recipent = user.getEmail();
        String subject = movieTitle+" avaliable";
        String message = "Hello "+user.getFirstName()+", the movie "+movieTitle+
                " which you subscribed to has become available on a streaming service.\n"+
                "Please use the link(s) below to watch it.\n\n"+
                "Regards, Search4.\n\n"+
                link;
        sendMail(recipent, subject, message);
    }

    private void sendMail(String recipent, String subject, String content) {
        try {
            Message message = new MimeMessage(setUpMail());
            message.setFrom(new InternetAddress(USERNAME));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipent));
            message.setSubject(subject);
            message.setText(content);
            Transport.send(message);
        } catch (MessagingException e) {
            System.err.println("ERROR "+e);
        }
    }

//    public boolean controlUserMail(String email) {
//        boolean validMail;
//        try {
//            Message message = new MimeMessage(setUpMail());
//            message.setFrom(new InternetAddress("fiskenaetet@gmail.com"));
//            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
//            message.setSubject("Välkommen till Fiskenätet");
//            message.setText("Tack för din registrering hos Fiskenätet!"
//                    + "\n" + "Detta är ett verifikationsmejl."
//                    + "\n" + "Lycka till med dina framtida auktioner."
//                    + "\n" + ""
//                    + "\n" + "Hälsningar Fiskenätet!");
//            Transport.send(message);
//            validMail = true;
//            log.info("Called method 'controlUserMail' that sent a welcome mail to " +email);
//        } catch(MessagingException e){
//            log.warning("Warning in method 'controlUserMail'. MessagingException: " +e);
//            validMail = false;
//        }
//        return validMail;
//    }
}