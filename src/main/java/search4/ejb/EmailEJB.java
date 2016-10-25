package search4.ejb;


import search4.entities.DisplayMovieEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.MovieEntity;
import search4.entities.ServiceProviderLink;

import java.util.List;
import java.util.Properties;

import javax.ejb.EJB;
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

    @EJB
    private DisplayMovieEJB displayMovieEJB;

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

    public void sendNotificationMail(DisplayUserEntity user, MovieEntity movie) {
        String recipent = user.getEmail();
        String subject = movie.getTitle()+" avaliable";
        String link = linkFactory(displayMovieEJB.createDisplayMovie(movie));
        String message = "Hello "+user.getFirstName()+", the movie "+movie.getTitle()+
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

    //TODO in helper or factory file/package? feels like the wrong place -- HELPER
    public String linkFactory(DisplayMovieEntity movie) {
        String link = "";
        List<ServiceProviderLink> links = movie.getProviderListAndroidFree();
        links.addAll(movie.getProviderListAndroidPurchase());
        links.addAll(movie.getProviderListAndroidSubscription());
        links.addAll(movie.getProviderListAndroidTvEverywhere());
        links.addAll(movie.getProviderListAndroidFree());
        links.addAll(movie.getProviderListIOSPurchase());
        links.addAll(movie.getProviderListIOSFree());
        links.addAll(movie.getProviderListIOSSubscription());
        links.addAll(movie.getProviderListIOSTvEverywhere());
        links.addAll(movie.getProviderListIOSTvEverywhere());
        links.addAll(movie.getProviderListOther());
        links.addAll(movie.getProviderListWebFree());
        links.addAll(movie.getProviderListWebPurchase());
        links.addAll(movie.getProviderListWebSubscription());
        links.addAll(movie.getProviderListWebTvEverywhere());
        for (ServiceProviderLink spl : links) {
            link += spl.getName()+": "+spl.getUrl()+"\n";
        }
        return link;
    }
}