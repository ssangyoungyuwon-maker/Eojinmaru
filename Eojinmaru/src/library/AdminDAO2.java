package library;

import java.util.List;


public interface AdminDAO2 {
	
	
	public void LoanBookSearchByPeriod(String loanperiod) ;

	public void LoanBookSearchByName(String bookname) ;
	
	public boolean LoanBookbaega() ;
	
	public List<AdminDTO2> LoanBookList();
	
	public List<AdminDTO2> returnBookList() ;
	
	public List<AdminDTO2> overdueBookList() ;
	
}
