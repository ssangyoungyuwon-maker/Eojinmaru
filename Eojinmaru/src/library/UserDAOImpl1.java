package library;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DBUtil.DBConn;
import DBUtil.DBUtil;

public class UserDAOImpl1 implements UserDAO1 {
	private Connection conn = DBConn.getConnection();

	@Override
	// 도서검색(제목/저자)
	public List<BookInfoDTO1> listBook(String bookname, String author_name) {
		List<BookInfoDTO1> list = new ArrayList<BookInfoDTO1>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;
		
		try {
			  // 도서번호, isbn, 도서이름, 저자, 출판사, 발행일를 출력
			sql = "SELECT book_code, bi.isbn, bookName, author_name, publisher_name, TO_CHAR(publisher_date, 'YYYYY-MM-DD') publisher_date FROM book b, bookinfo bi, author a, publisher p WHERE b.isbn = bi.isbn";
			
			pstmt = conn.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				BookInfoDTO1 dto = new BookInfoDTO1();
				
				dto.setBook_code(rs.getString("book_code"));
				dto.setIsbn(rs.getString("isbn"));
				dto.setBookName(rs.getString("bookname"));
				dto.setAuthor_name(rs.getString("author_name"));
				dto.setPublisher_name(rs.getString("publisher_name"));
				dto.setPublish_date(rs.getString("publisher_date"));
				
				list.add(dto);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();		
    	} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		
		return list;
	}

	@Override
	// 대출신청/연장
	public void loan(BookInfoDTO1 dto) throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
