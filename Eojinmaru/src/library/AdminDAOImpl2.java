package library;

import java.sql.Connection;
import java.util.List;

import DBUtil.DBConn;

public class AdminDAOImpl2  implements AdminDAO2 {
	private Connection conn = DBConn.getConnection();

	@Override
	public void LoanBookSearchByPeriod(String loanperiod) {
		
	}

	@Override
	public void LoanBookSearchByName(String bookname) {
		
	}

	@Override
	public boolean LoanBookbaega() {
		return false;
	}

	@Override
	public List<AdminDTO2> LoanBookList() {
		return null;
	}

	@Override
	public List<AdminDTO2> returnBookList() {
		return null;
	}

	@Override
	public List<AdminDTO2> overdueBookList() {
		return null;
	}
	

    
    
}