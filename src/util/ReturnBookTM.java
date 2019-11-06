package util;

public class ReturnBookTM {
    private String bookId;
    private String issueDate;
    private String issueId;
    private String bookTitle;
    private String memberId;
    private String memberName;
    private String returnDate;
    private double overdueFines;

    public ReturnBookTM() {
    }

    public ReturnBookTM(String bookId, String issueDate, String issueId, String bookTitle, String memberId, String memberName, String returnDate, double overdueFines) {
        this.bookId = bookId;
        this.issueDate = issueDate;
        this.issueId = issueId;
        this.bookTitle = bookTitle;
        this.memberId = memberId;
        this.memberName = memberName;
        this.returnDate = returnDate;
        this.overdueFines = overdueFines;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public double getOverdueFines() {
        return overdueFines;
    }

    public void setOverdueFines(double overdueFines) {
        this.overdueFines = overdueFines;
    }

    @Override
    public String toString() {
        return "ReturnBookTM{" +
                "bookId='" + bookId + '\'' +
                ", issueDate='" + issueDate + '\'' +
                ", issueId='" + issueId + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", returnDate='" + returnDate + '\'' +
                ", overdueFines=" + overdueFines +
                '}';
    }
}
