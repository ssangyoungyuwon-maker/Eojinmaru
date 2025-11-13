package library;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class AdminDAOImpl2 implements AdminDAO2 {
	private Connection conn = DBConn.getConnection();

	@Override
	public List<AdminDTO> sinchoengdaegidoseo() {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT sincheong_code, sincheong_name, sincheong_status FROM sincheong WHERE sincheong_status = '대기'";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();

				dto.setSincheongcode(rs.getInt("SINCHEONG_CODE"));
				dto.setSincheongbook(rs.getString("SINCHEONG_NAME"));
				dto.setSincheongstatus(rs.getString("SINCHEONG_STATUS"));

				list.add(dto);
			}
			
		} catch (Exception e) {
		} finally {
			DBUtil.close(pstmt);
		}

		return list;
	}

	@Override
	public String truncateString(String text, int maxLength) {
		if (text == null) {
			text = "";
		}
		if (text.length() > maxLength) {
			if (maxLength < 3) {
				return text.substring(0, maxLength);
			}
			return text.substring(0, maxLength - 3) + "...";
		}
		if (text.length() < maxLength) {
			StringBuilder sb = new StringBuilder(text);
			int paddingLength = maxLength - text.length();
			for (int i = 0; i < paddingLength; i++) {
				sb.append("　");
			}
			return sb.toString();
		}
		return text;
	}

	@Override
	public int sujeongsincheongstatus(AdminDTO dto) throws SQLException {
		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL UPDATESINCHEONGSTATUS (? , ?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, dto.getSincheongcode());
			cstmt.setString(2, dto.getSincheongstatus());

			cstmt.executeUpdate();

			result = 1;

		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
		} finally {
			DBUtil.close(cstmt);
		}

		return result;
	}

	@Override
	public List<AdminDTO> notice() {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NOTICE_ID, NOTICE_TITLE, NOTICE_CONTENT, "
					+ "to_char(NOTICE_DATE, 'yyyy-mm-dd') as NOTICE_DATE FROM Notice ORDER BY NOTICE_ID desc";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();

				dto.setNoticeId(rs.getInt("NOTICE_ID"));
				dto.setNoticeTitle(rs.getString("NOTICE_TITLE"));
				dto.setNoticeContent(rs.getString("NOTICE_CONTENT"));
				dto.setNoticeDate(rs.getString("NOTICE_DATE"));

				list.add(dto);
			}
		} catch (Exception e) {
		} finally {
			DBUtil.close(pstmt);
		}

		return list;
	}

	@Override
	public AdminDTO selectNoticeById(int noticeId) {

		AdminDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NOTICE_ID, NOTICE_TITLE, NOTICE_CONTENT, TO_CHAR(NOTICE_DATE, 'YYYY-MM-DD') AS NOTICE_DATE FROM Notice WHERE NOTICE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new AdminDTO();

				dto.setNoticeId(rs.getInt("NOTICE_ID"));
				dto.setNoticeTitle(rs.getString("NOTICE_TITLE"));
				dto.setNoticeContent(rs.getString("NOTICE_CONTENT"));
				dto.setNoticeDate(rs.getString("NOTICE_DATE"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}

		return dto;
	}

	@Override
	public int noticeInsert(AdminDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO NOTICE (notice_title, notice_content, notice_date) VALUES (?, ?, SYSDATE) ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getNoticeTitle());
			pstmt.setString(2, dto.getNoticeContent());
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

		return result;
	}

	@Override
	public int noticeUpdate(AdminDTO dto) {

		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL UPDATENOTICE (?, ?, ?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, dto.getNoticeId());
			cstmt.setString(2, dto.getNoticeTitle());
			cstmt.setString(3, dto.getNoticeContent());

			cstmt.executeUpdate();

			result = 1;

		} catch (SQLException e) {
			System.err.println("SQL 예외가 발생했습니다. : " + e.getMessage());
		} catch (Exception e) {
		} finally {
			DBUtil.close(cstmt);
		}
		return result;
	}

	@Override
	public int noticeDelete(int noticeId) {

		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL DELETENOTICE (?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, noticeId);

			cstmt.executeUpdate();

			result = 1;

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
		} finally {
			DBUtil.close(cstmt);
		}
		return result;
	}

	@Override
	public List<AdminDTO> loanbooklist() {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수" 
					+ " FROM " 
					+ "    ("
					+ "     SELECT" 
					+ "        ui.user_name AS 유저이름, " 
					+ "        lon.LOAN_CODE AS 대출번호, "
					+ "        lon.book_code AS 북코드, " 
					+ "        bi.bookname AS 책이름, "
					+ "        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일, "
					+ "        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일, "
					+ "        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일, "
					+ "        bk.book_condition AS 도서상태, " 
					+ "            CASE "
					+ "                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE) "
					+ "                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE) "
					+ "                ELSE 0 " 
					+ "            END AS 연체일수 " 
					+ "        FROM loan lon "
					+ "        JOIN userinfo ui ON ui.user_code = lon.user_code "
					+ "        JOIN book bk ON bk.book_code = lon.book_code "
					+ "        JOIN bookinfo bi ON bi.isbn = bk.isbn" 
					+ "    ) T " 
					+ " WHERE T.도서상태 = '대출중'  AND (T.실제반납일 IS NULL OR T.실제반납일 > TO_CHAR(SYSDATE, 'YYYY-MM-DD')) "; //

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();
				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setDue_date(rs.getString("반납예정일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.err.println("Error in loanbooklist: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return list;
	}

	@Override
	public AdminDTO loanbooksearchbybookcode(int bookcode) {

		AdminDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT " + "    T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수 " + " FROM "
					+ "    ( " + "     SELECT " + "        ui.user_name AS 유저이름, " + "        lon.LOAN_CODE AS 대출번호, "
					+ "        lon.book_code AS 북코드, " + "        bi.bookname AS 책이름, "
					+ "        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일, "
					+ "        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일, "
					+ "        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일, "
					+ "        bk.book_condition AS 도서상태, " + "            CASE "
					+ "                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE)"
					+ "                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE)"
					+ "                ELSE 0 " + "            END AS 연체일수 " + "        FROM loan lon "
					+ "        JOIN userinfo ui ON ui.user_code = lon.user_code"
					+ "        JOIN book bk ON bk.book_code = lon.book_code"
					+ "        JOIN bookinfo bi ON bi.isbn = bk.isbn" + "    ) T " 
					+ " WHERE 북코드 = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookcode);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new AdminDTO();

				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setDue_date(rs.getString("반납예정일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}

	@Override
	public int loanbookreturn(AdminDTO dto) {

		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL loanbookreturn (?, ?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, dto.getBookcode());
			cstmt.setString(2, dto.getBook_condition());

			cstmt.executeUpdate();

			result = 1;

		} catch (SQLException e) {
			System.err.println("SQL 예외가 발생했습니다. : " + e.getMessage());
		} catch (Exception e) {
		} finally {
			DBUtil.close(cstmt);
		}
		return result;
	}

	@Override
	public List<AdminDTO> loanbooksearchbyname(String username) {

		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수 "
					+ "					 FROM " 
					+ "					   (  " 
					+ "					    SELECT  "
					+ "					        ui.user_name AS 유저이름,  "
					+ "					       lon.LOAN_CODE AS 대출번호,  "
					+ "					        lon.book_code AS 북코드,  "
					+ "					     bi.bookname AS 책이름,  "
					+ "					        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일,  "
					+ "					        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일,  "
					+ "					        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일,  "
					+ "					        bk.book_condition AS 도서상태,  " + "					            CASE  "
					+ "					                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE) "
					+ "					                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE) "
					+ "					                ELSE 0  " + "					            END AS 연체일수  "
					+ "					        FROM loan lon  "
					+ "					        JOIN userinfo ui ON ui.user_code = lon.user_code "
					+ "					        JOIN book bk ON bk.book_code = lon.book_code "
					+ "					        JOIN bookinfo bi ON bi.isbn = bk.isbn " 
					+ "					    ) T  "
					+ "					WHERE 유저이름 LIKE ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + username + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();

				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setDue_date(rs.getString("반납예정일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));
				
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
	public List<AdminDTO> overdueloanbooklist() {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수" 
					+ " FROM " 
					+ "    ("
					+ "     SELECT" 
					+ "        ui.user_name AS 유저이름, " 
					+ "        lon.LOAN_CODE AS 대출번호, "
					+ "        lon.book_code AS 북코드, " 
					+ "        bi.bookname AS 책이름, "
					+ "        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일, "
					+ "        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일, "
					+ "        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일, "
					+ "        bk.book_condition AS 도서상태, " 
					+ "            CASE "
					+ "                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE) "
					+ "                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE) "
					+ "                ELSE 0 " 
					+ "            END AS 연체일수 " 
					+ "        FROM loan lon "
					+ "        JOIN userinfo ui ON ui.user_code = lon.user_code "
					+ "        JOIN book bk ON bk.book_code = lon.book_code "
					+ "        JOIN bookinfo bi ON bi.isbn = bk.isbn" 
					+ "    ) T " 
					+ " WHERE 연체일수 >= 1 "; //

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();
				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setDue_date(rs.getString("반납예정일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.err.println("Error in loanbooklist: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return list;
	}
	
	@Override
	public List<AdminDTO> returnbooklist() {
	
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수" 
					+ " FROM " 
					+ "    ("
					+ "     SELECT" 
					+ "        ui.user_name AS 유저이름, " 
					+ "        lon.LOAN_CODE AS 대출번호, "
					+ "        lon.book_code AS 북코드, " 
					+ "        bi.bookname AS 책이름, "
					+ "        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일, "
					+ "        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일, "
					+ "        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일, "
					+ "        bk.book_condition AS 도서상태, " 
					+ "            CASE "
					+ "                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE) "
					+ "                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE) "
					+ "                ELSE 0 " 
					+ "            END AS 연체일수 " 
					+ "        FROM loan lon "
					+ "        JOIN userinfo ui ON ui.user_code = lon.user_code "
					+ "        JOIN book bk ON bk.book_code = lon.book_code "
					+ "        JOIN bookinfo bi ON bi.isbn = bk.isbn" 
					+ "    ) T " 
					+ " WHERE T.도서상태 = '반납' "; //

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();
				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.err.println("Error in loanbooklist: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return list;
	}

	@Override
	public int returnbook_baega(AdminDTO dto) {

		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL returnbook_baega (?, ?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, dto.getBookcode());
			cstmt.setString(2, dto.getBook_condition());

			cstmt.executeUpdate();

			result = 1;

		} catch (SQLException e) {
			System.err.println("SQL 예외가 발생했습니다. : " + e.getMessage());
		} catch (Exception e) {
		} finally {
			DBUtil.close(cstmt);
		}
		return result;
	}

	@Override
	public int returnbook_baega_all(AdminDTO dto) {


		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL returnbook_baega (?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setString(2, dto.getBook_condition());

			cstmt.executeUpdate();

			result = 1;

		} catch (SQLException e) {
			System.err.println("SQL 예외가 발생했습니다. : " + e.getMessage());
		} catch (Exception e) {
		} finally {
			DBUtil.close(cstmt);
		}
		return result;
	}
	
	
}