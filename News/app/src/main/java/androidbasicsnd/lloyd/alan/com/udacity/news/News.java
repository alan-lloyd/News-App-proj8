package androidbasicsnd.lloyd.alan.com.udacity.news;

public class News {
    private String publicationDate;
    private String title;
    private String author;
    private String url;

    public News(String publicationDate, String author, String title, String url) {
        this.publicationDate = publicationDate;
        this.author = author;
        this.title = title;
        this.url = url;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getTitle() {
        return title;
    }


    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

}