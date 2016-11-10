package search4.ejb.interfaces;

import search4.entities.DisplayMovieEntity;
import search4.entities.DisplayUserEntity;
import search4.entities.MovieEntity;

import javax.ejb.Local;
import javax.mail.Session;

@Local
public interface LocalEmail {

    Session setUpMail();

    void sendNotificationMail(DisplayUserEntity user, DisplayMovieEntity movie);

    void sendForgotPasswordMail(String mail, String password);
    
    String linkFactory(DisplayMovieEntity movie, DisplayUserEntity userEntity);
}
