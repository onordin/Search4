package search4.helpers;

public class URLBuilder {

    public String tmdbUrl(Integer tmdbId) {
        APIKeyReader apiKeyReader = new APIKeyReader();
        String tmdbUrl = "https://api.themoviedb.org/3/movie/";
        String tmdbAPIKey = apiKeyReader.getKey("tmdb");
        return tmdbUrl+tmdbId+tmdbAPIKey;
    }
}
