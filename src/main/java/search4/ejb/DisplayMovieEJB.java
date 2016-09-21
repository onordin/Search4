package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.ejb.interfaces.LocalDisplayMovie;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;
import search4.entities.ServiceProviderLink;
import search4.entities.enums.ServiceProviderType;
import search4.exceptions.DataNotFoundException;
import search4.exceptions.UnregisteredProviderException;
import search4.helpers.DateParser;
import search4.helpers.JSonHelper;
import search4.helpers.URLBuilder;

import javax.ejb.EJB;
import javax.ejb.EJBTransactionRolledbackException;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import javax.ws.rs.BadRequestException;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DisplayMovieEJB implements LocalDisplayMovie {

    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;

    private MovieEntity getMovieData(Integer id) throws Exception{
        return displayMovieDAOBean.getMovieData(id);
    }

    //NEW METHOD: better to send the id to EJB and retrieve MovieEntity here. No point in bringing it to backing bean.
    public DisplayMovieEntity getDisplayMovie(Integer id) throws Exception{
        DisplayMovieEntity displayMovieEntity = new DisplayMovieEntity();
        MovieEntity movieEntity = getMovieData(id);
        setGuideboxId(movieEntity); //Check if guidbox id is set. If not, set it.
        setStreamingServices(displayMovieEntity, movieEntity.getGuideboxId()); //Retrieve streaming services from guidebox
        setTmdbInfo(displayMovieEntity, movieEntity.getTmdbId()); //Retrieve movie information (description, poster etc) from TMDB
        return displayMovieEntity;
    }

    private void setGuideboxId(MovieEntity movieEntity) {
        if (movieEntity.getGuideboxId() < 1) { //TODO check what lowest guidebox id is
            //TODO if below throws exception, do it here to
            movieEntity.setGuideboxId(getGuideboxId(movieEntity.getTmdbId()));
        }
    }

    private Integer getGuideboxId(Integer tmdbId) {
        Integer guideboxId = 0; //TODO find lowest
        JSonHelper jSonHelper = new JSonHelper();
        URLBuilder urlBuilder = new URLBuilder();

        String url = urlBuilder.guideboxUrl(tmdbId, "/search/movie/id/themoviedb/");

        JsonObject jsonObject = jSonHelper.getObject(url);
        if (jsonObject != null && !jsonObject.isEmpty()) {
            guideboxId = jsonObject.getInt("id");
        }
        else {
            //TODO throw exception? or return 0?
        }
        return guideboxId;
    }

    private void setStreamingServices(DisplayMovieEntity displayMovieEntity, Integer guideBoxId) throws UnregisteredProviderException, DataNotFoundException{
        JSonHelper jSonHelper = new JSonHelper();
        URLBuilder urlBuilder = new URLBuilder();

        String url = urlBuilder.guideboxUrl(guideBoxId, "/movie/");

        List<JsonObject> objectList = jSonHelper.getObjectList(url, "purchase_web_sources");
        List<ServiceProviderLink> serviceProviderLinks = new ArrayList<ServiceProviderLink>();

        if (objectList == null) {
            throw new DataNotFoundException("Invalid Guidebox ID ("+guideBoxId+")");
        }

        ServiceProviderLink providerLink;
        for(JsonObject jsonObject : objectList) {
            providerLink = new ServiceProviderLink();
            providerLink.setType(getType(jsonObject.getString("source"))); //TODO make sure all guidebox sources are implemented in Enum
            providerLink.setUrl(jsonObject.getString("link"));
            serviceProviderLinks.add(providerLink);
        }

        displayMovieEntity.setProviderList(serviceProviderLinks);
    }

    private ServiceProviderType getType(String identifier) throws UnregisteredProviderException{
        identifier = identifier.toUpperCase();
        ServiceProviderType ret = null;
        try {
            ret = ServiceProviderType.valueOf(identifier);
        } catch (IllegalArgumentException iae) {
            throw new UnregisteredProviderException("Provider not in system "+identifier);
        }
        return ret;
    }

    //TODO Kind of dubplicate with getMovieFromTMDB in UpdateDatabaseEJB
    private void setTmdbInfo(DisplayMovieEntity displayMovieEntity, Integer tmdbId) throws DataNotFoundException{
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
        else {
            throw new DataNotFoundException("Invalid TMdB Id ("+tmdbId+")");
        }
    }
}
