package library;

import java.sql.SQLException;
import java.util.List;

// 관리자 기능 관련 DB 작업을 위한 인터페이스
 
public interface AdminDAO {

    public MemberDTO findUserById(String userId);
    public MemberDTO findUserByCode(int userCode);
    public boolean deleteUserByCode(int userCode);
    public List<MemberDTO> findUserByName(String name);
    public List<MemberDTO> findAllUsers();
    public List<BookInfoDTO> findAllBooks();
    public List<BookInfoDTO> findBooksByName(String bookName);
    public boolean insertBook(BookInfoDTO newBook);
    public BookInfoDTO findBookByCode(int bookCode);
    public boolean deleteBookByCode(int bookCode);
    public boolean registerDisposedBook(int bookCode, String reason);
    public boolean deleteDisposedBook(int bookCode);
    public List<DisposedBookDTO> findAllDisposedBooks();
    
    public List<AdminDTO> sinchoengdaegidoseo();					// 신청도서 불러오기
	public String truncateString(String text, int maxLength);	// 신청 글자수가 길면 잘라줌
	public int sujeongsincheongstatus(AdminDTO dto) throws SQLException; // 
	public List<AdminDTO> notice() ;	// 전체 공지사항 불러오기
	public AdminDTO selectNoticeById(int noticeId);	// 공지사항 번호로 찾기
	public int noticeDelete(int noticeId) ; // 공지사항 번호로 삭제하기 
	public int noticeUpdate(AdminDTO dto) ; // 공지 번호로 수정 하기
	public int noticeInsert(AdminDTO dto); // 공지사항 등록하기
	public List<AdminDTO> loanbooklist();		// 대출중 도서 리스트 출력
	public AdminDTO loanbooksearchbybookcode(int bookcode);	// 대출한 책 bookcode 로 조회하기
	public int loanbookreturn(AdminDTO dto);	// 강제로 대출중 -> 반납처리
	public List<AdminDTO> loanbooksearchbyname(String username) ;	// 이름으로 도서 대여 이력 조회
	public List<AdminDTO> overdueloanbooklist()	; // 연체된 도서 확인하기
	public List<AdminDTO> returnbooklist();		// 반납중 도서 리스트 출력
	public int returnbook_baega(int bookCode) ; // 반납 도서들 배가
	public int returnbook_baega_all(AdminDTO dto) ; // 반납 도서들 전체 배가 (대출가능 만 받아짐)
}