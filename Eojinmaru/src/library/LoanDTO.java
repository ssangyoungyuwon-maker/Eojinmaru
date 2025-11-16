package library;

public class LoanDTO {
	private int loan_code; // 대출번호
	private int book_code; // 도서번호
	private String bookname; // 도서제목
	private int user_code; // 회원번호
	private String user_name; // 회원이름
	private String checkout_date; // 대출일자
	private String due_date; // 반납예정날짜
	private String return_date; // 실제 반납날짜
	private int isExtended; // 대출연장 가능여부
	private String reservation_date; // 대출예약
	private String loan_renewaldate; // 연체 대출불가날짜
	private String book_condition; // 도서대출? 현황 상태(대출중, 대충가능, 반납 등)
	public int setloancount;
	
	public int getLoan_code() {
		return loan_code;
	}
	public void setLoan_code(int loan_code) {
		this.loan_code = loan_code;
	}
	public int getBook_code() {
		return book_code;
	}
	public void setBook_code(int book_code) {
		this.book_code = book_code;
	}
	public String getBookname() {
		return bookname;
	}
	public void setBookname(String bookname) {
		this.bookname = bookname;
	}
	public int getUser_code() {
		return user_code;
	}
	public void setUser_code(int user_code) {
		this.user_code = user_code;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getCheckout_date() {
		return checkout_date;
	}
	public void setCheckout_date(String checkout_date) {
		this.checkout_date = checkout_date;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public String getReturn_date() {
		return return_date;
	}
	public void setReturn_date(String return_date) {
		this.return_date = return_date;
	}
	public int getIsExtended() {
		return isExtended;
	}
	public void setIsExtended(int isExtended) {
		this.isExtended = isExtended;
	}
	public String getReservation_date() {
		return reservation_date;
	}
	public void setReservation_date(String reservation_date) {
		this.reservation_date = reservation_date;
	}
	public String getLoan_renewaldate() {
		return loan_renewaldate;
	}
	public void setLoan_renewaldate(String loan_renewaldate) {
		this.loan_renewaldate = loan_renewaldate;
	}
	public String getBook_condition() {
		return book_condition;
	}
	public void setBook_condition(String book_condition) {
		this.book_condition = book_condition;
	}
	public int getSetloancount() {
		return setloancount;
	}
	public void setSetloancount(int setloancount) {
		this.setloancount = setloancount;
	}
	


}
