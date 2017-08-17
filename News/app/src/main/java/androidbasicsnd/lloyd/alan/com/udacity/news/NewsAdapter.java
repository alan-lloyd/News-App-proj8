package androidbasicsnd.lloyd.alan.com.udacity.news;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> news) {
        super(context, 0, news);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        News currentNews = getItem(position);
        //get received google book data values as Strings
        String publicationDate = currentNews.getPublicationDate();
        String title = currentNews.getTitle();
        String author = currentNews.getAuthor();
        //put collected data into App display page
        TextView publishedYearView = (TextView) listItemView.findViewById(R.id.publication_date);
        publishedYearView.setText(publicationDate);

        TextView newsTitleView = (TextView) listItemView.findViewById(R.id.news_title);
        newsTitleView.setText(title);


        TextView authorView = (TextView) listItemView.findViewById(R.id.author);
        authorView.setText(author);

        return listItemView;
    }//end of method getView()

}
