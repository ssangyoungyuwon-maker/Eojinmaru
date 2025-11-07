package library;

public class AdminDTO2 {
	
	private String bookname;
	private String loanperiod;
	private boolean baega;
	private String overdueusername;
	private String sincheongbook;
	private String sincheongstatus;
	private int sincheongcode;
	private int notice;
	
	
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
	public String getOverdueusername() {
		return overdueusername;
	}
	public void setOverdueusername(String overdueusername) {
		this.overdueusername = overdueusername;
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
	public int getNotice() {
		return notice;
	}
	public void setNotice(int notice) {
		this.notice = notice;
	}
}
