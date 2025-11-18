package library;

import java.sql.SQLException;
import java.util.List;

public interface BookDAO {
	
	// 도서검색
	public List<BookInfoDTO> listBook(String search);
	
	// 대출 신청 도서코드 검색
	public List<BookInfoDTO> loannotBook(int bookcode);
	
	// 대출 중 도서코드 검색
	public List<LoanDTO> loanlistbook(int bookcode);
	
	// 대출 신청
	public void insertloan(LoanDTO dto) throws SQLException;
	
	// 대출 연장
	public void extendloan(int loan_code) throws SQLException;
	
	// 대출중인 모든 도서 리스트
	public List<LoanDTO> loanlistall(String bookname);
	
	public List<LoanDTO> loanlistcode(int book_code);
	
	// 회원의 대출 리스트
	public List<LoanDTO> listloaning(int book_code);	
	
	// 대출 중인 도서 예약
	public void loanreservation(LoanDTO dto) throws SQLException;

	public List<LoanDTO> listloan(int book_code);
	
	// 대출 신청 시 패널티
	 // 연체 회원
	    public List<LoanDTO> overdue(int user_code);
	 // 대출불가 날짜가 존재하는 회원
	    public List<LoanDTO> renwaldate(int user_code);
	 // 도서 대출 5개가 존재하는 회원
	    public int loancount(int user_code);
	    
     // 대출 예약 중인 도서 
	    public List<LoanDTO> loanreservationbook(int book_code);
	    	
}
