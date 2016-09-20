package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;
import search4.helpers.APIKeyReader;
import search4.helpers.DateParser;
import search4.helpers.JSonHelper;
import search4.helpers.URLBuilder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.InputStream;
import java.net.URL;

//TODO interface?
@Stateless
public class DisplayMovieEJB {

    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;

    private MovieEntity getMovieData(Integer id) {
        return displayMovieDAOBean.getMovieData(id); //TODO anything we want to handle here?
    }

    //NEW METHOD: better to send the id to EJB and retrieve MovieEntity here. No point in bringing it to backing bean.
    public DisplayMovieEntity getDisplayMovie(Integer id) {
        DisplayMovieEntity displayMovieEntity = new DisplayMovieEntity();
        MovieEntity movieEntity = getMovieData(id);
        isGuideboxSet(movieEntity); //Check if guidbox id is set. If not, set it.
        setStreamingServices(displayMovieEntity, movieEntity.getGuideboxId()); //Retrieve streaming services from guidebox
        setTmdbInfo(displayMovieEntity, movieEntity.getTmdbId()); //Retrieve movie information (description, poster etc) from TMDB
        return displayMovieEntity;
    }

    private void isGuideboxSet(MovieEntity movieEntity) {
        //TODO check if guidebox ID is set in movieEntity, otherwise make call and set it
    }

    private void setStreamingServices(DisplayMovieEntity displayMovieEntity, Integer guideBoxId) {
        //TODO retrieve streaming services from guidebox
    }

    private void setTmdbInfo(DisplayMovieEntity displayMovieEntity, Integer tmdbId) {
        DateParser dateParser = new DateParser();
        JSonHelper jSonHelper = new JSonHelper();
        URLBuilder urlBuilder = new URLBuilder();
        
        String url = urlBuilder.tmdbUrl(tmdbId);

        JsonObject jsonObject = jSonHelper.getObject(url);
        if (jsonObject != null) {
            displayMovieEntity.setTitle(jsonObject.getString("original_title"));
            displayMovieEntity.setDescription(jsonObject.getString("overview"));
            displayMovieEntity.setDate(dateParser.getDateFromString(jsonObject.getString("release_date")));
            displayMovieEntity.setPosterUrl("http://image.tmdb.org/t/p/w185"+jsonObject.getString("poster_path"));
        }
        //TODO else throw error? or somehow send a message the operation failed
    }
}
