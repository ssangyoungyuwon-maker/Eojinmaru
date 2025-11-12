package library;

import java.sql.PreparedStatement;
import java.sql.ResultSet; 
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import DBUtil.DBConn;

public class MemberDAOImpl implements MemberDAO {
	
	private Connection conn = DBConn.getConnection();
    
    /**
     * 로그인 정보를 확인합니다.
     * @param id : 사용자가 입력한 아이디
     * @param pw : 사용자가 입력한 비밀번호
     * @return : 로그인 성공 시 해당 유저의 MainDTO, 실패 시 null
     */
    @Override 
    public MemberDTO login(String id, String pw) {
        String sql = "SELECT user_id, user_pwd, user_name, user_tel, user_email, user_address, loan_renewaldate " +
                     "FROM UserInfo WHERE user_id = ? AND user_pwd = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null; // 쿼리 결과를 받을 ResultSet
        MemberDTO user = null; // 로그인 성공 시 채워질 객체

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            
            rs = pstmt.executeQuery(); // SELECT 쿼리는 executeQuery() 사용

            // 쿼리 결과가 있다면 (로그인 성공)
            if (rs.next()) {
                user = new MemberDTO(); // 빈 DTO 객체 생성
                
                // ResultSet에서 DTO로 정보 옮겨 담기
                user.setUser_Id(rs.getString("user_id"));
                user.setUser_pwd(rs.getString("user_pwd"));
                user.setUser_name(rs.getString("user_name"));
                user.setUser_tel(rs.getString("user_tel"));
                user.setUser_email(rs.getString("user_email"));
                user.setUser_address(rs.getString("user_address"));
                user.setLoan_renewaldate(rs.getString("loan_renewaldate"));
            }

        } catch (SQLException e) {
            System.err.println(">> 로그인 쿼리 실행 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                // conn은 DBConn에서 관리하므로 닫지 않음
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return user; // 성공 시 DTO, 실패 시 null 반환
    }
	
    // (기존 signUpUser 메서드는 그대로 둠)
	@Override
	public boolean signUpUser(MemberDTO user) {
        
        String sql = "INSERT INTO UserInfo (user_code, user_id, user_pwd, user_name, user_birth, " +
                     "user_tel, user_email, user_address, loan_renewaldate) " +
                     "VALUES (user_seq.NEXTVAL, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, SYSDATE)";
        
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, user.getUser_Id());
            pstmt.setString(2, user.getUser_pwd());
            pstmt.setString(3, user.getUser_name());
            
            pstmt.setDate(4, Date.valueOf(user.getUser_birth())); 
            
            pstmt.setString(5, user.getUser_tel());
            pstmt.setString(6, user.getUser_email());
            pstmt.setString(7, user.getUser_address());

            int resultRows = pstmt.executeUpdate(); 
            
            return (resultRows > 0); 

        } catch (SQLException e) {
            System.err.println(">> DB 연결 또는 SQL 실행 중 오류 발생: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println(">> 날짜 형식이 잘못되었습니다. (YYYY-MM-DD 형식으로 입력하세요)");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false; 
    }
}