package library;

public class AdminDTO {
	
	private String bookname;
	private String loanperiod;
	private boolean baega;
	private String username;
	private int loancode;
	private int bookcode;
	private int overdue_date; // 연체일수
	private String checkout_date;
	private String return_date;
	private String due_date;
	private String book_condition;	
	private String sincheongbook;
	private String sincheongstatus;
	private int sincheongcode;
	private int noticeId;
	private String noticeTitle;
	private String noticeContent;
	private String noticeDate;
	
	
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public String getLoanperiod() {
		return loanperiod;
	}
	public void setLoanperiod(String loanperiod) {
		this.loanperiod = loanperiod;
	}
	public boolean isBaega() {
		return baega;
	}
	public void setBaega(boolean baega) {
		this.baega = baega;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getLoancode() {
		return loancode;
	}
	public void setLoancode(int loancode) {
		this.loancode = loancode;
	}
	public int getBookcode() {
		return bookcode;
	}
	public void setBookcode(int bookcode) {
		this.bookcode = bookcode;
	}
	public String getCheckout_date() {
		return checkout_date;
	}
	public void setCheckout_date(String checkout_date) {
		this.checkout_date = checkout_date;
	}
	public String getReturn_date() {
		return return_date;
	}
	public void setReturn_date(String return_date) {
		this.return_date = return_date;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public String getBook_condition() {
		return book_condition;
	}
	public void setBook_condition(String book_condition) {
		this.book_condition = book_condition;
	}
	public String getSincheongbook() {
		return sincheongbook;
	}
	public void setSincheongbook(String sincheongbook) {
		this.sincheongbook = sincheongbook;
	}
	public String getSincheongstatus() {
		return sincheongstatus;
	}
	public void setSincheongstatus(String sincheongstatus) {
		this.sincheongstatus = sincheongstatus;
	}
	public int getSincheongcode() {
		return sincheongcode;
	}
	public void setSincheongcode(int sincheongcode) {
		this.sincheongcode = sincheongcode;
	}
	public int getNoticeId() {
		return noticeId;
	}
	public void setNoticeId(int noticeId) {
		this.noticeId = noticeId;
	}
	public String getNoticeTitle() {
		return noticeTitle;
	}
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	public String getNoticeContent() {
		return noticeContent;
	}
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	public String getNoticeDate() {
		return noticeDate;
	}
	public void setNoticeDate(String noticeDate) {
		this.noticeDate = noticeDate;
	}
	public int getOverdue_date() {
		return overdue_date;
	}
	public void setOverdue_date(int overdue_date) {
		this.overdue_date = overdue_date;
	}
	
		

	
}