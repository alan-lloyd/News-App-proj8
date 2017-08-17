package androidbasicsnd.lloyd.alan.com.udacity.news;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

// Helper methods related to requesting and receiving data from Google books.
public final class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getSimpleName();   // Tag for log messages
    private static final int READ_TIMEOUT = 10000;
    private static final int CONNECT_TIMEOUT = 15000;
    //constants for data extracted from JSON data
    private static final String KEY_TITLE = "webTitle";
    private static final String KEY_ITEMS = "response";

    private static final String KEY_CONTRIBUTOR_NAME = "webTitle";

    private static final String KEY_TAG_CONTRIBUTOR = "tags";
    private static final String KEY_PUBLISHED_DATE = "webPublicationDate";
    private static final String KEY_VOLUME_INFO = "results";


    /**
     * Create a private constructor because no one should ever create a  QueryUtils object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed)
     */
    private QueryUtils() {
    }

    // Query the Google Books API dataset and return a list of class Book objects
    public static List<News> fetchNewsData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of  Book items
        List<News> news = extractFeatureFromJson(jsonResponse);
        // Return the list of books
        return news;
    }

    /**
     * Returns new URL object from the given string URL
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as response
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        // If the URL is null, then return early
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (http response code 200),
            // then read the input stream and parse the response
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the InputStream into a String which contains the
     * whole JSON response from the server
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }


    /**
     * Return a list of news objects that has been built up from parsing the given JSON response
     */
    private static List<News> extractFeatureFromJson(String newsJSON) {
        // If the JSON string is empty or null, then return early
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }
        // Create an empty ArrayList to start adding news items to
        ArrayList<News> news = new ArrayList<>();

        try {
            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(newsJSON);
            // Getting JSON Array node

            JSONArray newsArray = baseJsonResponse.getJSONArray(KEY_ITEMS);
            // looping through all imported books
            for (int i = 0; i < newsArray.length(); i++) {
                JSONObject eachnews = newsArray.getJSONObject(i);
                //discarding unwanted data


                JSONObject anews = eachnews.getJSONObject(KEY_VOLUME_INFO);



                // Extract the value for keys of interest
                String publishedyear = "";
                String contributorName = "";
                String title = "";

                //  String contributors="";
                if (baseJsonResponse.has(KEY_PUBLISHED_DATE)) {
                    //some books have no date key - try/catch handles this
                    try {
                        publishedyear = anews.getString(KEY_PUBLISHED_DATE);
                    } catch (JSONException e) {
                        publishedyear = "no publication date";
                    }
                }


                //  String contributors="";
                if (baseJsonResponse.has(KEY_PUBLISHED_DATE)) {
                    //some books have no date key - try/catch handles this
                    try {
                        publishedyear = anews.getString(KEY_PUBLISHED_DATE);
                    } catch (JSONException e) {
                        publishedyear = "no publication date";
                    }
                }




                if (baseJsonResponse.has(KEY_TITLE)) {
                    //some books have no title key - try/catch handles this
                    try {
                        title = anews.getString(KEY_TITLE);
                    } catch (JSONException e) {
                        title = "no title ";
                    }
                }
                // Extract the value for the key called "url"
                String url = eachnews.getString("url");



                JSONObject authorName = eachnews.getJSONObject(KEY_TAG_CONTRIBUTOR);  //tags...

                if (baseJsonResponse.has(KEY_CONTRIBUTOR_NAME)) {
                    //some books have no authors key - try/catch handles this
                    try {
                        contributorName = authorName.getString(KEY_CONTRIBUTOR_NAME);

                    } catch (JSONException e) {
                        contributorName = "no authors name";
                    }
                }


                // Create a new object with class Book attributes, from JSON response
                News thisNews = new News(publishedyear, contributorName, title, url);
                news.add(thisNews);

            }


        } catch (JSONException e) {
            // If another error is thrown when executing any of the above statements in the main "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }
        // Return the list of books
        return news;
    }// end of extractFeatureFromJson()

}