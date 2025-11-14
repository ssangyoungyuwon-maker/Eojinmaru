package library;

import java.sql.SQLException;
import java.util.List;

public interface BookDAO {
	
	// 도서검색
	public List<BookInfoDTO> listBook(String search);
	
	// 대출 신청 도서코드 검색
	public List<BookInfoDTO> loanBook(int bookcode);
	
	// 대출 중 도서코드 검색
	public List<LoanDTO> loanlistbook(int bookcode);
	
	// 대출 신청
	public void insertloan(LoanDTO dto) throws SQLException;
	
	// 대출 연장
	public void extendloan(int loan_code) throws SQLException;
	

	// 전체 대출 리스트
	public List<LoanDTO> listloan(int user_code);
	
	// 회원의 대출 리스트
	public List<LoanDTO> listloaning(int book_code);	
	
	// 대출 중인 도서 연장신청
	public List<LoanDTO> exloan(int loan_code);
	
	// 대출 중인 도서 예약
	public void loanreservation(LoanDTO dto) throws SQLException;

	// 연체 여부
	public LoanDTO bookLoaning(int user_code);

	
}
