package search4.helpers;

public class URLBuilder {

    public String tmdbUrl(Integer tmdbId) {
        APIKeyReader apiKeyReader = new APIKeyReader();
        String tmdbUrl = "https://api.themoviedb.org/3/movie/";
        String tmdbAPIKey = apiKeyReader.getKey("tmdb");
        return tmdbUrl+tmdbId+tmdbAPIKey;
    }

    public String guideboxUrl(Integer id, String endQuery) {
        APIKeyReader apiKeyReader = new APIKeyReader();
        String guideBoxUrl = "https://api-public.guidebox.com/v1.43/US/";
        String guideBoxAPIKey = apiKeyReader.getKey("guidebox");
        return guideBoxUrl+guideBoxAPIKey+endQuery+id;
    }
}
