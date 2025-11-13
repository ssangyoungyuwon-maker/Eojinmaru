package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class BookDAOImpl implements BookDAO {
	private Connection conn = DBConn.getConnection();

	@Override
	// 도서검색(제목/저자)
	public List<BookInfoDTO> listBook(String search) {
		List<BookInfoDTO> list = new ArrayList<BookInfoDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			  // 도서번호, isbn, 도서이름, 저자, 출판사, 발행일를 출력

			 sql = "SELECT b.book_code, bi.isbn, bookName, author_name, publisher_name, TO_CHAR(publish_date, 'YYYY-MM-DD') publish_date "
					+ " FROM book b"
					+ " left outer JOIN bookinfo bi ON b.isbn = bi.isbn "
					+ " left outer JOIN author a ON bi.isbn = a.isbn "
					+ " left outer JOIN publisher p ON bi.publisher_id = p.publisher_id "
					+ " WHERE INSTR(bookName, ?) >= 1 OR INSTR(author_name, ?) >= 1 ";
	
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, search);
			pstmt.setString(2, search);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BookInfoDTO dto = new BookInfoDTO();
				
				dto.setBook_code(rs.getInt("book_code"));
				dto.setIsbn(rs.getString("isbn"));
				dto.setBookName(rs.getString("bookname"));
				dto.setAuthor_name(rs.getString("author_name"));
				dto.setPublisher_name(rs.getString("publisher_name"));
				dto.setPublish_date(rs.getString("publish_date"));
				
				list.add(dto);
			}
	
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}
	
	@Override
	// 대출 신청 전 책 리스트 조회

	public List<BookInfoDTO> loanBook(int bookcode) {
		List<BookInfoDTO> list = new ArrayList<BookInfoDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT b.book_code, bookName, book_condition "
					+ " FROM book b "
					+ " JOIN bookinfo bi ON b.isbn = bi.isbn "
					+ " WHERE INSTR(book_code, ?) >= 1";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, bookcode);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BookInfoDTO dto = new BookInfoDTO();
				
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookName(rs.getString("bookName"));
				dto.setBook_condition(rs.getString("book_condition"));
				
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


	@Override
	// 대출신청
	// 대출관리코드, 도서코드, user_code, checkout_date, 
	public void insertloan(LoanDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			// INSERT INTO 테이블명(컬럼명, 컬러명) VALUES (값1, 값2)
			// INSERT ALL INTO 테이블명1(컬럼, 컬럼) VALUES(값, 값) INTO 테이블명2(컬럼, 컬럼) VALUES(값, 값);
			sql = "INSERT INTO loan(loan_code, book_code, user_code, checkout_date, due_date, isExtended) "
					+ " VALUES(loan_seq.nextval, ?, ?, sysdate, sysdate + 14, 0)";
			
		    pstmt = conn.prepareStatement(sql);
		    
		    pstmt.setInt(1, dto.getBook_code());
		    pstmt.setInt(2, dto.getUser_code());
		    
		    pstmt.executeUpdate();
		    
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
		
		return;
	}

	@Override
	// 전체 대출리스트
	// 회원번호, 회원이름, 도서번호, 도서제목, 대출일짜, 반납예정일짜, 실제반납일짜, 대출연장남은회기, 연체대출불가날짜
	public List<LoanDTO> listloan(int user_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT Loan_code, l.user_code, user_name, b.book_code, bookname, TO_CHAR(checkout_date, 'YYYY-MM-DD') checkout_date, TO_CHAR(due_date, 'YYYY-MM-DD') due_date, TO_CHAR(return_date, 'YYYY-MM-DD') return_date, TO_CHAR(loan_renewaldate, 'YYYY-MM-DD') loan_renewaldate "
					+ " FROM loan l "
					+ " JOIN userinfo u ON u.user_code = l.user_code "
					+ " JOIN book b ON b.book_code = l.book_code "
					+ " JOIN bookinfo bi ON bi.ISBN = b.ISBN "
					+ " WHERE l.user_code = ?";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LoanDTO dto = new LoanDTO();
				
				dto.setLoan_code(rs.getInt("Loan_code"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReservation_date(rs.getString("return_date"));
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
	
	// 회원 대출리스트
	public List<LoanDTO> listloaning(int book_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT Loan_code, l.user_code, user_name, b.book_code, bookname, TO_CHAR(checkout_date, 'YYYY-MM-DD') checkout_date, TO_CHAR(due_date, 'YYYY-MM-DD') due_date, TO_CHAR(return_date, 'YYYY-MM-DD') return_date, ixextended, TO_CHAR(loan_renewaldate, 'YYYY-MM-DD') loan_renewaldate "
					+ " FROM loan l "
					+ " JOIN user_info u ON u.user_code = l.user_code "
					+ " JOIN book b ON b.book_code = l.book_code "
					+ " JOIN bookinfo bi ON bi.ISBN = b.ISBN "
					+ " WHERE b.book_code = ? AND return_date is null";
					
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				LoanDTO dto = new LoanDTO();
				
				dto.setLoan_code(rs.getInt("Loan_code"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReservation_date(rs.getString("return_date"));
				dto.setIsExtended(rs.getInt("ixextended"));
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

	@Override
	// 도서상태(대출중) 리스트
	public List<LoanDTO> loaning(String book_condition) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT b.book_code, bi.bookName, b.book_condition, TO_CHAR(l.due_date, 'YYYY-MM-DD') "
					+ " FROM book b "
					+ " JOIN bookinfo bi ON b.isbn = bi.isbn "
					+ " JOIN loan l ON b.book_code = l.book_code "
					+ " WHERE b.book_condition = '대출중'";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(true) {
				LoanDTO dto = new LoanDTO();
				
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookName"));
				dto.setBook_condition(rs.getString("book_condition"));
				dto.setDue_date(rs.getString("due_date"));
				
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
	
	@Override
	// 도서상태(대출 중인 도서) 예약 신청
	public void loanreservation(LoanDTO dto) throws SQLException {
		
	}

	@Override
	public LoanDTO bookLoaning(int user_code) {
		LoanDTO dto = null;
		
		// 연체여부 확인
		try {
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return dto;
	}

	@Override
	public List<LoanDTO> listloan(String user_code) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
