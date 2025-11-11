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
        String sql = "DELETE FROM user_info WHERE user_code = ?";
        
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

    // 도서 관리 메서드
	@Override
	public List<BookInfoDTO1> findAllBooks() {
		List<BookInfoDTO1> list = new ArrayList<>();
        // ISBN, category_id, publisher_id, bookname, publisher_date
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
            pstmt.setString(1, "%" + bookName + "%"); // 부분 일치 검색
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
    public BookInfoDTO1 findBookByCode(String bookCode) {
        // book_code는 BookInfoDTO1에 정의된 필드입니다.
    	String sql = "SELECT bi.ISBN, bi.category_id, bi.publisher_id, bi.bookname, bi.publish_date, b.book_code " +
                "FROM book b " +
                "JOIN bookinfo bi ON b.ISBN = bi.ISBN " +
                "WHERE b.book_code = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BookInfoDTO1 book = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(bookCode));
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new BookInfoDTO1();
                book.setIsbn(rs.getString("ISBN"));
                book.setCategory_id(rs.getInt("CATEGORY_ID"));
                book.setPublisher_id(rs.getString("PUBLISHER_ID"));
                book.setBookName(rs.getString("BOOKNAME"));
                book.setPublish_date(rs.getString("PUBLISHER_DATE"));
                book.setBook_code(rs.getInt("BOOK_CODE")); // <-- 코드 설정
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
        // DB 실제 컬럼명을 가정하고 작성 (publisher_date로 가정)
        String sql = "INSERT INTO bookInfo (ISBN, category_id, publisher_id, bookname, publish_date) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        PreparedStatement pstmt = null;

        try {
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, book.getIsbn());
            pstmt.setInt(2, book.getCategory_id()); 
            pstmt.setString(3, book.getPublisher_id());
            pstmt.setString(4, book.getBookName());
            pstmt.setString(5, book.getPublish_date());

            int resultRows = pstmt.executeUpdate(); 
            
            return (resultRows > 0); 

        } catch (NumberFormatException e) {
            System.err.println(">> DAO 오류: Category ID가 숫자가 아닙니다.");
        } catch (IllegalArgumentException e) {
            System.err.println(">> " + "날짜 형식이 잘못되었습니다. (YYYY-MM-DD 형식으로 입력하세요)");
        } catch (SQLException e) {
            System.err.println(">> 도서 등록 중 DB 오류 발생: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return false; 
    }
    
    // 도서 삭제
    @Override
    public boolean deleteBookByCode(String bookCode) {
        // ISBN이 아닌 BOOK_CODE로 삭제
        String sql = "DELETE FROM book WHERE BOOK_CODE = ?";
        PreparedStatement pstmt = null;
        
        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bookCode); // BOOK_CODE는 String이므로 setString
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
    public boolean registerDisposedBook(String bookCode, String reason) {
        
    	// SQL 1: disposed_book 테이블에 폐기 이력 삽입
        String sqlInsert = "INSERT INTO disposed_book (book_code, dispose_date, dispose_reason) " +
                           "VALUES (?, SYSDATE, ?)";
        
        // SQL 2: bookInfo에서 해당 book_code의 도서 삭제
        String sqlDelete = "DELETE FROM book WHERE BOOK_CODE = ?";
        
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtDelete = null;
        
        // (참고: 안전을 위해 Transaction 처리가 필요하지만, 현재 구조에서는 순차 실행)
        try {
        	int bookCodeNum = Integer.parseInt(bookCode); // book_code는 NUMBER

            // 1. disposed_book에 삽입
            pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setInt(1, bookCodeNum);
            pstmtInsert.setString(2, reason);
            int insertRows = pstmtInsert.executeUpdate();

            if (insertRows == 0) {
                // 이 경우는 거의 없지만, INSERT 실패 시
                System.err.println(">> (DAO) 폐기 등록 실패: disposed_book 테이블 INSERT 실패.");
                return false;
            }

            // 2. book 테이블에서 삭제
            pstmtDelete = conn.prepareStatement(sqlDelete);
            pstmtDelete.setInt(1, bookCodeNum);
            pstmtDelete.executeUpdate();
            
            return true; // 두 작업 모두 성공

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
        /*
        // book_code가 숫자인 것을 이용해 bookinfo, book 테이블과 JOIN하여 도서명(bookname)을 가져옴
        String sql = "SELECT d.book_code, d.dispose_date, d.dispose_reason, bi.bookname " +
                     "FROM disposed_book d " +
                     "LEFT JOIN book b ON d.book_code = b.book_code " + // (삭제되었으니 없을 수도 있음, book_code만 필요하면 이 JOIN은 불필요)
                     "LEFT JOIN bookinfo bi ON b.ISBN = bi.ISBN " + // (도서명을 가져오기 위한 JOIN)
                     "ORDER BY d.dispose_date DESC";
        */
        
        // [수정] 도서명을 가져오기 어려운 스키마이므로, disposed_book 테이블만 조회
        // (만약 book_code로 bookinfo를 바로 조인할 수 있다면 JOIN 사용)
        
        String sql = "SELECT book_code, dispose_date, dispose_reason " +
                            "FROM disposed_book ORDER BY dispose_date DESC";

        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                DisposedBookDTO disposed = new DisposedBookDTO();
                disposed.setBook_code(rs.getInt("book_code"));
                disposed.setDispose_date(rs.getDate("dispose_date").toString());
                disposed.setDispose_reason(rs.getString("dispose_reason"));
                // (bookname은 이 쿼리로 가져올 수 없음)
                list.add(disposed);
            }
        } catch (SQLException e) {
            System.err.println(">> 폐기 도서 목록 조회 중 오류: " + e.getMessage());
        } finally {
            // (자원 해제 로직 생략)
        }
        return list;
    }
	
}