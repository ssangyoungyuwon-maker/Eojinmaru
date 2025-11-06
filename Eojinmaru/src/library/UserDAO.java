package library;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

import DBUtil.DBConn;



/**
 * user_info 테이블에 접근하는 클래스 (Data Access Object)
 * 회원가입 등 유저 관련 DB 작업을 전담합니다.
 */
public class UserDAO {
	
	private Connection conn = DBConn.getConnection();
    /**
     * 회원가입 정보를 DB에 저장합니다.
     * @param user : 회원가입 정보가 담긴 UserDTO 객체
     * @return : 성공 시 true, 실패 시 false
     */
    public boolean signUpUser(UserDTO user) {
        
        // 1. user_code는 user_seq.NEXTVAL (시퀀스) 사용
        // 2. loan_renewaldate는 NOT NULL이므로 기본값 SYSDATE (현재 날짜) 사용
        String sql = "INSERT INTO user_info (user_code, user_id, user_pwd, user_name, user_birth, " +
                     "user_tel, user_email, user_address, loan_renewaldate) " +
                     "VALUES (user_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, SYSDATE)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
          
            
            pstmt = conn.prepareStatement(sql);
            
            
            pstmt.setString(1, user.getUser_Id());
            pstmt.setString(2, user.getUser_pwd());
            pstmt.setString(3, user.getUser_name());
            
            // DTO의 String 날짜(YYYY-MM-DD)를 java.sql.Date로 변환
            pstmt.setDate(4, Date.valueOf(user.getUser_birth())); 
            
            pstmt.setString(5, user.getUser_tel());
            pstmt.setString(6, user.getUser_email());
            pstmt.setString(7, user.getUser_address());

            int resultRows = pstmt.executeUpdate(); 
            
            // 결과 반환 (1줄 이상 삽입되었으면 성공)
            return (resultRows > 0); 

        } catch (SQLException e) {
            System.err.println(">> DB 연결 또는 SQL 실행 중 오류 발생: " + e.getMessage());
            // 예: ID 중복, 날짜 형식 오류(YYYY-MM-DD가 아님) 등
        } catch (IllegalArgumentException e) {
            System.err.println(">> 날짜 형식이 잘못되었습니다. (YYYY-MM-DD 형식으로 입력하세요)");
        } finally {
            // 자원 해제 (연결 순서의 역순)
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false; // 실패 시
    }
}