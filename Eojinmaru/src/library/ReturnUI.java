package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class ReturnUI {

	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private LoginInfo login = null;

	// UI
	public ReturnUI(LoginInfo login) {
		this.login = login;
	}

	public void start() {
		System.out.println("\n[도서반납]");
		MemberDTO loginuser = login.loginUser();

		try {
			List<LoanDTO> list = showbooksonloan(loginuser.getUser_code());
			if (list.size() == 0) {
				System.out.println("대출중인 도서가 없습니다.\n");
				return;
			}
			System.out.println("\n\t\t\t\t\t📚 [ 대출 리스트 ] \t\t\t\t\t");
			System.out.println("=================================================================================");
			System.out.println("|회원번호\t|책번호\t|책이름\t\t|\t대출일자\t|\t반납예정일\t|\t대출연장여부|");
			System.out.println("=================================================================================");

			for (LoanDTO dto : list) {
				System.out.print(dto.getUser_code() + "\t");
				System.out.print(dto.getBook_code() + "\t");
				System.out.print(dto.getBookname() + "\t\t");
				System.out.print(dto.getCheckout_date() + "\t");
				System.out.print(dto.getDue_date() + "\t");
				System.out.println(dto.getIsExtended() + "회");
			}
			System.out.println("=================================================================================");
			System.out.println("\n반납할 책 번호를 입력하세요");
			int book_code = Integer.parseInt(br.readLine());
			boolean b = false;

			for (LoanDTO dto : list) {
				if (dto.getBook_code() == book_code) {
					b = true;
					break;
				}
			}
			
			if (b == false) {
				System.out.println("대출 책이 아닙니다.");
				return;
			}

			returnbook(loginuser.getUser_code(), book_code);

			System.out.println("반납이 완료되었습니다.");
			chkOverdue(loginuser.getUser_code());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// DAO
	public List<LoanDTO> showbooksonloan(int user_code) throws SQLException {
		Connection conn = DBConn.getConnection();
		List<LoanDTO> list = new ArrayList<LoanDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT l.user_code, l.book_code, i.bookname, TO_CHAR(l.checkout_date,'YYYY-MM-DD')checkoutdate, TO_CHAR(l.due_date,'YYYY-MM-DD')duedate, l.isExtended  "
					+ " FROM Loan l " + " JOIN Book b ON l.book_code=b.book_code "
					+ " JOIN BOOKINFO i ON i.ISBN=b.ISBN " + " WHERE l.user_code=? AND return_date IS NULL";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);

			rs = pstmt.executeQuery();

			while (rs.next()) {
				LoanDTO dto = new LoanDTO();

				dto.setUser_code(rs.getInt("user_code"));
				dto.setBook_code(rs.getInt("book_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkoutdate"));
				dto.setDue_date(rs.getString("duedate"));
				dto.setIsExtended(rs.getInt("isExtended"));

				list.add(dto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return list;

	}

	public void returnbook(int user_code, int book_code) throws SQLException {
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;

		conn.setAutoCommit(false);
		try {

			sql = "UPDATE loan SET return_date = sysdate WHERE user_code=? AND book_code=? AND return_date IS NULL";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);
			pstmt.setInt(2, book_code);
			pstmt.executeUpdate();

			sql = "UPDATE book SET book_condition='반납' WHERE book_code=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
			pstmt.executeUpdate();

			sql = "UPDATE userinfo SET loan_renewaldate=sysdate+(SELECT CASE WHEN(return_date-due_date)>0 THEN return_date-due_date ELSE 0 END "
					+ " FROM loan WHERE book_code=? AND user_code=? ) WHERE user_code = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
			pstmt.setInt(2, user_code);
			pstmt.setInt(3, user_code);
			pstmt.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			conn.rollback();
			throw e;
		} finally {
			DBUtil.close(pstmt);
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}

	public void chkOverdue(int user_code) throws SQLException {
		Connection conn = DBConn.getConnection();
		PreparedStatement pstmt = null;
		String sql;
		ResultSet rs = null;
		try {
			sql = "SELECT CEIL(loan_renewaldate-sysdate) diff, TO_CHAR(TO_DATE(loan_renewaldate,'RR-MM-DD'),'YYYY-MM-DD') rdate FROM userinfo WHERE user_code=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				int diff = rs.getInt("diff");
				String rdate = rs.getString("rdate");

				if (diff > 0) {
					System.out.println("[" + diff + "]일 연체되어 [" + rdate + "]부터 대출가능합니다.");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
	}
}