package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class BookRequestUI {
	
	public static final String ANSI_RESET = "\u001B[0m"; // 스타일 초기화

	// 텍스트 색상
	public static final String ANSI_BLUE = "\u001B[34m"; // 파란색
	public static final String ANSI_YELLOW = "\u001B[33m"; // 노란색

	// 스타일
	public static final String ANSI_BOLD = "\u001B[1m"; // 굵게
	public static final String ANSI_ITALIC = "\u001B[3m";  // 이탤릭체
	public static final String ANSI_UNDERLINE = "\u001B[4m"; // 밑줄

	// 배경색
	public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m"; // 노란색 배경
	
	private Connection conn = DBConn.getConnection();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
    // 신청 도서 정보 받기
    protected void request() throws Exception {
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
    		
    		String LINE = "=====================================================================================";
    		System.out.print(LINE);
    		System.out.println("\n\t\t\t\t" +ANSI_BOLD + ANSI_ITALIC + "📝 [  Book Request  ] " + ANSI_RESET);
    		System.out.println(LINE);
    		
    		System.out.print(" 신청할 도서 정보를 입력해주세요 \n 도 서 정 보 ➡️ ");
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
