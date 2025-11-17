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

public class AdminDAOImpl implements AdminDAO {

    private Connection conn = DBConn.getConnection();

    // 회원 관리 메서드
    @Override
    public MemberDTO findUserById(String userId) {
        String sql = "SELECT user_code, user_id, user_name, user_birth, user_tel, user_email, user_address " +
                     "FROM userinfo WHERE user_id = ?";
        
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
                     "FROM userinfo WHERE user_code = ?";
        
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
                     "FROM userinfo WHERE user_name LIKE ? ORDER BY user_name";
        
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
        String sql = "DELETE FROM userinfo WHERE user_code = ?";
        
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
                     "FROM userinfo ORDER BY user_code ASC";
        
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
	public List<BookInfoDTO> findAllBooks() {
		List<BookInfoDTO> list = new ArrayList<>();
       
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
                BookInfoDTO book = new BookInfoDTO();
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
	public List<BookInfoDTO> findBooksByName(String bookName) {
		List<BookInfoDTO> list = new ArrayList<>();
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
                BookInfoDTO book = new BookInfoDTO();
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
    public BookInfoDTO findBookByCode(int bookCode) {
        
    	String sql = "SELECT bi.ISBN, bi.category_id, bi.publisher_id, bi.bookname, bi.publish_date, b.book_code " +
                "FROM book b " +
                "JOIN bookinfo bi ON b.ISBN = bi.ISBN " +
                "WHERE b.book_code = ?";
        
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        BookInfoDTO book = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new BookInfoDTO();
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
    public boolean insertBook(BookInfoDTO book) {
        
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
    // book 의 book_condtition 을 대출 불가로 바꿔버려야함 삭제가 되는건 삭제해버리고 안되는건 대출불가로 바꿔버림
    	
    	 String sqlLoan = "DELETE FROM LOAN WHERE BOOK_CODE = ?"; // 1. 자식 레코드 삭제
         String sqlBook = "DELETE FROM book WHERE BOOK_CODE = ?";  // 2. 부모 레코드 삭제
         
         PreparedStatement pstmtLoan = null;
         PreparedStatement pstmtBook = null;
         
        
        
        try {
        	conn.setAutoCommit(false); 
        	
        	pstmtLoan = conn.prepareStatement(sqlLoan);
            pstmtLoan.setInt(1, bookCode);
            pstmtLoan.executeUpdate(); 
            
            pstmtBook = conn.prepareStatement(sqlBook);
            pstmtBook.setInt(1, bookCode); 
            int resultBook = pstmtBook.executeUpdate();
            
            if (resultBook > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
            
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            System.err.println(">> 도서 삭제 중 오류 (자식 레코드 문제 포함): " + e.getMessage());
        } finally {
            try {
                if (pstmtLoan != null) pstmtLoan.close();
                if (pstmtBook != null) pstmtBook.close();
                conn.setAutoCommit(true); 
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return false;
    }
	
	// 폐기 등록
    @Override
    public boolean registerDisposedBook(int bookCode, String reason) {
        
    	
        BookInfoDTO bookToDispose = findBookByCode(bookCode); 
        if (bookToDispose == null) {
            System.err.println(">> (DAO) 폐기 등록 실패: 해당 도서를 찾을 수 없음.");
            return false;
        }
        String isbn = bookToDispose.getIsbn(); 
        
        String sqlInsert = "INSERT INTO disposedbook (book_code, isbn, dispose_date, dispose_reason) " +
                           "VALUES (?, ?, SYSDATE, ?)"; 
        
        String sqlDeleteLoan = "DELETE FROM LOAN WHERE BOOK_CODE = ?";
        
        String sqlDeleteBook = "DELETE FROM book WHERE BOOK_CODE = ?";;
        
        PreparedStatement pstmtInsert = null;
        PreparedStatement pstmtDeleteBook = null;
        PreparedStatement pstmtDeleteLoan = null;
        
        try {
        	conn.setAutoCommit(false); 
        	
            pstmtInsert = conn.prepareStatement(sqlInsert);
            pstmtInsert.setInt(1, bookCode);
            pstmtInsert.setString(2, isbn); 
            pstmtInsert.setString(3, reason);
            
            if (pstmtInsert.executeUpdate() == 0) {
                conn.rollback();
            	System.err.println(">> (DAO) 폐기 등록 실패: disposedbook 테이블 INSERT 실패.");
                return false;
            }

            pstmtDeleteLoan = conn.prepareStatement(sqlDeleteLoan);
            pstmtDeleteLoan.setInt(1, bookCode);
            pstmtDeleteLoan.executeUpdate(); 
            
            pstmtDeleteBook = conn.prepareStatement(sqlDeleteBook);
            pstmtDeleteBook.setInt(1, bookCode);
            
            if (pstmtDeleteBook.executeUpdate() == 0) {
                conn.rollback();
                System.err.println(">> (DAO) 폐기 등록 실패: book 테이블 DELETE 실패. (이미 삭제되었거나 존재하지 않음)");
                return false;
            }
            
            conn.commit(); 
            return true;

        } catch (NumberFormatException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } 
            System.err.println(">> DAO 오류: book_code가 숫자가 아닙니다.");
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } 
            System.err.println(">> 폐기 도서 등록/삭제 중 오류: " + e.getMessage());
        } finally {
            try {
            	if (pstmtDeleteLoan != null) pstmtDeleteLoan.close(); 
                if (pstmtDeleteBook != null) pstmtDeleteBook.close(); 
                conn.setAutoCommit(true); 
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
           try { 
                if (rs != null) rs.close(); 
                if (pstmt != null) pstmt.close(); 
            } catch (SQLException e) { e.printStackTrace(); } 
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
    
    // 폐기 기록 조회 
    public DisposedBookDTO findDisposedBookByCode(int bookCode) {
        String sql = "SELECT d.book_code, bi.bookname " +
                     "FROM disposedbook d " +
                     "LEFT JOIN bookinfo bi ON d.ISBN = bi.ISBN " +
                     "WHERE d.book_code = ?";

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        DisposedBookDTO disposedBook = null;

        try {
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookCode);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                disposedBook = new DisposedBookDTO();
                disposedBook.setBook_code(rs.getInt("book_code"));
                disposedBook.setBookName(rs.getString("bookname"));
                // dispose_date, dispose_reason은 이 쿼리에서 가져오지 않음 (단순 확인용)
            }
        } catch (SQLException e) {
            System.err.println(">> 폐기 기록 검색 중 오류: " + e.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) { e.printStackTrace(); }
        }
        return disposedBook;
    }
    
    @Override
	public List<AdminDTO> sinchoengdaegidoseo() {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT sincheong_code, sincheong_name, sincheong_status FROM sincheong WHERE sincheong_status = '대기'";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();

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

	@Override
	public int sujeongsincheongstatus(AdminDTO dto) throws SQLException {
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
	public List<AdminDTO> notice() {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NOTICE_ID, NOTICE_TITLE, NOTICE_CONTENT, "
					+ "to_char(NOTICE_DATE, 'yyyy-mm-dd') as NOTICE_DATE FROM Notice ORDER BY NOTICE_ID desc";

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();

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
	public AdminDTO selectNoticeById(int noticeId) {

		AdminDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT NOTICE_ID, NOTICE_TITLE, NOTICE_CONTENT, TO_CHAR(NOTICE_DATE, 'YYYY-MM-DD') AS NOTICE_DATE FROM Notice WHERE NOTICE_ID = ?";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, noticeId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new AdminDTO();

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
	public int noticeInsert(AdminDTO dto) {
		int result = 0;
		PreparedStatement pstmt = null;
		String sql;

		try {
			sql = "INSERT INTO NOTICE (notice_title, notice_content, notice_date) VALUES (?, ?, SYSDATE) ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, dto.getNoticeTitle());
			pstmt.setString(2, dto.getNoticeContent());
			result = pstmt.executeUpdate();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
		}

		return result;
	}

	@Override
	public int noticeUpdate(AdminDTO dto) {

		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL UPDATENOTICE (?, ?, ?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, dto.getNoticeId());
			cstmt.setString(2, dto.getNoticeTitle());
			cstmt.setString(3, dto.getNoticeContent());

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

	@Override
	public List<AdminDTO> loanbooklist() {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수" 
					+ " FROM " 
					+ "    ("
					+ "     SELECT" 
					+ "        ui.user_name AS 유저이름, " 
					+ "        lon.LOAN_CODE AS 대출번호, "
					+ "        lon.book_code AS 북코드, " 
					+ "        bi.bookname AS 책이름, "
					+ "        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일, "
					+ "        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일, "
					+ "        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일, "
					+ "        bk.book_condition AS 도서상태, " 
					+ "            CASE "
					+ "                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE) "
					+ "                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE) "
					+ "                ELSE 0 " 
					+ "            END AS 연체일수 " 
					+ "        FROM loan lon "
					+ "        JOIN userinfo ui ON ui.user_code = lon.user_code "
					+ "        JOIN book bk ON bk.book_code = lon.book_code "
					+ "        JOIN bookinfo bi ON bi.isbn = bk.isbn" 
					+ "    ) T " 
					+ " WHERE T.도서상태 = '대출중'  AND (T.실제반납일 IS NULL OR T.실제반납일 > TO_CHAR(SYSDATE, 'YYYY-MM-DD')) "; //

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();
				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setDue_date(rs.getString("반납예정일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.err.println("Error in loanbooklist: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return list;
	}

	@Override
	public AdminDTO loanbooksearchbybookcode(int bookcode) {

		AdminDTO dto = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT " + "    T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수 " + " FROM "
					+ "    ( " + "     SELECT " + "        ui.user_name AS 유저이름, " + "        lon.LOAN_CODE AS 대출번호, "
					+ "        lon.book_code AS 북코드, " + "        bi.bookname AS 책이름, "
					+ "        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일, "
					+ "        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일, "
					+ "        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일, "
					+ "        bk.book_condition AS 도서상태, " + "            CASE "
					+ "                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE)"
					+ "                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE)"
					+ "                ELSE 0 " + "            END AS 연체일수 " + "        FROM loan lon "
					+ "        JOIN userinfo ui ON ui.user_code = lon.user_code"
					+ "        JOIN book bk ON bk.book_code = lon.book_code"
					+ "        JOIN bookinfo bi ON bi.isbn = bk.isbn" + "    ) T " 
					+ " WHERE 북코드 = ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bookcode);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				dto = new AdminDTO();

				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setDue_date(rs.getString("반납예정일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));

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
	public int loanbookreturn(AdminDTO dto) {

		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL loanbookreturn (?, ?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, dto.getBookcode());
			cstmt.setString(2, dto.getBook_condition());

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
	public List<AdminDTO> loanbooksearchbyname(String username) {

		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = "SELECT T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수 "
					+ "					 FROM " 
					+ "					   (  " 
					+ "					    SELECT  "
					+ "					        ui.user_name AS 유저이름,  "
					+ "					       lon.LOAN_CODE AS 대출번호,  "
					+ "					        lon.book_code AS 북코드,  "
					+ "					     bi.bookname AS 책이름,  "
					+ "					        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일,  "
					+ "					        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일,  "
					+ "					        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일,  "
					+ "					        bk.book_condition AS 도서상태,  " + "					            CASE  "
					+ "					                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE) "
					+ "					                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE) "
					+ "					                ELSE 0  " + "					            END AS 연체일수  "
					+ "					        FROM loan lon  "
					+ "					        JOIN userinfo ui ON ui.user_code = lon.user_code "
					+ "					        JOIN book bk ON bk.book_code = lon.book_code "
					+ "					        JOIN bookinfo bi ON bi.isbn = bk.isbn " 
					+ "					    ) T  "
					+ "					WHERE 유저이름 LIKE ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + username + "%");
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();

				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setDue_date(rs.getString("반납예정일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));
				
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs);
			DBUtil.close(pstmt);
		}
		return list;
	}
	
	@Override
	public List<AdminDTO> overdueloanbooklist() {
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수" 
					+ " FROM " 
					+ "    ("
					+ "     SELECT" 
					+ "        ui.user_name AS 유저이름, " 
					+ "        lon.LOAN_CODE AS 대출번호, "
					+ "        lon.book_code AS 북코드, " 
					+ "        bi.bookname AS 책이름, "
					+ "        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일, "
					+ "        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일, "
					+ "        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일, "
					+ "        bk.book_condition AS 도서상태, " 
					+ "            CASE "
					+ "                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE) "
					+ "                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE) "
					+ "                ELSE 0 " 
					+ "            END AS 연체일수 " 
					+ "        FROM loan lon "
					+ "        JOIN userinfo ui ON ui.user_code = lon.user_code "
					+ "        JOIN book bk ON bk.book_code = lon.book_code "
					+ "        JOIN bookinfo bi ON bi.isbn = bk.isbn" 
					+ "    ) T " 
					+ " WHERE 연체일수 >= 1 "; //

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();
				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setDue_date(rs.getString("반납예정일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.err.println("Error in loanbooklist: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return list;
	}
	
	@Override
	public List<AdminDTO> returnbooklist() {
	
		List<AdminDTO> list = new ArrayList<AdminDTO>();
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sql;

		try {
			sql = " SELECT T.유저이름, T.대출번호, T.북코드, T.책이름, T.대출일, T.반납예정일, T.실제반납일, T.도서상태, T.연체일수" 
					+ " FROM " 
					+ "    ("
					+ "     SELECT" 
					+ "        ui.user_name AS 유저이름, " 
					+ "        lon.LOAN_CODE AS 대출번호, "
					+ "        lon.book_code AS 북코드, " 
					+ "        bi.bookname AS 책이름, "
					+ "        TO_CHAR(lon.CHECKOUT_DATE, 'YYYY-MM-DD') AS 대출일, "
					+ "        TO_CHAR(lon.DUE_DATE, 'YYYY-MM-DD') AS 반납예정일, "
					+ "        TO_CHAR(lon.RETURN_DATE, 'YYYY-MM-DD') AS 실제반납일, "
					+ "        bk.book_condition AS 도서상태, " 
					+ "            CASE "
					+ "                WHEN lon.return_date IS NOT NULL AND lon.return_date > lon.DUE_DATE THEN TRUNC(lon.return_date - lon.DUE_DATE) "
					+ "                WHEN lon.return_date IS NULL AND lon.DUE_DATE < SYSDATE THEN TRUNC(SYSDATE - lon.DUE_DATE) "
					+ "                ELSE 0 " 
					+ "            END AS 연체일수 " 
					+ "        FROM loan lon "
					+ "        JOIN userinfo ui ON ui.user_code = lon.user_code "
					+ "        JOIN book bk ON bk.book_code = lon.book_code "
					+ "        JOIN bookinfo bi ON bi.isbn = bk.isbn" 
					+ "    ) T " 
					+ " WHERE T.도서상태 = '반납' "; //

			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				AdminDTO dto = new AdminDTO();
				dto.setUsername(rs.getString("유저이름"));
				dto.setLoancode(rs.getInt("대출번호"));
				dto.setBookcode(rs.getInt("북코드"));
				dto.setBookname(rs.getString("책이름"));
				dto.setCheckout_date(rs.getString("대출일"));
				dto.setReturn_date(rs.getString("실제반납일"));
				dto.setBook_condition(rs.getString("도서상태"));
				dto.setOverdue_date(rs.getInt("연체일수"));

				list.add(dto);
			}
		} catch (Exception e) {
			System.err.println("Error in loanbooklist: " + e.getMessage());
			e.printStackTrace();
		} finally {
			DBUtil.close(pstmt);
			DBUtil.close(rs);
		}
		return list;
	}

	@Override
	public int returnbook_baega(int bookCode) {

		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL returnbook_baega (?, ?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setInt(1, bookCode);
			cstmt.setString(2, "대출가능");

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
	public int returnbook_baega_all(AdminDTO dto) {


		int result = 0;
		CallableStatement cstmt = null;
		String sql;

		try {
			sql = "CALL returnbook_baega (?)";

			cstmt = conn.prepareCall(sql);

			cstmt.setString(2, dto.getBook_condition());

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
	
}