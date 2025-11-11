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
	Connection conn = DBConn.getConnection();

	private LoginInfo login = null;

	// UI
	public ReturnUI(LoginInfo login) {
		this.login = login;
	}

	public void start() {
		System.out.println("\n[도서반납]");

		try {

			MemberDTO loginuser = login.loginUser();

			List<LoanDTO> list = showbooksonloan(loginuser.getUser_code());

			if (list.size() == 0) {
				System.out.println("대출중인 도서가 없습니다.\n");
				return;
			}

			System.out.println("회원번호\t대출번호\t책이름\t대출일자\t반납예정일\t대출연장여부");
			System.out.println("----------------------------------------------");
			for (LoanDTO dto : list) {
				System.out.print(dto.getUser_code() + "\t");
				System.out.print(dto.getCheckout_date() + "\t");
				System.out.print(dto.getBookname() + "\t");
				System.out.print(dto.getCheckout_date() + "\t");
				System.out.print(dto.getDue_date() + "\t");
				System.out.println(dto.getIsExtended());
			}

			System.out.println("반납할 책 번호를 입력하세요");
			int book_code =Integer.parseInt( br.readLine());
			boolean b = false;
			
			for (LoanDTO dto : list) {
				if(dto.getBook_code()==book_code) {
					b=true;
					break;
				}
			}			
			
			if(b==false) {
				System.out.println("대출 책이 아닙니다.");
				return;
			}
			
			returnbook(loginuser.getUser_code(),book_code);
			
			System.out.println("반납이 완료되었습니다.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// DAO
	public List<LoanDTO> showbooksonloan(int user_code) throws SQLException {
		LoanDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			List<LoanDTO> list = new ArrayList<LoanDTO>();

			sql = "SELECT l.user_code, loan_code, book_name, checkout_date, due_date, isExtended  "
					+ " FROM Loan l JOIN Book b ON l.book_code=b.book_code WHERE user_code=? AND return_date IS NULL";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				dto = new LoanDTO();

				dto.setUser_code(rs.getInt("user_code"));
				dto.setLoan_code(rs.getInt("loan_code"));
				dto.setBookname(rs.getString("bookname"));
				dto.setCheckout_date(rs.getString("checkout_date"));
				dto.setDue_date(rs.getString("due_date"));
				dto.setIsExtended(rs.getInt("isExtended"));

				list.add(dto);
			}
			return list;
		} catch (SQLException e) {
			throw e;
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

	}

	public void returnbook(int user_code, int book_code) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		conn.setAutoCommit(false);
		try {

			sql = "UPDATE loan SET return_date = sysdate WHERE user_code=? ";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, user_code);
			pstmt.executeUpdate();

			sql = "UPDATE book SET book_condition='반납' WHERE book_code=?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, book_code);
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
}
