package library;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO1 {
	
	// 도서검색
	public List<BookInfoDTO1> listBook(String search);
	
	// 대출 신청 도서코드 검색
	public List<BookInfoDTO1> loanBook(int book_code);
	
	// 대출 신청
	public void insertloan(LoanDTO dto) throws SQLException;
	
	// 전체 대출 리스트
	public List<LoanDTO> listloan(String user_code);
	
	// 대출 중 리스트
	public List<LoanDTO> listloaning(String user_code);	
	
	// 대출 중인 도서 리스트
	public List<LoanDTO> loaning(String book_condition);
	
	// 대출 중인 도서 예약
	public void loanreservation(LoanDTO dto) throws SQLException;

	// 연체 여부
	public LoanDTO bookLoaning(int user_code);

	List<LoanDTO> listloan(int user_code);
	
}
