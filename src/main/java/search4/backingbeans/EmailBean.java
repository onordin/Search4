package search4.backingbeans;

import search4.ejb.EmailEJB;
import search4.entities.DisplayMovieEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.ServiceProviderLink;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named(value="emailBean")
@SessionScoped
public class EmailBean implements Serializable{


    @EJB
    private EmailEJB emailEJB;

    public void sendNotification(DisplayUserEntity user, DisplayMovieEntity movie) {
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
//        emailEJB.sendNotificationMail(user, link, movie);
    }
}
