package androidbasicsnd.lloyd.alan.com.udacity.news;

public class News {
    private String publicationDate;
    private String title;
    private String author;
    private String url;
    private String articleCategory;

    public News(String articleCategory, String publicationDate, String author, String title, String url) {
        this.articleCategory = articleCategory;
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

    public String getArticleCategory() {
        return articleCategory;
    }

}