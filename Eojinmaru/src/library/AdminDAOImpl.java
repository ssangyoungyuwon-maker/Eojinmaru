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

    // 회원 관리 메서드
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
                user.setUser_birth(rs.getDate("user_birth").toString()); 
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
            pstmt.setInt(1, userCode); 
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
        
        String sql = "SELECT user_code, user_id, user_name, user_birth, user_tel, user_email, user_address " +
                     "FROM user_info WHERE user_name LIKE ? ORDER BY user_name";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + name + "%"); 
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
        String sql = "DELETE FROM user_info WHERE user_code = ?";
        
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userCode);
            
            int resultRows = pstmt.executeUpdate(); 
            
            return (resultRows > 0); 

        } catch (SQLException e) {
            System.err.println(">> 회원 삭제 중 오류: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return false; 
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

    // 도서 관리 메서드
	@Override
	public List<BookInfoDTO1> findAllBooks() {
		List<BookInfoDTO1> list = new ArrayList<>();
       
		String sql = "SELECT bi.ISBN, bi.category_id, bi.publisher_id, bi.bookname, bi.publish_date, b.book_code " +
                "FROM bookinfo bi " +
                "LEFT JOIN book b ON bi.ISBN = b.ISBN " +
                "ORDER BY bi.bookname ASC";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookInfoDTO1 book = new BookInfoDTO1();
                book.setIsbn(rs.getString("ISBN"));
                book.setCategory_id(rs.getInt("category_id"));
                book.setPublisher_id(rs.getString("publisher_id"));
                book.setBookName(rs.getString("bookname"));
                book.setPublish_date(rs.getDate("publish_date").toString());
                book.setBook_code(rs.getInt("book_code")); // book_code 설정
                list.add(book);
            }
        } catch (SQLException e) {
            System.err.println(">> 전체 도서 조회 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
	}

	@Override
	public List<BookInfoDTO1> findBooksByName(String bookName) {
		List<BookInfoDTO1> list = new ArrayList<>();
		String sql = "SELECT bi.ISBN, bi.category_id, bi.publisher_id, bi.bookname, bi.publish_date, b.book_code " +
                "FROM bookinfo bi " +
                "LEFT JOIN book b ON bi.ISBN = b.ISBN " +
                "WHERE bi.bookname LIKE ? ORDER BY bi.bookname ASC";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, "%" + bookName + "%"); 
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BookInfoDTO1 book = new BookInfoDTO1();
                book.setIsbn(rs.getString("ISBN"));
                book.setCategory_id(rs.getInt("category_id"));
                book.setPublisher_id(rs.getString("publisher_id"));
                book.setBookName(rs.getString("bookname"));
                book.setPublish_date(rs.getDate("publish_date").toString());
                book.setBook_code(rs.getInt("book_code"));
                list.add(book);
            }
        } catch (SQLException e) {
            System.err.println(">> 도서명으로 검색 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return list;
    }
	
    // 도서 코드 검색 구현
    @Override
    public BookInfoDTO1 findBookByCode(int bookCode) {
        
    	String sql = "SELECT bi.ISBN, bi.category_id, bi.publisher_id, bi.bookname, bi.publish_date, b.book_code " +
                "FROM book b " +
                "JOIN bookinfo bi ON b.ISBN = bi.ISBN " +
                "WHERE b.book_code = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BookInfoDTO1 book = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new BookInfoDTO1();
                book.setIsbn(rs.getString("ISBN"));
                book.setCategory_id(rs.getInt("category_id"));
                book.setPublisher_id(rs.getString("publisher_id"));
                book.setBookName(rs.getString("bookname"));
                book.setPublish_date(rs.getString("publish_date"));
                book.setBook_code(rs.getInt("book_code")); 
            }
        } catch (NumberFormatException e) {
            System.err.println(">> DAO 오류: book_code가 숫자가 아닙니다.");
        } catch (SQLException e) {
            System.err.println(">> 도서 코드로 도서 검색 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return book;
    }
	
    @Override
    public boolean insertBook(BookInfoDTO1 book) {
        
        String sqlInfo = "INSERT INTO bookInfo (ISBN, category_id, publisher_id, bookname, publish_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        String sqlBook = "INSERT INTO book (BOOK_CODE, ISBN) VALUES (?, ?)";
        
        PreparedStatement pstmtInfo = null;
        PreparedStatement pstmtBook = null;

        try {
        	conn.setAutoCommit(false);
        	
            pstmtInfo = conn.prepareStatement(sqlInfo);
            pstmtInfo.setString(1, book.getIsbn());
            pstmtInfo.setInt(2, book.getCategory_id()); 
            pstmtInfo.setString(3, book.getPublisher_id());
            pstmtInfo.setString(4, book.getBookName());
            pstmtInfo.setString(5, book.getPublish_date());

            int resultInfo = pstmtInfo.executeUpdate();
            
            if (resultInfo == 0) {
                conn.rollback();
                return false;
            }
            
            pstmtBook = conn.prepareStatement(sqlBook);
            pstmtBook.setInt(1, book.getBook_code()); 
            pstmtBook.setString(2, book.getIsbn());   
            
            int resultBook = pstmtBook.executeUpdate();

            if (resultBook == 0) {
                conn.rollback();
                return false;
            }
              
            conn.commit(); 
            return true;
           
        } catch (SQLException e) {
            try {
                conn.rollback(); 
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            System.err.println(">> 도서 등록 중 DB 오류 발생: " + e.getMessage());
        } finally {
            try {
               
                if (pstmtInfo != null) pstmtInfo.close();
                if (pstmtBook != null) pstmtBook.close();
                
                conn.setAutoCommit(true); 
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false;
    }
    
    // 도서 삭제
    @Override
    public boolean deleteBookByCode(int bookCode) {
        String sql = "DELETE FROM book WHERE BOOK_CODE = ?";
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookCode); 
            return pstmt.executeUpdate() > 0;
            
        } catch (NumberFormatException e) {
            System.err.println(">> DAO 오류: book_code가 숫자가 아닙니다.");
        } catch (SQLException e) {
            System.err.println(">> 도서 삭제 중 오류: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }
	
	// 폐기 등록
    @Override
    public boolean registerDisposedBook(int bookCode, String reason) {
        
    	
        BookInfoDTO1 bookToDispose = findBookByCode(bookCode); 
        if (bookToDispose == null) {
            System.err.println(">> (DAO) 폐기 등록 실패: 해당 도서를 찾을 수 없음.");
            return false;
        }
        String isbn = bookToDispose.getIsbn(); // ISBN 확보
        
        String sqlInsert = "INSERT INTO disposedbook (book_code, isbn, dispose_date, dispose_reason) " +
                           "VALUES (?, ?, SYSDATE, ?)"; 
        
        String sqlDelete = "DELETE FROM book WHERE BOOK_CODE = ?";
        
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtDelete = null;
        
        try {
        	
            pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setInt(1, bookCode);
            pstmtInsert.setString(2, isbn); 
            pstmtInsert.setString(3, reason);
            int insertRows = pstmtInsert.executeUpdate();

            if (insertRows == 0) {
            	System.err.println(">> (DAO) 폐기 등록 실패: disposedbook 테이블 INSERT 실패.");
            	
                return false;
            }

            pstmtDelete = conn.prepareStatement(sqlDelete);
            pstmtDelete.setInt(1, bookCode);
            pstmtDelete.executeUpdate();
            
            
            return true; 

        } catch (NumberFormatException e) {
            System.err.println(">> DAO 오류: book_code가 숫자가 아닙니다.");
        } catch (SQLException e) {
            System.err.println(">> 폐기 도서 등록/삭제 중 오류: " + e.getMessage());
        } finally {
            try {
                if (pstmtInsert != null) pstmtInsert.close();
                if (pstmtDelete != null) pstmtDelete.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
		return false;
    }
    
    // 폐기 확인
    @Override
    public List<DisposedBookDTO> findAllDisposedBooks() {
        List<DisposedBookDTO> list = new ArrayList<>();
        
        String sql = "SELECT d.book_code, bi.bookname, d.dispose_date, d.dispose_reason " +
                "FROM disposedbook d " +
                "LEFT JOIN bookinfo bi ON d.ISBN = bi.ISBN " + 
                "ORDER BY d.dispose_date DESC ";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DisposedBookDTO disposed = new DisposedBookDTO();
                disposed.setBook_code(rs.getInt("book_code"));
                disposed.setBookName(rs.getString("bookname"));
                disposed.setDispose_date(rs.getDate("dispose_date").toString());
                disposed.setDispose_reason(rs.getString("dispose_reason"));
                list.add(disposed);
            }
        } catch (SQLException e) {
            System.err.println(">> 폐기 도서 목록 조회 중 오류: " + e.getMessage());
        } finally {
           
        }
        return list;
    }

    @Override
    public boolean deleteDisposedBook(int bookCode) {
        String sql = "DELETE FROM disposedbook WHERE book_code = ?";
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookCode);
            
            int resultRows = pstmt.executeUpdate();  
            
            return (resultRows > 0);                       

        } catch (SQLException e) {
            System.err.println(">> 폐기 도서 기록 삭제 중 오류: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return false; 
    }
	
}