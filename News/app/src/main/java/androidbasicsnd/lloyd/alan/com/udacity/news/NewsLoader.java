package androidbasicsnd.lloyd.alan.com.udacity.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

// loads a list of News articles by using an AsyncTask to perform the network request to the given URL
public class NewsLoader extends AsyncTaskLoader<List<News>> {

    // query URL
    private String mUrl;

    // Constructs a new NewsLoader. context of the activity,
    // url to load data from
    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    //on a background thread - this method is called automatically by android. It returns the result of
    //fetchNewsData() - which includes createURL and fetchData methods. So declaring a Loader thread
    //is what starts an internet connection in this app.
    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) {
            return null;
        }
        // perform the network request, parse response, and extract list of News items
        return QueryUtils.fetchNewsData(mUrl);
    }
}