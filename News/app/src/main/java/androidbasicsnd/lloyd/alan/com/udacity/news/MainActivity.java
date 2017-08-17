package androidbasicsnd.lloyd.alan.com.udacity.news;

import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.app.LoaderManager.LoaderCallbacks;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderCallbacks<List<News>> {

    //Constant value for the Book loader ID. Can choose any integer
    private static final int NEWS_LOADER_ID = 1;
    // Tag for log messages
    private static final String LOG_TAG = NewsLoader.class.getName();
    //   string  to obtain unrefined news data from the Guardian newspaper API;
    private static final String NEWS_REQUEST_URL = "https://content.guardianapis.com/search?q=business&AND&trade&from-date=2017-08-01&show-tags=contributor&api-key=75700ee6-e4a9-4f2c-b53e-2cb294199581";


    //  private android.widget.SearchView searchText;
    //TextView displayed when list empty
    private TextView emptyStateTextView;
    // Adapter for the list of Books
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find a reference to the  ListView in the layout
        ListView newsListView = (ListView) findViewById(R.id.list);
        // Create a new adapter that takes an empty list of Books as input
        adapter = new NewsAdapter(this, new ArrayList<News>());
        // Set the adapter on the  ListView so the list can be populated in the user interface
        newsListView.setAdapter(adapter);

        emptyStateTextView = (TextView) findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyStateTextView);
        // searchText = (android.widget.SearchView) findViewById(R.id.search_text);

        getLoaderManager().initLoader(NEWS_LOADER_ID, null, MainActivity.this);

        //listener to load user selected web pages from book items
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current Book that was clicked on
                News currentNews = adapter.getItem(position);
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(currentNews.getUrl());
                // Create a new intent to view the Book URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);
                // Send the intent to launch a new activity
                //check device has an App that can handle this program, to stop it crashing
                if (websiteIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(websiteIntent);  //where intent is your intent
                }
            }
        });  //end of on click listener for user to view selected webpages of results

    }//end of onCreate


    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        return new NewsLoader(this, NEWS_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);
        // Set empty state text to display "No news found."
        emptyStateTextView.setText(R.string.no_news);
        // Clear the adapter of previous News data
        adapter.clear();
        // If there is a valid list of  News, then add them to the adapter's
        // data set. This will trigger the ListView to update
        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, to clear out our existing data
        adapter.clear();
    }
} //end of MainActivity

