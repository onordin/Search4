package search4.ejb;


import search4.ejb.interfaces.LocalDisplayMovie;
import search4.ejb.interfaces.LocalEmail;
import search4.ejb.interfaces.LocalProvider;
import search4.entities.*;

import java.io.Serializable;
import java.util.ArrayList;
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

//@LocalBean
@Stateless
public class EmailEJB implements LocalEmail, Serializable{

    @EJB
    private LocalDisplayMovie displayMovieEJB;
    @EJB
    private LocalProvider providerEJB;

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

    public void sendNotificationMail(DisplayUserEntity user, DisplayMovieEntity movie) {
        String recipent = user.getEmail();
        String subject = movie.getTitle()+" avaliable";
        String link = "LINK ERROR";
        try {
            link = linkFactory(movie, user);
        } catch (Exception e) {
            System.out.println("SUPER ERROR "+e);
        }
        String message = "Hello "+user.getFirstName()+", the movie "+movie.getTitle()+
                " which you subscribed to has become available on a streaming service.\n"+
                "Please use the link(s) below to watch it.\n\n"+
                "Regards, Search4.\n\n"+
                link;
        sendMail(recipent, subject, message);
    }
    
    public void sendForgotPasswordMail(String mail, String password) {
        String recipent = mail;
        String subject = "Search4 password changed";
        String message = "Hello, your new random generated password is: " +password;
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

    public String linkFactory(DisplayMovieEntity movie, DisplayUserEntity userEntity) {
        String link = "";
        List<ServiceProviderLink> links = getAllServiceProviderLinksFor(movie);
        for (ServiceProviderLink spl : links) {
            link += spl.getName()+": "+spl.getUrl()+"\n";
        }
        return link;
    }

//    private List<ServiceProviderLink> getDesiredProviders(List<ServiceProviderLink> allLinks, DisplayUserEntity userEntity) {
//        List<ServiceProviderLink> desiredProviderLinks = new ArrayList<ServiceProviderLink>();
//        List<DisplayProviderEntity> desiredProviders = providerEJB.getAllForUser(userEntity.getId());
////        List<ServiceProviderLink> compareableProviders = displayProvidersToServiceProviderLinks(desiredProviders);
//        for (ServiceProviderLink providerLink : allLinks) {
//
//        }
//    }
//
//    private List<ServiceProviderLink> displayProvidersToServiceProviderLinks(List<DisplayProviderEntity> displayProviderEntities) {
//        List<ServiceProviderLink> serviceProviderLinks = new ArrayList<ServiceProviderLink>();
//        for (DisplayProviderEntity displayProvider : displayProviderEntities) {
//            ServiceProviderLink providerLink = new ServiceProviderLink();
//            providerLink.setName(displayProvider.getProvider()
//            serviceProviderLinks.add(providerLink);
//        }
//        return serviceProviderLinks;
//    }

    private List<ServiceProviderLink> getAllServiceProviderLinksFor(DisplayMovieEntity movie) {
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
        return links;
    }

	
}