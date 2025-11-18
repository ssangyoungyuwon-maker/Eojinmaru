package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class SincheongUI {
	
	
	public void sincheongUI() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String request;

		while (true) {
			try {
				System.out.println("\n[도서신청]");
				System.out.println("신청할 도서의 제목과 저자를 입력하세요.[종료:q]");
				request = br.readLine();
				

				if (request.equalsIgnoreCase("q")) {
					System.out.println("❌ 종료되어 이전 화면으로 돌아갑니다.");
					return;
				}
				request(request);
				System.out.println("✔ ["+request +"] 신청이 완료 되었습니다.");
				return;

			} catch (SQLException e) {
				System.out.println("❌ 오류 발생 : 도서 신청에 실패했습니다.");
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	// DAO
	public void request(String request) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;
		String sql;
		
		
		try {
			conn = DBConn.getConnection();
			sql = "INSERT INTO sincheong (sincheong_code, sincheong_name, sincheong_status) VALUES (sincheong_seq.NEXTVAL,?,'대기')";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, request);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			throw e;
		}finally {
			DBUtil.close(pstmt);

		}
	}
}
