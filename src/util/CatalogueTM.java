package util;

public class CatalogueTM {
    private String bookId;
    private String bookTitle;
    private String bookAuthor;
    private String bookCategory;
    private String bookPublisher;
    private double bookPrice;
    private String bookStatus;

    public CatalogueTM() {
    }

    public CatalogueTM(String bookId, String bookTitle, String bookAuthor, String bookCategory, String bookPublisher, double bookPrice, String bookStatus) {
        this.setBookId(bookId);
        this.setBookTitle(bookTitle);
        this.setBookAuthor(bookAuthor);
        this.setBookCategory(bookCategory);
        this.setBookPublisher(bookPublisher);
        this.setBookPrice(bookPrice);
        this.setBookStatus(bookStatus);
    }

    @Override
    public String toString() {
        return "CatalogueTM{" +
                "bookId='" + getBookId() + '\'' +
                ", bookTitle='" + getBookTitle() + '\'' +
                ", bookAuthor='" + getBookAuthor() + '\'' +
                ", bookCategory='" + getBookCategory() + '\'' +
                ", bookPublisher='" + getBookPublisher() + '\'' +
                ", bookPrice=" + getBookPrice() +
                ", bookStatus='" + getBookStatus() + '\'' +
                '}';
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public double getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(double bookPrice) {
        this.bookPrice = bookPrice;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public void setBookStatus(String bookStatus) {
        this.bookStatus = bookStatus;
    }
}
