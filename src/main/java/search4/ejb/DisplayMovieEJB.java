package search4.ejb;

import search4.daobeans.DisplayMovieDAOBean;
import search4.entities.DisplayMovieEntity;
import search4.entities.MovieEntity;
import search4.entities.ServiceProviderLink;
import search4.entities.enums.ServiceProviderType;
import search4.helpers.DateParser;
import search4.helpers.JSonHelper;
import search4.helpers.URLBuilder;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.List;

//TODO interface?
@Stateless
public class DisplayMovieEJB {

    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;

    private MovieEntity getMovieData(Integer id) {
        return displayMovieDAOBean.getMovieData(id); //TODO error handling; here or in DAO?
    }

    //TODO error handling for all methods in this method. Throw exceptions? Send error messages? Return?
    //NEW METHOD: better to send the id to EJB and retrieve MovieEntity here. No point in bringing it to backing bean.
    public DisplayMovieEntity getDisplayMovie(Integer id) {
        DisplayMovieEntity displayMovieEntity = new DisplayMovieEntity();
        MovieEntity movieEntity = getMovieData(id);
        setGuideboxId(movieEntity); //Check if guidbox id is set. If not, set it.
        setStreamingServices(displayMovieEntity, movieEntity.getGuideboxId()); //Retrieve streaming services from guidebox
        setTmdbInfo(displayMovieEntity, movieEntity.getTmdbId()); //Retrieve movie information (description, poster etc) from TMDB
        return displayMovieEntity;
    }

    private void setGuideboxId(MovieEntity movieEntity) {
        if (movieEntity.getGuideboxId() < 1) { //TODO check what lowest guidebox id is
            movieEntity.setGuideboxId(getGuideboxId(movieEntity.getTmdbId()));
        }
    }

    private Integer getGuideboxId(Integer tmdbId) {
        Integer guideboxId = 0; //TODO find lowest
        JSonHelper jSonHelper = new JSonHelper();
        URLBuilder urlBuilder = new URLBuilder();

        String url = urlBuilder.guideboxUrl(tmdbId, "/search/movie/id/themoviedb/");

        JsonObject jsonObject = jSonHelper.getObject(url);
        if (jsonObject != null) {
            guideboxId = jsonObject.getInt("id");
        }
        return guideboxId;
    }

    private void setStreamingServices(DisplayMovieEntity displayMovieEntity, Integer guideBoxId) {
        JSonHelper jSonHelper = new JSonHelper();
        URLBuilder urlBuilder = new URLBuilder();

        String url = urlBuilder.guideboxUrl(guideBoxId, "/movie/");

        List<JsonObject> objectList = jSonHelper.getObjectList(url, "purchase_web_sources");
        List<ServiceProviderLink> serviceProviderLinks = new ArrayList<ServiceProviderLink>();

        ServiceProviderLink providerLink;
        for(JsonObject jsonObject : objectList) {
            providerLink = new ServiceProviderLink();
            providerLink.setType(getType(jsonObject.getString("source"))); //TODO make sure all guidebox sources are implemented in Enum
            providerLink.setUrl(jsonObject.getString("link"));
            serviceProviderLinks.add(providerLink);
        }

        displayMovieEntity.setProviderList(serviceProviderLinks);
    }

    private ServiceProviderType getType(String identifier) {
        return ServiceProviderType.valueOf(identifier.toUpperCase());
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
