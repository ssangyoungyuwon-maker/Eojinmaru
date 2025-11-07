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
	public List<AdminDTO2> sinchoengstatus() {
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
    
}