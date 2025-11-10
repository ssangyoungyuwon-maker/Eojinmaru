package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBUtil.DBConn;

public class AdminDAOImpl implements AdminDAO {

    private Connection conn = DBConn.getConnection();

    @Override
    public MemberDTO findUserById(String userId) {
        String sql = "SELECT user_code, user_id, user_name, user_birth, user_tel, user_email, user_address " +
                     "FROM user_info WHERE user_id = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MemberDTO user = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, userId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new MemberDTO();
                user.setUser_code(rs.getInt("user_code"));
                user.setUser_Id(rs.getString("user_id"));
                user.setUser_name(rs.getString("user_name"));
                user.setUser_birth(rs.getDate("user_birth").toString()); // Date -> String
                user.setUser_tel(rs.getString("user_tel"));
                user.setUser_email(rs.getString("user_email"));
                user.setUser_address(rs.getString("user_address"));
            }
        } catch (SQLException e) {
            System.err.println(">> ID로 회원 검색 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return user;
    }

    @Override
    public MemberDTO findUserByCode(int userCode) {
        String sql = "SELECT user_code, user_id, user_name, user_birth, user_tel, user_email, user_address " +
                     "FROM user_info WHERE user_code = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        MemberDTO user = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userCode); // <-- int로 설정
            rs = pstmt.executeQuery();

            if (rs.next()) {
                user = new MemberDTO();
                user.setUser_code(rs.getInt("user_code"));
                user.setUser_Id(rs.getString("user_id"));
                user.setUser_name(rs.getString("user_name"));
                user.setUser_birth(rs.getDate("user_birth").toString());
                user.setUser_tel(rs.getString("user_tel"));
                user.setUser_email(rs.getString("user_email"));
                user.setUser_address(rs.getString("user_address"));
            }
        } catch (SQLException e) {
            System.err.println(">> 코드로 회원 검색 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return user;
    }
    
    @Override
    public List<MemberDTO> findUserByName(String name) {
        List<MemberDTO> list = new ArrayList<>();
        // 이름의 일부만 입력해도 검색되도록 LIKE 사용
        String sql = "SELECT user_code, user_id, user_name, user_birth, user_tel, user_email, user_address " +
                     "FROM user_info WHERE user_name LIKE ? ORDER BY user_name";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%"); // 예: "홍" -> "%홍%"
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MemberDTO user = new MemberDTO();
                user.setUser_code(rs.getInt("user_code"));
                user.setUser_Id(rs.getString("user_id"));
                user.setUser_name(rs.getString("user_name"));
                user.setUser_birth(rs.getDate("user_birth").toString());
                user.setUser_tel(rs.getString("user_tel"));
                user.setUser_email(rs.getString("user_email"));
                user.setUser_address(rs.getString("user_address"));
                list.add(user);
            }
        } catch (SQLException e) {
            System.err.println(">> 이름으로 회원 검색 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }

    @Override
    public boolean deleteUserByCode(int userCode) {
        String sql = "DELETE FROM user_info WHERE user_id = ?";
        
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userCode);
            
            int resultRows = pstmt.executeUpdate(); // DELETE 실행
            
            return (resultRows > 0); // 1줄 이상 삭제되었으면 true

        } catch (SQLException e) {
            System.err.println(">> 회원 삭제 중 오류: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return false; // 삭제 실패
    }
    
    @Override
    public List<MemberDTO> findAllUsers() {
        List<MemberDTO> list = new ArrayList<>();
        String sql = "SELECT user_code, user_id, user_name, user_birth, user_tel, user_email, user_address " +
                     "FROM user_info ORDER BY user_code ASC";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                MemberDTO user = new MemberDTO();
                user.setUser_code(rs.getInt("user_code"));
                user.setUser_Id(rs.getString("user_id"));
                user.setUser_name(rs.getString("user_name"));
                user.setUser_birth(rs.getDate("user_birth").toString());
                user.setUser_tel(rs.getString("user_tel"));
                user.setUser_email(rs.getString("user_email"));
                user.setUser_address(rs.getString("user_address"));
                list.add(user);
            }
        } catch (SQLException e) {
            System.err.println(">> 전체 회원 조회 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }
}