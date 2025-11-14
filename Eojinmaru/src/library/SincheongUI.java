package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import DBUtil.DBConn;

public class SincheongUI {
	private Connection conn = DBConn.getConnection();
	
	public void sincheongUI() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		SincheongDTO dto = new SincheongDTO();
	
		String request;
		

		while (true) {
			try {
				
				System.out.print("신청할 도서의 제목과 저자를 입력하세요.[종료:q]");
				request = dto.setRequest(br.readLine());
				

				if (request.equalsIgnoreCase("q")) {
					System.out.println("이전 화면으로 돌아갑니다.");
					return;
				}
				
				System.out.println(request + "이 신청되었습니다.");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// DAO

	public void insertSincheong(String request) throws SQLException {
		
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			sql = "INSERT INTO sincheong (sincheong_name,sincheong_status) VALUES (?,'대기')";
			pstmt.setString(1, request);
			pstmt = conn.prepareStatement(sql);
			
			pstmt.executeUpdate();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// DTO
	public class SincheongDTO {

		private String request;

		public String getRequest() {
			return request;
		}

		public String setRequest(String request) {
			return this.request = request;
		}
	}
}
