package library;

import java.sql.SQLException;
import java.util.List;


public interface AdminDAO2 {
	
	public List<AdminDTO2> sinchoengdagidoseo();					// 신청도서 불러오기
	public String truncateString(String text, int maxLength);	// 신청 글자수가 길면 잘라줌
	public int sujeongsincheongstatus(AdminDTO2 dto) throws SQLException; // 
	public List<AdminDTO2> notice() ;	// 전체 공지사항 불러오기
	public AdminDTO2 selectNoticeById(int noticeId);	// 공지사항 번호로 찾기
	public int noticeDelete(int noticeId) ; // 공지사항 번호로 삭제하기 
	public int noticeUpdate(AdminDTO2 dto) ; // 공지 번호로 수정 하기
	public int noticeInsert(AdminDTO2 dto); // 공지사항 등록하기
	public List<AdminDTO2> loanbooklist();
}
