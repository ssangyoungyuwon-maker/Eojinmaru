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

			sql = "SELECT b.book_code, bi.isbn, bi.bookName, "
					+ "LISTAGG(a.author_name, ', ') WITHIN GROUP (ORDER BY a.author_name) AS author_list, "
					+ "p.publisher_name, TO_CHAR(bi.publish_date, 'YYYY-MM-DD') publish_date, b.book_condition "
					+ "FROM book b "
					+ "LEFT OUTER JOIN bookinfo bi ON b.isbn = bi.isbn "
					+ "LEFT OUTER JOIN author a ON bi.isbn = a.isbn "
					+ "LEFT OUTER JOIN publisher p ON bi.publisher_id = p.publisher_id "
					+ "WHERE INSTR(bi.bookName, ?) >= 1 OR INSTR(a.author_name, ?) >= 1 "
					+ "GROUP BY b.book_code, bi.isbn, bi.bookName, p.publisher_name, bi.publish_date, b.book_condition "
					+ "ORDER BY b.book_code ";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, search);
			pstmt.setString(2, search);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				BookInfoDTO dto = new BookInfoDTO();

				dto.setBook_code(rs.getInt("book_code"));
				dto.setIsbn(rs.getString("isbn"));
				dto.setBookName(rs.getString("bookname"));
				dto.setAuthor_name(rs.getString("author_list"));
				dto.setPublisher_name(rs.getString("publisher_name"));
				dto.setPublish_date(rs.getString("publish_date"));
				dto.setBook_condition(rs.getString("book_condition"));

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
			sql = "SELECT b.book_code, bookName, book_condition " + " FROM book b "
					+ " JOIN bookinfo bi ON b.isbn = bi.isbn " + " WHERE INSTR(book_code, ?) >= 1 AND book_condition = '대출가능'";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, bookcode);

			rs = pstmt.executeQuery();

			while (rs.next()) {
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
	// 대출가능 도서 검색
	public List<LoanDTO> loanlistbook(int bookcode) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT b.book_code, bookName, book_condition FROM book b "
					+ " JOIN bookinfo bi ON b.isbn = bi.isbn " + " WHERE book_code = ? AND book_condition = '대출가능'";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, bookcode);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();

				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookName"));
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

// ----------------------------------------------------------------------------------------------------------------------------------
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
			pstmt.close();

			sql = "UPDATE book set book_condition = '대출중' where book_code = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, dto.getBook_code());

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
	public void extendloan(int loan_code) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "UPDATE loan SET isExtended = 1, due_date = due_date + 7 WHERE loan_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, loan_code);
			pstmt.executeUpdate();

		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}

	}

	@Override
	// 전체 대출리스트(대출예약용)
	// 회원번호, 회원이름, 도서번호, 도서제목, 대출일짜, 반납예정일짜, 실제반납일짜, 대출연장남은회기, 연체대출불가날짜
	public List<LoanDTO> listloan(int book_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT loan_code, l.user_code, user_name, b.book_code, bookname, "
					+ " TO_CHAR(checkout_date, 'YYYY-MM-DD') checkout_date, "
					+ " TO_CHAR(due_date, 'YYYY-MM-DD') due_date, "
					+ " TO_CHAR(return_date, 'YYYY-MM-DD') return_date, " + " book_condition " + " FROM loan l "
					+ " JOIN userinfo u ON u.user_code = l.user_code " + " JOIN book b ON b.book_code = l.book_code "
					+ " JOIN bookinfo bi ON bi.ISBN = b.ISBN "
				    + " WHERE l.book_code = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();

				dto.setLoan_code(rs.getInt("loan_code"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReservation_date(rs.getString("return_date"));
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

	// 회원이 대출중인 리스트(대출연장용)
	public List<LoanDTO> listloaning(int user_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT Loan_code, l.user_code, user_name, b.book_code, bookname, "
					+ " TO_CHAR(checkout_date, 'YYYY-MM-DD') checkout_date, "
					+ " TO_CHAR(due_date, 'YYYY-MM-DD') due_date, "
					+ " TO_CHAR(return_date, 'YYYY-MM-DD') return_date, " + " isExtended " + " FROM loan l "
					+ " JOIN userinfo u ON u.user_code = l.user_code " + " JOIN book b ON b.book_code = l.book_code "
					+ " JOIN bookinfo bi ON bi.ISBN = b.ISBN " + " WHERE l.user_code = ? AND return_date is null";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();

				dto.setLoan_code(rs.getInt("Loan_code"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReturn_date(rs.getString("return_date"));
				dto.setIsExtended(rs.getInt("isExtended"));

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

// ---------------------------------------------------------------------------------------------------------------------------------		

	@Override
	// 도서상태(대출 중인 도서) 예약 신청
	public void loanreservation(LoanDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

			try {
				
			sql = "UPDATE loanreservation SET reservation_date = ? WHERE book_code = ?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getDue_date());
			pstmt.setInt(2, dto.getBook_code());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}
		return;
	}


	@Override
	// 대출 신청시 패널티(연체 중 회원)
	public List<LoanDTO> overdue(int user_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT Loan_code, l.user_code, user_name, b.book_code, bookname, "
					+ " TO_CHAR(checkout_date, 'YYYY-MM-DD') checkout_date, "
					+ " TO_CHAR(due_date, 'YYYY-MM-DD') due_date, "
					+ " TO_CHAR(return_date, 'YYYY-MM-DD') return_date, "
					+ " isExtended, TO_CHAR(loan_renewaldate, 'YYYY-MM-DD') loan_renewaldate " + " FROM loan l "
					+ " JOIN userinfo u ON u.user_code = l.user_code " + " JOIN book b ON b.book_code = l.book_code "
					+ " JOIN bookinfo bi ON bi.ISBN = b.ISBN "
					+ " WHERE l.user_code = ? AND return_date IS null AND (TO_CHAR(due_date, 'YYYY-MM-DD') < TO_CHAR(SYSDATE, 'YYYY-MM-DD'))";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();

				dto.setLoan_code(rs.getInt("Loan_code"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReturn_date(rs.getString("return_date"));
				dto.setIsExtended(rs.getInt("isExtended"));
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
	// 대출 예약 중인 도서 대출신청 시 불가
	public List<LoanDTO> loanreservationbook(int book_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT l.loan_code, l.book_code, bi.bookname, l.user_code, b.book_condition, "
					+ " TO_CHAR(l.due_date, 'YYYY-MM-DD') due_date, "
					+ " TO_CHAR(lr.reservation_date, 'YYYY-MM-DD') reservation_date " + " FROM loan l "
					+ " JOIN book b ON b.book_code = l.book_code " + " JOIN bookinfo bi ON bi.ISBN = b.ISBN "
					+ " LEFT JOIN loanreservation lr ON lr.book_code = l.book_code "
					+ " WHERE l.book_code = ? AND reservation_date IS NOT null AND TO_CHAR(reservation_date, 'YYYY-MM-DD') = TO_CHAR(SYSDATE, 'YYYY-MM-DD')";

			pstmt = conn.prepareStatement(sql);

			pstmt.setInt(1, book_code);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();
				dto.setLoan_code(rs.getInt("Loan_code"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setBook_condition(rs.getString("book_condition"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReservation_date(rs.getString("reservation_date"));

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
	// 대출불가 날짜가 존재하고 오늘 이후 날짜를 가지고 있는 회원
	public List<LoanDTO> renwaldate(int user_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT Loan_code, l.user_code, user_name, b.book_code, bookname, "
					+ " TO_CHAR(checkout_date, 'YYYY-MM-DD') checkout_date, "
					+ " TO_CHAR(due_date, 'YYYY-MM-DD') due_date, "
					+ " TO_CHAR(return_date, 'YYYY-MM-DD') return_date, "
					+ " isExtended, TO_CHAR(loan_renewaldate, 'YYYY-MM-DD') loan_renewaldate " + " FROM loan l "
					+ " JOIN userinfo u ON u.user_code = l.user_code " + " JOIN book b ON b.book_code = l.book_code "
					+ " JOIN bookinfo bi ON bi.ISBN = b.ISBN "
					+ " WHERE l.user_code = ? AND TO_CHAR(loan_renewaldate, 'YYYY-MM-DD') >= TO_CHAR(SYSDATE, 'YYYY-MM-DD')";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();

				dto.setLoan_code(rs.getInt("Loan_code"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReturn_date(rs.getString("return_date"));
				dto.setIsExtended(rs.getInt("isExtended"));
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
	// 대출도서 개수 5권 이상 대출 불가
	public int loancount(int user_code) {
		int count = 0;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			
			sql = "SELECT COUNT(loan_code) totalcont FROM loan WHERE user_code = ? AND return_date IS NULL";
			
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setInt(1, user_code);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				count = rs.getInt(1);
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return count;
	}

	@Override
	public List<LoanDTO> loanlistall(String bookname) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT loan_code, l.user_code, user_name, b.book_code, bookname, "
					+ " TO_CHAR(checkout_date, 'YYYY-MM-DD') checkout_date, "
					+ " TO_CHAR(due_date, 'YYYY-MM-DD') due_date, "
					+ " TO_CHAR(return_date, 'YYYY-MM-DD') return_date, " + " book_condition " + " FROM loan l "
					+ " JOIN userinfo u ON u.user_code = l.user_code " + " JOIN book b ON b.book_code = l.book_code "
					+ " JOIN bookinfo bi ON bi.ISBN = b.ISBN " 
					+ " WHERE INSTR(bookName, ?) >= 1 AND checkout_date IS NOT null AND book_condition = '대출중' ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, bookname);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();

				dto.setLoan_code(rs.getInt("loan_code"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReservation_date(rs.getString("return_date"));
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
	public List<LoanDTO> loanlistcode(int book_code) {
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT loan_code, l.user_code, user_name, b.book_code, bookname, "
					+ " TO_CHAR(checkout_date, 'YYYY-MM-DD') checkout_date, "
					+ " TO_CHAR(due_date, 'YYYY-MM-DD') due_date, "
					+ " TO_CHAR(return_date, 'YYYY-MM-DD') return_date, " + " book_condition " + " FROM loan l "
					+ " JOIN userinfo u ON u.user_code = l.user_code " + " JOIN book b ON b.book_code = l.book_code "
					+ " JOIN bookinfo bi ON bi.ISBN = b.ISBN " 
					+ " WHERE INSTR(b.book_code, ?) >= 1 AND checkout_date IS NOT null AND book_condition = '대출중' ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();

				dto.setLoan_code(rs.getInt("loan_code"));
				dto.setUser_code(rs.getInt("user_code"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setReservation_date(rs.getString("return_date"));
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

}


