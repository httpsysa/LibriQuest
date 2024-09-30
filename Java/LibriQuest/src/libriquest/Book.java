package libriquest;

public class Book {
    public String title;
    public String author;
    public String publisher;
    public String genre;
    public String publishedDate;
    public String status;

    public Book(String title, String author, String publisher, String genre, String publishedDate, String status) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.genre = genre;
        this.publishedDate = publishedDate;
        this.status = status;
    }

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public String getGenre() { return genre; }
    public String getPublishedDate() { return publishedDate; }
    public String getStatus() { return status; }
}