package library;

import java.sql.SQLException;
import java.util.List;

public interface UserDAO1 {
	// 도서검색
	public List<BookInfoDTO1> listBook(String bookname, String author_name);
	
	// 대출 신청
	public void insertloan(LoanDTO dto) throws SQLException;
	
	// 대출 연장
	public List<LoanDTO> listloan(String user_code);
}
