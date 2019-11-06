package util;

public class MemberTM {
     String memberId;
     String memberFname;
     String memberLname;
     String memberAddress;
     String memberContactNo;
     String memberNIC;
     String memberEmail;

    public MemberTM() {
    }

    public MemberTM(String memberId) {
        this.memberId = memberId;
    }

    public MemberTM(String memberId, String memberFname, String memberLname, String memberAddress, String memberContactNo, String memberNIC, String memberEmail) {
        this.memberId = memberId;
        this.memberFname = memberFname;
        this.memberLname = memberLname;
        this.memberAddress = memberAddress;
        this.memberContactNo = memberContactNo;
        this.memberNIC = memberNIC;
        this.memberEmail = memberEmail;
    }

    @Override
    public String toString() {
        return "MemberTM{" +
                "memberId='" + memberId + '\'' +
                ", memberFname='" + memberFname + '\'' +
                ", memberLname='" + memberLname + '\'' +
                ", memberAddress='" + memberAddress + '\'' +
                ", memberContactNo='" + memberContactNo + '\'' +
                ", memberNIC='" + memberNIC + '\'' +
                ", memberEmail='" + memberEmail + '\'' +
                '}';
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberFname() {
        return memberFname;
    }

    public void setMemberFname(String memberFname) {
        this.memberFname = memberFname;
    }

    public String getMemberLname() {
        return memberLname;
    }

    public void setMemberLname(String memberLname) {
        this.memberLname = memberLname;
    }

    public String getMemberAddress() {
        return memberAddress;
    }

    public void setMemberAddress(String memberAddress) {
        this.memberAddress = memberAddress;
    }

    public String getMemberContactNo() {
        return memberContactNo;
    }

    public void setMemberContactNo(String memberContactNo) {
        this.memberContactNo = memberContactNo;
    }

    public String getMemberNIC() {
        return memberNIC;
    }

    public void setMemberNIC(String memberNIC) {
        this.memberNIC = memberNIC;
    }

    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }
}
