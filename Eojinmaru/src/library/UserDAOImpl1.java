package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class UserDAOImpl1 implements UserDAO1 {
	private Connection conn = DBConn.getConnection();

	@Override
	// 도서검색(제목/저자)
	public List<BookInfoDTO1> listBook(String bookname, String author_name) {
		List<BookInfoDTO1> list = new ArrayList<BookInfoDTO1>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			  // 도서번호, isbn, 도서이름, 저자, 출판사, 발행일를 출력
			sql = "SELECT book_code, bi.isbn, bookName, author_name, publisher_name, TO_CHAR(publisher_date, 'YYYYY-MM-DD') publisher_date FROM book b, bookinfo bi, author a, publisher p WHERE b.isbn = bi.isbn";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BookInfoDTO1 dto = new BookInfoDTO1();
				
				dto.setBook_code(rs.getInt("book_code"));
				dto.setIsbn(rs.getString("isbn"));
				dto.setBookName(rs.getString("bookname"));
				dto.setAuthor_name(rs.getString("author_name"));
				dto.setPublisher_name(rs.getString("publisher_name"));
				dto.setPublish_date(rs.getString("publisher_date"));
				
				list.add(dto);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();		
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}

	@Override
	// 대출신청
	// 대출관리코드, 도서코드, user_code, checkout_date, 
	public void insertloan(LoanDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			// INSERT INTO 테이블명(컬럼명, 컬러명) VALUES (값1, 값2)
			// INSERT ALL INTO 테이블명1(컬럼, 컬럼) VALUES(값, 값) INTO 테이블명2(컬럼, 컬럼) VALUES(값, 값);
			sql = "INSERT INTO loan(loan_code, book_code, user_code, checkout_date, due_date, isExtended) VALUES(loan_seq.nextval, ?, ?, sysdate, sysdate + 14, 0)";
			
		    pstmt = conn.prepareStatement(sql);
		    
		    pstmt.setInt(1, dto.getBook_code());
		    pstmt.setString(2, dto.getUser_code());
		    
		    pstmt.executeUpdate();
		    
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return;
	}

	@Override
	// 대출연장
	// 회원번호, 회원이름, 도서번호, 도서제목, 대출일짜, 반납예정일짜, 실제반납일짜, 대출연장남은회기, 연체대출불가날짜
	public List<LoanDTO> listloan(String user_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT u.user_code, user_name, b.book_code, bookname, checkout_date, due_date, return_date, ixextended, loan_renewaldate FROM bookinfo b, loan l JOIN user_info u ON u.user_code = l.user_code JOIN book b ON b.book_code = l.book_code";
					
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LoanDTO dto = new LoanDTO();
				
				dto.setUser_code(rs.getString("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReturn_date(rs.getString("return_date"));
				dto.setIsExtended(rs.getString("ixextended"));
				dto.setLoan_renewaldate(rs.getString("loan_renewaldate"));
				
				list.add(dto);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}


}
