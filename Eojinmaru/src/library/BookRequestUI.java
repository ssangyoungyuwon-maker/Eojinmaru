package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class BookRequestUI {
	private Connection conn = DBConn.getConnection();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
    // 신청 도서 정보 받기
    protected void request() throws Exception {
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
    		System.out.print("신청할 도서 정보를 입력해주세요 \n 도서정보 : ");
    		String bookInfo = br.readLine().trim();
    		
    		if (bookInfo.isEmpty()) {
    			throw new Exception("✅ 입력된 내용이 없어 도서 신청이 취소되었습니다.");
    		}
    		
    		sql = "INSERT INTO Sincheong (sincheong_code, sincheong_name, sincheong_status) VALUES (sincheong_seq.NEXTVAL, ?, '대기')";
    		
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setString(1, bookInfo);
    		
    		if(pstmt.executeUpdate() <= 0) {
    			throw new Exception("❌ 도서 신청에 실패했습니다.");
    		}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
    }

}
