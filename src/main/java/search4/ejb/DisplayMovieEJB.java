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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Stateless
public class DisplayMovieEJB implements LocalDisplayMovie, Serializable{

	private List<String> tempProviders;

    @EJB
    private DisplayMovieDAOBean displayMovieDAOBean;

    private MovieEntity getMovieData(Integer id) throws Exception{
        return displayMovieDAOBean.getMovieData(id);
    }
    

    public void getSimpleDisplayMovieEntity(Integer id) throws Exception {
    	DisplayMovieEntity displayMovieEntity = new DisplayMovieEntity();
    	MovieEntity movieEntity = getMovieData(id);
    	displayMovieEntity.setTitle(movieEntity.getTitle());
    	
    }
    
    //NEW METHOD: better to send the id to EJB and retrieve MovieEntity here. No point in bringing it to backing bean.
    public DisplayMovieEntity getDisplayMovie(Integer id) throws Exception {
        MovieEntity movieEntity = getMovieData(id);
        setGuideboxId(movieEntity); //Check if guidbox id is set. If not, set it.
        tempProviders = new ArrayList<String>();
        return createDisplayMovie(movieEntity);
    }

    public DisplayMovieEntity createDisplayMovie(MovieEntity movieEntity) throws Exception{
        DisplayMovieEntity displayMovieEntity = new DisplayMovieEntity();
        tempProviders = new ArrayList<String>();
        setStreamingServices(displayMovieEntity, movieEntity.getGuideboxId()); //Retrieve streaming services from guidebox
        displayMovieEntity.setCurrentProviders(tempProviders);
        setTmdbInfo(displayMovieEntity, movieEntity.getTmdbId()); //Retrieve movie information (description, poster etc) from TMDB
        displayMovieEntity.checkAddedServices();    //populate all boolean has-properties
        return displayMovieEntity;
    }

    private void setGuideboxId(MovieEntity movieEntity) throws Exception {
        if (movieEntity.getGuideboxId() < 1) { //first actual guidebox id is 6
            //TODO if we get weird errors with getting display movie it probably means there is an exception thrown from this method
            movieEntity.setGuideboxId(getGuideboxId(movieEntity.getTmdbId()));
        }
    }

    private Integer getGuideboxId(Integer tmdbId) throws Exception {
        Integer guideboxId = 0;
        JSonHelper jSonHelper = new JSonHelper();
        URLBuilder urlBuilder = new URLBuilder();

        String url = urlBuilder.guideboxUrl(tmdbId, "/search/movie/id/themoviedb/");

        JsonObject jsonObject = jSonHelper.getObject(url);
        if (jsonObject != null && !jsonObject.isEmpty()) {
            guideboxId = jsonObject.getInt("id");
        }
        else {
            return 0; //Currently if there is no guidebox movie with the specified tmdb id, set guidebox id to 0 (unset)
        }
        return guideboxId;
    }

    private void setStreamingServices(DisplayMovieEntity displayMovieEntity, Integer guideBoxId) throws Exception {
        URLBuilder urlBuilder = new URLBuilder();

        String url = urlBuilder.guideboxUrl(guideBoxId, "/movie/");

        JsonObject movieObject = new JSonHelper().getObject(url);

        //Android
        displayMovieEntity.setProviderListAndroidFree(getProvidersOfType(movieObject, "free_android_sources", guideBoxId));
        displayMovieEntity.setProviderListAndroidPurchase(getProvidersOfType(movieObject, "purchase_android_sources", guideBoxId));
        displayMovieEntity.setProviderListAndroidSubscription(getProvidersOfType(movieObject, "subscription_android_sources", guideBoxId));
        displayMovieEntity.setProviderListAndroidTvEverywhere(getProvidersOfType(movieObject, "tv_everywhere_android_sources", guideBoxId));

        //iOS
        displayMovieEntity.setProviderListIOSFree(getProvidersOfType(movieObject, "free_ios_sources", guideBoxId));
        displayMovieEntity.setProviderListIOSPurchase(getProvidersOfType(movieObject, "purchase_ios_sources", guideBoxId));
        displayMovieEntity.setProviderListIOSSubscription(getProvidersOfType(movieObject, "subscription_ios_sources", guideBoxId));
        displayMovieEntity.setProviderListIOSTvEverywhere(getProvidersOfType(movieObject, "tv_everywhere_ios_sources", guideBoxId));

        //Web
        displayMovieEntity.setProviderListWebFree(getProvidersOfType(movieObject, "free_web_sources", guideBoxId));
        displayMovieEntity.setProviderListWebPurchase(getProvidersOfType(movieObject, "purchase_web_sources", guideBoxId));
        displayMovieEntity.setProviderListWebSubscription(getProvidersOfType(movieObject, "subscription_web_sources", guideBoxId));
        displayMovieEntity.setProviderListWebTvEverywhere(getProvidersOfType(movieObject, "tv_everywhere_web_sources", guideBoxId));

        //Others
        displayMovieEntity.setProviderListOther(getProvidersOfType(movieObject, "other_sources", guideBoxId));
    }

    private List<ServiceProviderLink> getProvidersOfType(JsonObject movieObject, String providerType, Integer guideBoxId) throws Exception {
        JSonHelper jSonHelper = new JSonHelper();
        List<JsonObject> objectList = jSonHelper.getObjectList(movieObject, providerType);
        List<ServiceProviderLink> serviceProviderLinks = new ArrayList<ServiceProviderLink>();

        if (objectList == null) {
            System.out.println("This is explosion?");
            throw new DataNotFoundException("Invalid Guidebox ID ("+guideBoxId+")");
        }

        ServiceProviderLink providerLink;
        for(JsonObject jsonObject : objectList) {
            providerLink = new ServiceProviderLink();
            providerLink.setName(jsonObject.getString("display_name"));
            tempProviders.add(jsonObject.getString("display_name"));//sparar i listan f�r att spara alla providers
            providerLink.setUrl(jsonObject.getString("link"));
            serviceProviderLinks.add(providerLink);
        }
        return serviceProviderLinks;
    }

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


    public List<String> getMatchingProviders(List<String> requestedProviders, DisplayMovieEntity displayMovieEntity) throws Exception{
    	List<String> resultList = new ArrayList<String>();
    	if (requestedProviders.contains("All")) {
    		resultList = displayMovieEntity.getCurrentProviders();
    	} else {
	    	for(String requestedProvider : requestedProviders) {
	    		for(String movieProvider : displayMovieEntity.getCurrentProviders()) {
	    			if(requestedProvider.equalsIgnoreCase(movieProvider)) {
	    				if(!resultList.contains(requestedProvider)) {
	    					resultList.add(requestedProvider);
	    				}
	    			}
	    		}
	    	}
    	}
    	return resultList;
    }


}
