package library;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class BookRequestUI {
	private Connection conn = DBConn.getConnection();
	private BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	
    // 신청 도서 정보 받기
    protected void request() {
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
    		System.out.println("신청할 도서 정보를 입력해주세요 \n 제목 : ");
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
    
    // 선택된 공지글 수정
    protected void noticeUpdate(int noticeId) throws Exception {
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
    		System.out.println("수정할 공지 제목을 입력해주세요 \n 제목 : ");
    		String newTitle = br.readLine().trim();
    		
    		System.out.println("수정할 공지 내용을 입력해주세요 \n 내용 : ");
    		String newContent = br.readLine().trim();
    		
    		if (newTitle.isEmpty() && newContent.isEmpty()) {
    			throw new Exception("✅ 입력된 내용이 없어 공지사항 수정이 취소되었습니다.");
    		}
    		
    		sql = "UPDATE NOTICE SET notice_title = ?, notice_content = ?, notice_date = SYSDATE WHERE notice_id = ?";
    		
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setString(1, newTitle);
    		pstmt.setString(2, newContent);
    		pstmt.setInt(3, noticeId);
    		
    		if(pstmt.executeUpdate() <= 0) {
    			throw new Exception("❌ 공지사항 수정에 실패했습니다.");
    		}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
    }
    
    //선택된 공지글 삭제
    protected void noticeDelete(int noticeId) throws Exception {
    	PreparedStatement pstmt = null;
    	String sql;
    	
    	try {
    		sql = "DELETE FROM NOTICE WHERE notice_id = ?";
    		
    		pstmt = conn.prepareStatement(sql);
    		pstmt.setInt(1, noticeId);
    		
    		if(pstmt.executeUpdate() <= 0) {
    			throw new Exception("❌ 공지사항 삭제에 실패했습니다.");
    		}
		} catch (Exception e) {
			throw e;
		} finally {
			DBUtil.close(pstmt);
		}
    }
    
    
    // 말줄임 함수(공지사항 제목 자르는데 사용)
    private String truncateString(String text, int maxLength) {
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
    
    // 줄바꿈 함수(공지글 내용을 줄바꾸는 데 사용)
    private void printWrapped(String text, int width) {
        int length = text.length();
        for (int i = 0; i < length; i += width) {
            int end = Math.min(i + width, length);
            System.out.println(text.substring(i, end));
        }
    }

}
