package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class UserDAO {
	private Connection conn = DBConn.getConnection();

	public void updateUser(MemberDTO dto) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;

		try {
			conn.setAutoCommit(false);

			sql = "UPDATE UserInfo SET user_pwd=?, user_name=?, user_tel=?, user_email=?,user_address=? WHERE user_id=?";

			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, dto.getUser_pwd());
			pstmt.setString(2, dto.getUser_name());
			pstmt.setString(3, dto.getUser_tel());
			pstmt.setString(4, dto.getUser_email());
			pstmt.setString(5, dto.getUser_address());
			pstmt.setString(6, dto.getUser_Id());

			pstmt.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			DBUtil.rollback(conn);
			
		} finally {
			DBUtil.close(pstmt);

			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}

	public void deleteUser(String id) throws SQLException {
		PreparedStatement pstmt = null;
		String sql;
		
		try {
			conn.setAutoCommit(false);
			
			sql = "DELETE FROM UserInfo WHERE user_id=?";
			
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			pstmt.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			DBUtil.rollback(conn);
		}finally {
			DBUtil.close(pstmt);
			
			try {
				conn.setAutoCommit(true);
			} catch (Exception e2) {
			}
		}
	}
	
	public MemberDTO chkmyinfo(String id) throws SQLException{
		MemberDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql="SELECT user_Id, user_pwd, user_name, user_birth, user_tel,user_email,user_address"
					+ " FROM UserInfo WHERE user_id=?";
		
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				dto = new MemberDTO();
				
				dto.setUser_Id(rs.getString("user_Id"));
				dto.setUser_pwd(rs.getString("user_pwd"));
				dto.setUser_name(rs.getString("user_name"));
				dto.setUser_pwd(rs.getString("user_birth"));
				dto.setUser_pwd(rs.getString("user_tel"));
				dto.setUser_pwd(rs.getString("user_email"));
				dto.setUser_pwd(rs.getString("user_address"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return dto;
	}
	
}