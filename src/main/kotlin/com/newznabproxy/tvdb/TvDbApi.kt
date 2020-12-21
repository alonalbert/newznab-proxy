package com.newznabproxy.tvdb

import com.google.gson.Gson
import com.newznabproxy.tvdb.model.LoginResult
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

private const val API_KEY = "0a74a3c6-1388-4dfc-9df5-76531508062f"
private const val API_PIN = "1234"
private const val URL_BASE = "https://api4.thetvdb.com/v4"
private const val URL_AUTHENTICATE = "$URL_BASE/login"
private const val URL_SERIES = "$URL_BASE/series/%s"

object TvDbApi {

  private val gson = Gson()
  private val httpClient = HttpClient.newHttpClient()
  private var sessionToken: String


  init {
    val login = Login(API_KEY, API_PIN)
    val response = httpClient.send(
      HttpRequest.newBuilder(URI.create(URL_AUTHENTICATE))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .header("Accept", "application/json")
        .POST(BodyPublishers.ofString(gson.toJson(login)))
        .build(),
      BodyHandlers.ofString()
    )
    val loginResult = gson.fromJson(response.body(), LoginResult::class.java)
    sessionToken = loginResult.data.token

  }

  fun getSeriesName(id: String): String {
    val response = httpClient.send(
      HttpRequest.newBuilder(URI.create(URL_SERIES.format(id)))
        .header("Authorization", "Bearer $sessionToken")
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .build(),
      BodyHandlers.ofString()
    )
    return gson.fromJson(response.body(), SeriesResult::class.java).data.name
  }

  // /**
  //  * Represents this <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id">API call</a>
  //  * which returns a Series object, that contains detailed information about a series.
  //  *
  //  * @param seriesId id of a series obtained via the {@link #searchSeries(String)} method
  //  * @return a Series object
  //  * @throws IOException either because of an unsuccessful HttpClient execution or an unsuccessful
  //  * conversion from an entity to a String
  //  */
  // public Series getSeries(int seriesId) throws IOException {
  //   /* Prepare the URL */
  //   String url = BASE_URL + PATH_SERIES;
  //   url = url.replace("$ID", String.valueOf(seriesId));
  //
  //   /* Execute the GET Method, receive a JSON response from the server and convert it to a String */
  //   httpGet.setURI(URI.create(url));
  //   httpResponse = httpClient.execute(httpGet);
  //   httpEntity = httpResponse.getEntity();
  //   String body = EntityUtils.toString(httpEntity, "UTF-8");
  //
  //   /* Finally translate the JSON response to object(s) and return it */
  //   return gson.fromJson(body, Series.class).getSeries();
  // }
  //
  // /**
  //  * Represents this <a href="https://api.thetvdb.com/swagger#!/Episodes/get_episodes_id">API
  //  * call</a> which returns a EpisodeDetailed object that contains detailed information of a
  //  * specific episode.
  //  *
  //  * @param episodeId id of a episode obtained via the {@link #getSeasons(int)} method
  //  * @return a EpisodeDetailed object
  //  * @throws IOException either because of an unsuccessful HttpClient execution or an unsuccessful
  //  * conversion from an entity to a String
  //  */
  // public EpisodeDetailed getSpecificEpisode(int episodeId) throws IOException {
  //   /* Prepare the URL */
  //   String url = BASE_URL + PATH_EPISODES_DETAILED;
  //   url = url.replace("$ID", String.valueOf(episodeId));
  //
  //   /* Execute the GET Method, receive a JSON response from the server and convert it to a String */
  //   httpGet.setURI(URI.create(url));
  //   httpResponse = httpClient.execute(httpGet);
  //   httpEntity = httpResponse.getEntity();
  //   String body = EntityUtils.toString(httpEntity, "UTF-8");
  //
  //   /* Finally translate the JSON response to object(s) and return it */
  //   return gson.fromJson(body, EpisodeDetailed.class).getEpisodeDetailed();
  // }
  //
  //
  // /**
  //  * Represents this <a href="https://api.thetvdb.com/swagger#!/Search/get_search_series">API
  //  * call</a> which returns a list of SearchResult objects. More importantly this method returns a
  //  * seriesId for each object which is needed for most API calls.
  //  *
  //  * @param seriesName name of series
  //  * @return a list of SearchResult objects
  //  * @throws IOException either because of an unsuccessful HttpClient execution or an unsuccessful
  //  * conversion from an entity to a String
  //  */
  // public List<SearchResult> searchSeries(String seriesName) throws IOException {
  //   /* Prepare the URL */
  //   String url = BASE_URL + PATH_SEARCH;
  //   seriesName = seriesName.replaceAll(" ", "%20");
  //   url = url.replace("$NAME", seriesName);
  //
  //   /* Execute the GET Method, receive a JSON response from the server and convert it to a String */
  //   httpGet.setURI(URI.create(url));
  //   httpResponse = httpClient.execute(httpGet);
  //   httpEntity = httpResponse.getEntity();
  //   String body = EntityUtils.toString(httpEntity, "UTF-8");
  //
  //   /* Finally translate the JSON response to object(s) and return it */
  //   return gson.fromJson(body, SearchResult.class).getSearchResults();
  // }
  //
  // /**
  //  * Represents this <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_actors">API
  //  * call</a> which returns a list of Actor objects of a specific series.
  //  *
  //  * @param seriesId id of a series obtained via the {@link #searchSeries(String)} method
  //  * @return a list of Actor objects
  //  * @throws IOException either because of an unsuccessful HttpClient execution or an unsuccessful
  //  * conversion from an entity to a String
  //  */
  // public List<Actor> getActors(int seriesId) throws IOException {
  //   /* Prepare the URL */
  //   String url = BASE_URL + PATH_SERIES + PATH_ACTORS;
  //   url = url.replace("$ID", String.valueOf(seriesId));
  //
  //   /* Execute the GET Method, receive a JSON response from the server and convert it to a String */
  //   httpGet.setURI(URI.create(url));
  //   httpResponse = httpClient.execute(httpGet);
  //   httpEntity = httpResponse.getEntity();
  //   String body = EntityUtils.toString(httpEntity, "UTF-8");
  //
  //   /* Finally translate the JSON response to object(s) and return it */
  //   return gson.fromJson(body, Actor.class).getActors();
  //
  // }
  //
  // /**
  //  * Represents this <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_episodes">API
  //  * call</a> which returns a list of Episode objects of a specific series. Furthermore this method
  //  * - appart from converting the JSON response to Episode objects - bundles the given Episode
  //  * objects into a list of season objects for obvious reasons. This means no more uncategorized/
  //  * chaotic episode objects.
  //  *
  //  * @param seriesId obtained via the {@link #searchSeries(String)} method
  //  * @return a list of Season objects
  //  * @throws IOException either because of an unsuccessful HttpClient execution or an unsuccessful
  //  * conversion from an entity to a String
  //  */
  // public List<Season> getSeasons(int seriesId) throws IOException {
  //   /* Prepare the URL */
  //   String url = BASE_URL + PATH_SERIES + PATH_EPISODES;
  //   url = url.replace("$ID", String.valueOf(seriesId));
  //
  //   /* Execute the GET Method, receive a JSON response from the server and convert it to a String */
  //   httpGet.setURI(URI.create(url));
  //   httpResponse = httpClient.execute(httpGet);
  //   httpEntity = httpResponse.getEntity();
  //   String body = EntityUtils.toString(httpEntity, "UTF-8");
  //
  //   /* Translate the JSON response to object(s) */
  //   List<Episode> episodes = gson.fromJson(body, Episode.class).getEpisodes();
  //
  //   /* Get the total number of seasons */
  //   int totalSeasons = 0;
  //
  //   for (Episode e : episodes) {
  //     if (e.getSeasonNumber() > totalSeasons) {
  //       totalSeasons = e.getSeasonNumber();
  //     }
  //   }
  //
  //   /* Wrap all the give episode objects into a list of season objects and return it */
  //   List<Season> seasons = new ArrayList<>();
  //
  //   for (int i = 0; i <= totalSeasons; i++) {
  //     Season tempSeasonObject = new Season();
  //     List<Episode> tempEpisodes = new ArrayList<>();
  //     for (int j = 0; j < episodes.size(); j++) {
  //       if (episodes.get(j).getSeasonNumber() == i) {
  //         tempEpisodes.add(episodes.get(j));
  //       }
  //     }
  //     tempSeasonObject.setEpisodes(tempEpisodes);
  //     seasons.add(tempSeasonObject);
  //   }
  //
  //   /* Double check if all season objects contain a list of episodes greater than 1*/
  //   for (int i = 0; i < seasons.size(); i++) {
  //     if (seasons.get(i).getEpisodes().size() < 1) {
  //       seasons.remove(i);
  //     }
  //   }
  //
  //   return seasons;
  // }
  //
  // /**
  //  * Represents this <a href="https://api.thetvdb.com/swagger#!/Series/get_series_id_images_query">API
  //  * call</a> which returns a list of Image objects of a specific series.
  //  *
  //  * @param seriesId obtained via the {@link #searchSeries(String)} method
  //  * @param type of image (enum with fanart, poster, series or season attributes)
  //  * @return a list of Image objects
  //  * @throws IOException either because of an unsuccessful HttpClient execution or an unsuccessful
  //  * conversion from an entity to a String
  //  */
  // public List<Image> getImages(int seriesId, ImageType type) throws IOException {
  //   /* Prepare the URL */
  //   String url = BASE_URL + PATH_SERIES + PATH_IMAGES;
  //   url = url.replace("$ID", String.valueOf(seriesId));
  //   url = url.replace("$KEYTYPE", type.name().toLowerCase());
  //
  //   /* Execute the GET Method, receive a JSON response from the server and convert it to a String */
  //   httpGet.setURI(URI.create(url));
  //   httpResponse = httpClient.execute(httpGet);
  //   httpEntity = httpResponse.getEntity();
  //   String body = EntityUtils.toString(httpEntity, "UTF-8");
  //
  //   /* Finally translate the JSON response to object(s) and return it */
  //   return gson.fromJson(body, Image.class).getImages();
  // }
}
