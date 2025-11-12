package library;

import java.util.List;

// 관리자 기능 관련 DB 작업을 위한 인터페이스
 
public interface AdminDAO {

    public MemberDTO findUserById(String userId);
    public MemberDTO findUserByCode(int userCode);
    public boolean deleteUserByCode(int userCode);
    public List<MemberDTO> findUserByName(String name);
    public List<MemberDTO> findAllUsers();
    public List<BookInfoDTO1> findAllBooks();
    public List<BookInfoDTO1> findBooksByName(String bookName);
    public boolean insertBook(BookInfoDTO1 newBook);
    public BookInfoDTO1 findBookByCode(int bookCode);
    public boolean deleteBookByCode(int bookCode);
    public boolean registerDisposedBook(int bookCode, String reason);
    public boolean deleteDisposedBook(int bookCode);
    public List<DisposedBookDTO> findAllDisposedBooks();
}