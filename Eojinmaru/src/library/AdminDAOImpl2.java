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

public class AdminDAOImpl2 implements AdminDAO2 {	// 도서 신청 목록(번호, 내용, 상태=대기) 가져오기
	private Connection conn = DBConn.getConnection();
	
	@Override
	public List<AdminDTO2> sinchoengdagidoseo() {
		List<AdminDTO2> list = new ArrayList<AdminDTO2>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT sincheong_code, sincheong_name, sincheong_status FROM sincheong WHERE sincheong_status = '대기'" ;
			
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				AdminDTO2 dto = new AdminDTO2();
				
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
            return "";
        }
        if (text.length() > maxLength) {
            return text.substring(0, maxLength - 3) + "...";
        }
        return text;
    }

	@Override
	public int sujeongsincheongstatus(AdminDTO2 dto) throws SQLException{
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
	public List<AdminDTO2> notice() {
		List<AdminDTO2> list = new ArrayList<AdminDTO2>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			sql = "SELECT NOTICE_ID, NOTICE_TITLE, NOTICE_CONTENT, "
					+ "to_char(NOTICE_DATE, 'yyyy-mm-dd') as NOTICE_DATE FROM Notice ORDER BY NOTICE_ID";
		
		pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		
		while (rs.next()) {
			AdminDTO2 dto = new AdminDTO2();
			
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
	public AdminDTO2 selectNoticeById(int noticeId) {
		
		AdminDTO2 dto = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    String sql;

	    try {
	        sql = "SELECT NOTICE_ID, NOTICE_TITLE, NOTICE_CONTENT, TO_CHAR(NOTICE_DATE, 'YYYY-MM-DD') AS NOTICE_DATE FROM Notice WHERE NOTICE_ID = ?";
	        
	        pstmt = conn.prepareStatement(sql);
	        pstmt.setInt(1, noticeId); 
	        rs = pstmt.executeQuery();

	        if (rs.next()) {
	            dto = new AdminDTO2();
	            
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
	public int noticeUpdate(AdminDTO2 dto) {
		
		int result = 0;
		CallableStatement cstmt = null;
		String sql;
		
		try {
			sql = "CALL UPDATENOTICE (?, ?, ?)";
			
			cstmt = conn.prepareCall(sql);
			
			cstmt.setString(1, dto.getNoticeTitle());
			cstmt.setString(2, dto.getNoticeContent());
			cstmt.setInt(3, dto.getNoticeId());
			
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
	
    
}