package util;

import java.time.LocalDate;

public class IssueBookTM {
    private String issueDate;
    private String issueId;
    private String memberId;
    private String memberName;
    private String bookId;
    private String bookTitle;
    private String returnStatus;

    public IssueBookTM() {
    }

    public IssueBookTM(String issueDate, String issueId, String memberId, String memberName, String bookId, String bookTitle, String returnStatus) {
        this.issueDate = issueDate;
        this.issueId = issueId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.returnStatus = returnStatus;
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

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    @Override
    public String toString() {
        return "IssueBookTM{" +
                "issueDate=" + issueDate +
                ", issueId='" + issueId + '\'' +
                ", memberId='" + memberId + '\'' +
                ", memberName='" + memberName + '\'' +
                ", bookId='" + bookId + '\'' +
                ", bookTitle='" + bookTitle + '\'' +
                ", returnStatus='" + returnStatus + '\'' +
                '}';
    }
}
